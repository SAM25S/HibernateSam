package controller; // Ensure the correct package name for your servlet

import model.Book;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

@WebServlet("/add")
public class BookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("bookname");
        String price = req.getParameter("price");

        Book book = new Book();
        book.setBookName(name);
        book.setPrice(price);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(book);
            tx.commit();
            res.sendRedirect("addbook.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}