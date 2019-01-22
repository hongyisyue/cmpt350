/**
 * Created by homeyxue on 2018-03-21.
 */
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
                    .getConnection("jdbc:postgresql://localhost:5430/",
                            "postgres", "19951212dtm");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        doExecutionWithoutReturn("create schema IF NOT EXISTS MessageSchema;");
        doExecutionWithoutReturn("create table IF NOT EXISTS MessageSchema.MessageTable(id integer not null, message text not null, userid text not null, PRIMARY KEY (id));");
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

//    private LinkedList<String> getAllTable(){
//        LinkedList<String> result = new LinkedList<>();
//        try {
//            ResultSetMetaData rsmd = temp.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            while (temp.next()) {
//                String columnValue = temp.getString(2);
//                result.add(columnValue);
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return result;
//    }

    private String doInsertMessageString(Integer id, String message, String userId){
        String argument = "insert into MessageSchema.MessageTable(id,message,userid) values(";
        argument = argument.concat(id.toString());
        argument = argument.concat(", '");
        argument = argument.concat(message);
        argument = argument.concat("', '");
        argument = argument.concat(userId);
        argument = argument.concat("');");
        id++;
        return argument;
    }

    private String doSelectAllPrepareString(){
        return "select * from MessageSchema.MessageTable;";
    }

    private String doSelectIdString(String aId) {return "select * from MessageSchema.MessageTable where userid = '" + aId + "';" ;}

    public void insertMessage(Integer id, String message, String userId){
        doExecutionWithoutReturn(doInsertMessageString(id,message,userId));
    }

    public LinkedList<String> getMessage() {
        return doExecutionWithReturn(doSelectAllPrepareString());
    }

    public LinkedList<String> getIdMessage(String id) {
        return doExecutionWithReturn(doSelectIdString(id));
    }

    public int getMaxId(){
        LinkedList<String> some = doExecutionWithReturn("select max(id) from MessageSchema.MessageTable;");
        try{
            return Integer.parseInt(some.get(0));
        }catch (Exception e){
            return 1;
        }

    }
//
//    public static void main(String args[]) {
//        MessageSQL some = new MessageSQL();
//        some.insertMessage(0,"hello world");
//        some.insertMessage(1,"world hello");
//        System.out.println(some.getMessage());
//        some.done();
//    }

}

//import java.sql.*;
//import java.util.LinkedList;
//
//public class MessageSQL {
//
//    Connection conn;
//
//    PreparedStatement p;
//
//    ResultSet temp;
//
//    public MessageSQL(){
//        try {
//            Class.forName("org.postgresql.Driver");
//            conn = DriverManager
//                    .getConnection("jdbc:postgresql://localhost:5430/",
//                            "postgres", "19951212dtm");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getClass().getName()+": "+e.getMessage());
//            System.exit(0);
//        }
//        doExecutionWithoutReturn("create schema IF NOT EXISTS MessageSchema;");
//        doExecutionWithoutReturn("create table IF NOT EXISTS MessageSchema.MessageTable(id integer not null, message text not null, userId integer not null, PRIMARY KEY (id));");
//    }
//
//    private void doExecutionWithoutReturn(String input){
//        try{
//            p = conn.prepareStatement(input);
//            p.executeUpdate();
//
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
//
//    private LinkedList<String> doExecutionWithReturn(String input){
//        try{
//            p = conn.prepareStatement(input);
//            temp = p.executeQuery();
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return printResultSet();
//    }
//
//    public void done(){
//        // doExecutionWithoutReturn("drop schema MessageSchema cascade;");
//        try{
//            p.close();
//            conn.close();
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//    }
//
//    private LinkedList<String> printResultSet(){
//        LinkedList<String> result = new LinkedList<>();
//        try {
//            ResultSetMetaData rsmd = temp.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            while (temp.next()) {
//                int i;
//                // case for get max id
//                if(columnsNumber == 1){
//                    i = 1;
//                }
//                // case for get message
//                else{
//                    i = 2;
//                }
//                for (; i <= columnsNumber; i++) {
//                    //if (i > 1) System.out.print(",  ");
//                    System.out.println("column" + temp.getString(i));
//                    String columnValue = temp.getString(i);
//                    result.push(columnValue);
//                }
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private LinkedList<String> getAllTable(){
//        LinkedList<String> result = new LinkedList<>();
//        try {
//            ResultSetMetaData rsmd = temp.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            while (temp.next()) {
//                String columnValue = temp.getString(2);
//                result.add(columnValue);
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private String doInsertMessageString(Integer id, String message, String userId, Integer date){
//        String argument = "insert into testschema.MessageNewTable3(id,message,userId,date) values(";
//        argument = argument.concat(id.toString());
//        argument = argument.concat(", '");
//        argument = argument.concat(message);
//        argument = argument.concat("', ");
//        argument = argument.concat(userId.toString());
//        argument = argument.concat(",");
//        argument = argument.concat(date.toString());
//        argument = argument.concat(");");
//        id++;
//        return argument;
//    }
//
//    private String doSelectAllPrepareString(){
//        return "select * from testschema.MessageNewTable3;";
//    }
//
//    public void insertMessage(Integer id, String message, String userId, Integer date){
//        doExecutionWithoutReturn(doInsertMessageString(id,message,userId, date));
//    }
//
//    public LinkedList<String> getMessage() {
//        return doExecutionWithReturn(doSelectAllPrepareString());
//    }
//
//    public int getMaxId(){
//        LinkedList<String> some = doExecutionWithReturn("select max(id) from testschema.MessageNewTable3;");
//        try{
//            return Integer.parseInt(some.get(0));
//        }catch (Exception e){
//            return 1;
//        }
//
//    }
//
//    public static void main(String args[]) {
//        MessageSQL some = new MessageSQL();
//        some.done();
//    }
//}
