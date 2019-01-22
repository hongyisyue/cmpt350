/**
 * Created by homeyxue on 2018-01-23.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class crawler {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] Args)
    {
        System.out.println("Please enter the URL: ");
        String input_URL = scanner.nextLine();
        System.out.println("Please enter the command number: ");
        int input_num = scanner.nextInt();
        connect(input_URL, input_num);
    }

    protected static void connect(String URL, int num)
    {

        try
        {
            String CRLF = "\r\n";

            Socket s = new Socket(URL,80);

            BufferedReader br;
            PrintStream ps;

            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());


            // send request

            ps.print("GET / HTTP/1.0" +  CRLF);
            ps.print("Accept:text/html" + CRLF);
            ps.print(CRLF + CRLF);


            String data = br.readLine();

            if(num <= 0) {
                while (data != null) {
                    ArrayList<String> urls = pullLinks(data);
                    for (int i =0; i< urls.size(); i++){
                        System.out.println(urls.get(i));            //print each url found in the given HTML page
                    }
                    data = br.readLine();
                }
            }

            else {
                while (data != null) {
                    ArrayList<String> urls = pullLinks(data);
                    for (int i =0; i< urls.size(); i++){
                        System.out.println("No."+(i+1)+" URL's sub-URLs: ");
                        connect(urls.get(i),-1);            //for each url found, call crawler
                    }
                    data = br.readLine();
                }
            }


        } catch (Exception E) {System.out.println(E); }
    }

        protected static ArrayList<String> pullLinks(String text) {

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        ArrayList<String> URLs = new ArrayList<>();
        while(m.find()) {
            String urlStr = m.group();
            if(urlStr.contains("://")) {
                urlStr = urlStr.split("://")[1];
            }
            URLs.add(urlStr);
        }
        return URLs;
    }
    }
