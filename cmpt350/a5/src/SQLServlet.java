import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.*;

@WebServlet(name = "SQLServlet")
public class SQLServlet extends HttpServlet {

    int id;

    MessageSQL messageCollection;

    int time = 0;

    LinkedList mes;

    int numRequest;
    
    public SQLServlet(){
        super();
        messageCollection = new MessageSQL();
        id = messageCollection.getMaxId()+1;
        messageCollection.done();

        mes = new LinkedList();
        time =(int) (System.currentTimeMillis() / 1000L);
        numRequest = 0;
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int time2 = (int) (System.currentTimeMillis() / 1000L);
        System.out.println(time2);
        String message;
        Integer user;
        try {
            message = request.getParameter("message");
            user = Integer.parseInt(request.getParameter("userId"));
            // System.out.println(System.currentTimeMillis() / 1000L);

        }catch(Exception e){
            response.sendRedirect("show.html?message=invalid%20request");
            return;
        }
        messageCollection = new MessageSQL();
        messageCollection.insertMessage(id,message,user,time2);
        id++;
        messageCollection.done();
        response.sendRedirect("index.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String option = "";
        String list = "";
        try {
            option = request.getParameter("option");
            list = request.getParameter("idList");
            if(!option.equals("0") && !option.equals("1") && !option.equals("2") && !option.equals("3")){
                response.sendRedirect("show.html?message=invalid%20option");
                return;
            }
        }catch(Exception e){
            if(!option.equals("0")){
                response.sendRedirect("show.html?message=need%20list%20of%20user");
                return;
            }
        }
        String [] some = list.split(",");
        String collection = "";
        messageCollection = new MessageSQL();
        LinkedList messcoll = messageCollection.getMessage();

        switch (option){
            case "0":
                for(int i=0;i<messcoll.size();i+=3){
                    collection+=messcoll.get(i+2);
                    collection+=" ";
                    collection+=messcoll.get(i+1);
                    collection+=", ";
                }
                break;
            case "1":
                for(int i=0;i<messcoll.size();i+=3){
                    if(Arrays.asList(some).contains(messcoll.get(i))){
                        collection+=messcoll.get(i+1);
                        collection+=" ";
                        collection+=messcoll.get(i);
                        collection+=", ";
                    }
                    mes.add(messcoll.get(i));
                }

                break;
            case "2":
                for(int i=0;i<messcoll.size();i+=3){
                    if(!Arrays.asList(some).contains(messcoll.get(i))){
                        collection+=messcoll.get(i+1);
                        collection+=" ";
                        collection+=messcoll.get(i);
                        collection+=", ";
                    }
                }
                break;
            case "3":
                System.out.println("start print messcoll");
                for(int i=0;i<messcoll.size();i++){
                    System.out.println(messcoll.get(i));
                }
                System.out.println("end print messcoll");
                for(int i=0;i<messcoll.size();i+=3) {
                    if (Arrays.asList(some).contains(messcoll.get(i+1))) {
                        System.out.println("last time: "+time);
                        System.out.println("store time: "+Integer.parseInt( (String)messcoll.get(i)));
                        if (Integer.parseInt( (String)messcoll.get(i)) > time) {
                            System.out.println("last time: "+time);
                            System.out.println("store time: "+(String)messcoll.get(i));
                            collection += messcoll.get(i + 2);
                            collection += " ";
                            collection += messcoll.get(i + 1);
                            collection += ", ";

                        }
                    }
                }
                break;
        }
        if(numRequest %2 != 0) {
            time = (int) (System.currentTimeMillis() / 1000L);
        }
        numRequest++;
        System.out.println("time 1: "+time);
        messageCollection.done();
        response.sendRedirect("show.html?message="+collection);
        return;
    }
}