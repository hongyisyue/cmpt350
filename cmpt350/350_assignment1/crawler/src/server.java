/**
 * Created by homeyxue on 2018-01-25.
 */
import java.io.*;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class server {

    private static final int fNumberOfThreads = 10;
    private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);

    public static void main(String[] args) throws IOException {
        System.out.println("Enter the path: ");
        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        ServerSocket socket = new ServerSocket(1024);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = () -> HandleRequest(connection, path);
            fThreadPool.execute(task);
        }
    }

    private static void HandleRequest(Socket s, String path) {  //handle request from browser
        BufferedReader br;
        PrintStream ps;
        String request;

        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            request = br.readLine();

            String browser = request.split(" ")[1];
            String file_name = browser.substring(1, browser.length());

            String File = getFile(path, file_name);

            ps = new PrintStream(s.getOutputStream(), true);
            String response;

                response = File;

            ps.println("HTTP/1.0 200");
            ps.println("Content-type: text/html");
            ps.println("");
            ps.println(response);
            s.close();
        } catch (IOException e) {
            System.out.println("Fail to respond client request: " + e.getMessage());
        }
    }

    public static String getFile(String path, String file_name) throws IOException {       //find file using given path and check if it matches local file
        File file = new File(path);
        File[] list = file.listFiles();
        if (list != null) {
            for (File files : list) {
                if (files.getName().equals(file_name)) {
                    return transFile(files.getAbsolutePath());
                }
            }
        }
        return transFile("/Users/homeyxue/Desktop/cmpt350/as_1/not_found.html");     //handle case if file not found
    }

    static String transFile(String path)     //translate file content into html file format
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String result = new String(encoded, StandardCharsets.UTF_8);
        return result;
    }
}