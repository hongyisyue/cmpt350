import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.LinkedList;
import java.net.URL;

@WebServlet(name = "MiddleServlet")
public class MiddleServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.sendRedirect("SQLServlet");
        System.out.println("start send post");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://127.0.0.1:8081/SQLServlet?message="+request.getParameter("message")).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true); // Triggers POST.
        response.sendRedirect("index.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("SQLServlet");
    }
}
