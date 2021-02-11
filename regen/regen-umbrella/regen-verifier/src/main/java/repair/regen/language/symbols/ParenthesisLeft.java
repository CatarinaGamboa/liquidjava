package repair.regen.language.symbols;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

@Priority(value = 2)
@Pattern(regExp = "\\(")
public class ParenthesisLeft implements IModel {

}
