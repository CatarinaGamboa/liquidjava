package liquidjava.diagnostics;

import java.util.HashMap;

import liquidjava.processor.context.PlacementInCode;

/**
 * Translation table mapping variable names to their placement in code
 * 
 * @see HashMap
 * @see PlacementInCode
 */
public class TranslationTable extends HashMap<String, PlacementInCode> {
    public TranslationTable() {
        super();
    }
}
