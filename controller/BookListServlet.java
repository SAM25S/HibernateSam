package controller;

import model.Book;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.html");
            return;
        }

        try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = hibernateSession.createQuery("FROM Book", Book.class);
            List<Book> books = query.getResultList();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            out.println("<html><head><title>Book List</title></head><body>");
            out.println("<h1>All Books</h1>");
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Price</th></tr>");
            for (Book book : books) {
                out.println("<tr><td>" + book.getBookId() + "</td><td>" + book.getBookName() + "</td><td>" + book.getPrice() + "</td></tr>");
            }
            out.println("</table>");
            out.println("<br><a href='addbook.html'>Add Book</a> | <a href='logout'>Logout</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not retrieve book list.");
        }
    }
}
