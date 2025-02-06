package testSuite.in_progress.stack_overflow.result_set;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Test {


    public static void main(Connection con, String username, String password) throws Exception {
        PreparedStatement p_stat =
            // con.prepareStatement("select typeid from users where username=? and password=?");  
            con.prepareStatement("select typeid from users where username=? and password=?",
            ResultSet.TYPE_SCROLL_SENSITIVE, 
            ResultSet.CONCUR_UPDATABLE);

        p_stat.setString(1, username);
        p_stat.setString(2, password);
        
        ResultSet rs = p_stat.executeQuery();
        int rowCount=0;
        while(rs.next()){       
            rowCount++;         
        }
        rs.beforeFirst(); // ERROR because it is FORWARD_ONLY
        // TO BE CORRECT::::
        
    }
    
}






// Main point of intro/paper:
// Verify Refinements for stateful objects

// Approach:
// Refinements are helpful to prevent bugs
// In OOP, we have many protocols, with typestate we can prevent those bugs 
// So we can get the best of both worlds, and add typestate using refinements and model them together
// Presence of aliasing

// We can use refinement types to model typestate
// and by doing so, we can encode properties that join predicates with the typestate.

// Contributions:
// 1. an approach to use refinement type systems to model stateful objects and their protocols
// 2. a formalization of the approach
// 3. adopting the dependent type reasoning to typestate

// [Aliasing - Latte]
// Implementation Latte.
// Attention to - Behavior of destructive reads!

// [LiquidJava]
// Grammar - reuse 90%
// Type checking rules using Latte information
//              - using latte
//              - state changes w/ forgetting and remembering
// More implementation


// Comparisons:
// Plural
// Flux