package testSuite.in_progress.searching_state_space.url_connection_reuse_connection;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URLConnection
 * 
 * ->  STATE_OPENED -> STATE_SETTER -> STATE_CONNECTED
 */
public class Test {
    /*
     * Error cannot set property before opening connection
     */
     public static void example4278917(URL address) {
        try {

            // Step 1) Open the connection
            URLConnection connection = address.openConnection(); 

            // Step 2) Connect
            connection.connect();
            
            /*  Other code in original question */
            
            // Step 3) Setup parameters and connection properties after connection == ERROR
            connection.setAllowUserInteraction(true);
            connection.addRequestProperty("AUTHENTICATION_REQUEST_PROPERTY", "authorizationRequest");
            connection.getHeaderFields();

        } catch (IOException e) {
            // Handle exceptions related to network or stream issues
            System.err.println("Error: " + e.getMessage());
        }
    }
}
