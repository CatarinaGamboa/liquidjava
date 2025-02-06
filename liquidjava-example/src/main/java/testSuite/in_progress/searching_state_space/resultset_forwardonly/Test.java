package testSuite.in_progress.searching_state_space.resultset_forwardonly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ResultSet
 */
public class Test {

    /*
     * Error ResultSet is FORWARD_ONLY and we try to get a value before
     */
     public static void example6367737(Connection con, String username, String password ) throws Exception {

      // Step 1) Prepare the statement
      PreparedStatement pstat =
        con.prepareStatement("select typeid from users where username=? and password=?");  

      // Step 2) Set parameters for the statement
      pstat.setString(1, username);
      pstat.setString(2, password);

      // Step 3) Execute the query
      ResultSet rs = pstat.executeQuery();

      // Step 4) Process the result
      int rowCount=0;
      while(rs.next()){       
          rowCount++;         
      }

      // ERROR! because it is FORWARD_ONLY, we cannot go back and check beforeFirst
      rs.beforeFirst(); 

      int typeID = 0;
      if(rowCount>=1) {
      while(rs.next()){
            typeID=rs.getInt(1);
        }
      }

      // To be correct we need to change the resultset to be scrollable
      /*PreparedStatement pstat =      
        con.prepareStatement("select typeid from users where username=? and password=?",
        ResultSet.TYPE_SCROLL_SENSITIVE, 
        ResultSet.CONCUR_UPDATABLE);
      */
    }
}
