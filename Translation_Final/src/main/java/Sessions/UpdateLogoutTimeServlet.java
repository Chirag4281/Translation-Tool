package Sessions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

@WebServlet("/UpdateLogoutTime")
public class UpdateLogoutTimeServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Translator";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        out.println("New");
        if (session != null) {
            String username = (String) session.getAttribute("username");

            if (username != null) {
                long logoutTime = System.currentTimeMillis();

                try 
                {   
                     Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    String sql = "UPDATE user_sessions SET logout_time = ? WHERE username = ? AND logout_time IS NULL";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setTimestamp(1, new java.sql.Timestamp(logoutTime));
                        stmt.setString(2, username);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UpdateLogoutTimeServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
