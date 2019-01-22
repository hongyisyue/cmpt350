import java.sql.*;
import java.util.LinkedList;

public class MessageSQL {

    Connection conn;

    PreparedStatement p;

    ResultSet temp;

    public MessageSQL(){
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5431/postgres",
                            "yugu", "yanzoujia");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        doExecutionWithoutReturn("create schema IF NOT EXISTS testschema;");
        doExecutionWithoutReturn("create table IF NOT EXISTS testschema.MessageNewTable3(id integer not null, message text not null, userId integer not null, date integer not null, PRIMARY KEY (id));");
    }

    private void doExecutionWithoutReturn(String input){
        try{
            p = conn.prepareStatement(input);
            p.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private LinkedList<String> doExecutionWithReturn(String input){
        try{
            p = conn.prepareStatement(input);
            temp = p.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return printResultSet();
    }

    public void done(){
        // doExecutionWithoutReturn("drop schema MessageSchema cascade;");
        try{
            p.close();
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private LinkedList<String> printResultSet(){
        LinkedList<String> result = new LinkedList<>();
        try {
            ResultSetMetaData rsmd = temp.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (temp.next()) {
                int i;
                // case for get max id
                if(columnsNumber == 1){
                    i = 1;
                }
                // case for get message
                else{
                    i = 2;
                }
                for (; i <= columnsNumber; i++) {
                    //if (i > 1) System.out.print(",  ");
                    System.out.println("column" + temp.getString(i));
                    String columnValue = temp.getString(i);
                    result.push(columnValue);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private LinkedList<String> getAllTable(){
        LinkedList<String> result = new LinkedList<>();
        try {
            ResultSetMetaData rsmd = temp.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (temp.next()) {
                String columnValue = temp.getString(2);
                result.add(columnValue);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private String doInsertMessageString(Integer id, String message, Integer userId, Integer date){
        String argument = "insert into testschema.MessageNewTable3(id,message,userId,date) values(";
        argument = argument.concat(id.toString());
        argument = argument.concat(", '");
        argument = argument.concat(message);
        argument = argument.concat("', ");
        argument = argument.concat(userId.toString());
        argument = argument.concat(",");
        argument = argument.concat(date.toString());
        argument = argument.concat(");");
        id++;
        return argument;
    }

    private String doSelectAllPrepareString(){
        return "select * from testschema.MessageNewTable3;";
    }

    public void insertMessage(Integer id, String message, Integer userId, Integer date){
        doExecutionWithoutReturn(doInsertMessageString(id,message,userId, date));
    }

    public LinkedList<String> getMessage() {
        return doExecutionWithReturn(doSelectAllPrepareString());
    }

    public int getMaxId(){
        LinkedList<String> some = doExecutionWithReturn("select max(id) from testschema.MessageNewTable3;");
        try{
            return Integer.parseInt(some.get(0));
        }catch (Exception e){
            return 1;
        }

    }

    public static void main(String args[]) {
        MessageSQL some = new MessageSQL();
        some.done();
    }
}
