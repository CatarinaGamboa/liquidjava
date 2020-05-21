package repair.regen.infer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InducedRefinementsParser {

    
    private static final String STARTER = "====================================================== search started:";
    private static final String ENDER = "*************Summary***************";
    
    public InducedRefinementsParser() {
        // Does nothing
    }
    
    public static List<String> parseRefinements(String file) throws IOException {
        
        List<String> result = new ArrayList<>();
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();
            
            boolean start = false;
            boolean end = false;
            
            while((line = reader.readLine()) != null && !end) {
                if (!start) {
                    start = line.startsWith(STARTER);
                    continue;
                }
                else
                    end = line.startsWith(ENDER);
                
                if (start && !end)
                    if (line.contains("NPC constraint")) {

                        line = reader.readLine();

                        while (line.endsWith("&&") || line.endsWith("||"))
                            line += reader.readLine();

                        if (line.startsWith("%"))
                            line = line.substring(line.indexOf("%", 2) + 1);
                                            
                        result.add(line);
                    }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
