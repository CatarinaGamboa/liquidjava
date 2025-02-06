package testSuite.in_progress.searching_state_space.resultset_read_before_next;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ResultSet
 */
public class Test {

    /*
     * Error - in ResultSet, after executing the query we need to call next() before getting a value
     */
     public static void example6367737(Connection con, String username, String password ) throws Exception {

      // Step 1) Prepare the statement
      PreparedStatement pstat =
        con.prepareStatement("select typeid from users where username=? and password=?");  

      // Step 2) Set parameters for the statement
      pstat.setString(1, username);
      pstat.setString(2, password);

      // Step 3) Execute the query
      ResultSet parentMessage = pstat.executeQuery("SELECT SUM(IMPORTANCE) AS IMPAVG FROM MAIL");

      float avgsum = parentMessage.getFloat("IMPAVG"); // Error because we are trying to get a value before next

      // To be correct we need to call next() before the getter
    }
}
