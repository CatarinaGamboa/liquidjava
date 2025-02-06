package testSuite.in_progress.searching_state_space.url_connection_set_property;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Test {
     public static void example331538(URL address) {
        try {

            // Step 1) Open the connection
            URLConnection cnx = address.openConnection(); 

            // Step 2) Setup parameters and connection properties
            cnx.setAllowUserInteraction(false); // Step 2)
            cnx.setDoOutput(true);
            cnx.addRequestProperty("User-Agent", 
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");


            // Step 3)
            cnx.connect(); 

            // Step 4)
            cnx.getContent();

            // Get the input stream and process it
            InputStream is = cnx.getInputStream();
            System.out.println("Successfully opened input stream.");

            // Ensure to close the InputStream after use
            is.close();

        } catch (IOException e) {
            // Handle exceptions related to network or stream issues
            System.err.println("Error: " + e.getMessage());
        }
    }


    /**
     * 
     */
    String sessionId = "1234";
    public  void example5368535(URL address, String content) {
        try {
            HttpURLConnection con = openConnection(address, true, true, "POST");
        
            //ERROR write before set
            writeToOutput(con, content); // writeOutput calls  cnx.getOutputStream()
            setCookies(con); // writeOutput calls cnx.setRequestProperty

            con.connect();

            System.out.println("Request completed successfully.");
        } catch (IOException e) {
            // Handle exceptions related to network or stream issues
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Exactly from the original code
    public static final HttpURLConnection openConnection(URL url, boolean in, boolean out,String requestMethode) throws IOException{
        HttpURLConnection con = (HttpURLConnection) url.openConnection ();
        con.setDoInput(in);
        con.setDoOutput (out);
        if(requestMethode == null){
            requestMethode = "GET";
        }
        con.setRequestMethod(requestMethode);
        con.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
        return con;
    }

    // Set cookies
    private  void setCookies(HttpURLConnection cnx) {
        if (sessionId != null) {
            cnx.setRequestProperty("Cookie", sessionId);
            System.out.println("Cookie set: " + sessionId);
        }
    }

    // Write content
    private void writeToOutput(HttpURLConnection cnx, String content) throws IOException {
        try {
            OutputStream os = cnx.getOutputStream() ;
            os.write(content.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            System.err.println("Error writing content: " + e.getMessage());
            throw e;
        }
    }
}
