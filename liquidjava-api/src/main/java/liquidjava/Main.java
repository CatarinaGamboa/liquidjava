package liquidjava;

import example.Simple;
import java.util.List;
import liquidjava.infer.InducedRefinementsParser;
import liquidjava.utils.Processor;

/** Main class for the Repair system */
public class Main {

  public static void main(String[] args) throws Exception {

    List<String> refinements = (new Processor()).getRefinement(new Simple());

    List<String> inducedRefinements = InducedRefinementsParser.parseRefinements("output3");

    System.out.println(refinements);
    System.out.println(inducedRefinements);

    String finalRefinement = "";

    if (!refinements.isEmpty()) {
      finalRefinement += String.join(" && ", refinements);

      if (!refinements.isEmpty())
        finalRefinement += " && " + String.join(" && ", inducedRefinements);
    } else if (!inducedRefinements.isEmpty())
      finalRefinement = String.join(" && ", inducedRefinements);

    System.out.println(finalRefinement);
  }
}
