package controller;

import model.Book;
import model.User;
import util.DateUtil;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Book> books = null;

        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = hibernateSession.createQuery("FROM Book", Book.class);
            books = query.list();
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving data");
            return;
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Welcome</title></head><body>");
        out.println("<h1>Welcome " + user.getName() + "</h1>");
        out.println("<h2>User Details</h2>");
        out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th></tr>");
        out.println("<tr><td>" + user.getId() + "</td><td>" + user.getName() + "</td><td>" + user.getEmail() + "</td><td>" + user.getRole() + "</td></tr></table>");

        out.println("<h2>Book Inventory</h2>");
        out.println("<table border='1'><tr><th>ID</th><th>Book Name</th><th>Price</th><th>Issue Date</th><th>Return Date</th><th>Issued To</th><th>Actions</th></tr>");
        if (books != null) {
            for (Book book : books) {
                out.println("<tr>");
                out.println("<td>" + book.getBookId() + "</td>");
                out.println("<td>" + book.getBookName() + "</td>");
                out.println("<td>" + book.getPrice() + "</td>");
                out.println("<td>" + (book.getIssueDate() != null ? DateUtil.formatDate(book.getIssueDate()) : "Not Issued") + "</td>");
                out.println("<td>" + (book.getReturnDate() != null ? DateUtil.formatDate(book.getReturnDate()) : "Not Set") + "</td>");
                out.println("<td>" + (book.getIssuedTo() != null ? book.getIssuedTo() : "Available") + "</td>");
                out.println("<td>");
                if (book.getIssueDate() == null) {
                    out.println("<a href='issuebook.html?id=" + book.getBookId() + "'>Issue</a>");
                } else {
                    out.println("<form action='return' method='post'>");
                    out.println("<input type='hidden' name='bookId' value='" + book.getBookId() + "'>");
                    out.println("<button type='submit'>Return</button>");
                    out.println("</form>");
                }
                out.println("</td>");
                out.println("</tr>");
            }
        }
        out.println("</table>");

        out.println("<p><a href='addbook.html'>Add New Book</a></p>");
        out.println("<p><a href='logout'>Logout</a></p>");
        out.println("</body></html>");
    }
}

