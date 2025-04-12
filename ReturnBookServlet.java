package controller;

import model.Book;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet("/return")
public class ReturnBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession httpSession = req.getSession(false);
        if (httpSession == null || httpSession.getAttribute("user") == null) {
            res.sendRedirect("login.html");
            return;
        }
        
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Book book = session.get(Book.class, bookId);
            
            if (book != null) {
                book.setIssueDate(null);
                book.setReturnDate(null);
                book.setIssuedTo(null);
                session.merge(book);
                tx.commit();
                res.sendRedirect("welcome");
            } else {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error returning book");
        }
    }
}
