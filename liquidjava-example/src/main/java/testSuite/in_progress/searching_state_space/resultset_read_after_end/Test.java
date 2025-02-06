package testSuite.in_progress.searching_state_space.resultset_read_after_end;

import java.sql.ResultSet;

/**
 * URLConnection
 */
public class Test {

    /*
     * Error cannot reschedule  a timer
     */
     public static Object example1801324(ResultSet rs) throws Exception {
        Object count = null;
        if (rs != null) {
            while (rs.next()) {
              count = rs.getInt(1);
            }
            count = rs.getInt(1); //this will throw Exhausted resultset
          }

        return count;
    }
}
