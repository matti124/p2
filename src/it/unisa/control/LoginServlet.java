package it.unisa.control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.unisa.model.*;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao userDao = new UserDao();

        try {
            String username = request.getParameter("un");
            String password = request.getParameter("pw");

            // Hashing della password utilizzando SHA-256
            String hashedPassword = hashWithSHA256(password);

            UserBean user = userDao.doRetrieve(username, hashedPassword);

            String checkout = request.getParameter("checkout");

            if (user != null) { // Se l'utente esiste

                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);

                if (checkout != null)
                    response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                else
                    response.sendRedirect(request.getContextPath() + "/Home.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error"); // Pagina di errore
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per l'hashing della password utilizzando SHA-256
    private String hashWithSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
