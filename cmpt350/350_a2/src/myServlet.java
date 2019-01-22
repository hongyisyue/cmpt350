import com.sun.xml.internal.ws.api.message.Message;
import jdk.nashorn.internal.parser.JSONParser;

import javax.jws.WebMethod;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by homeyxue on 2018-03-05.
 */
@WebServlet("/message")
public class myServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static LinkedList<String> messages;

    int id;
    MessageSQL messagesql;

    public myServlet() {
        super();
        messages = new LinkedList<>();
        messagesql = new MessageSQL();
        id = messagesql.getMaxId()+1;
        messagesql.done();
    }

    protected void doGet(HttpServletRequest mRequest, HttpServletResponse mResponse)
            throws ServletException, IOException{

        messagesql = new MessageSQL();
        messages = messagesql.getMessage();

        mResponse.setContentType("application/json");
        PrintWriter out = mResponse.getWriter();
        out.println("[");
        for(int i = 0; i < messages.size()-1; i+=2){

            out.println("\"" + messages.get(i) + ": " + messages.get(i+1) + "\"");
            if(i < messages.size() - 2) out.print(",");
        }
        out.println("]");

        messagesql.done();
    }

    protected void doPost(HttpServletRequest mRequest, HttpServletResponse mResponse)
            throws ServletException, IOException{

        messagesql = new MessageSQL();
        String mMessage = mRequest.getParameter("message");
        String mUserId = mRequest.getParameter("userid");
        messagesql.insertMessage(id, mMessage, mUserId);
        id++;
        messagesql.done();

        //mResponse.sendRedirect("index.html");
    }

    protected void doPut(HttpServletRequest mRequest, HttpServletResponse mResponse)
            throws ServletException, IOException{

        messagesql = new MessageSQL();
        String mUserId = mRequest.getParameter("useridgiven");

        if(mUserId.equals(null)){
            messages = messagesql.getMessage();
        }else {
            messages = messagesql.getIdMessage(mUserId);
        }

        Collections.reverse(messages);

        mResponse.setContentType("application/json");
        PrintWriter out = mResponse.getWriter();
        out.println("[");
        for(int i = 0; i < messages.size(); i++){

            out.println("\"" + messages.get(i) + "\"");
            if(i < messages.size() - 1) out.print(",");
        }
        out.println("]");
        System.out.println(messages); // it returns the right data here

        messagesql.done();

        //mResponse.sendRedirect("index.html");


    }

}


