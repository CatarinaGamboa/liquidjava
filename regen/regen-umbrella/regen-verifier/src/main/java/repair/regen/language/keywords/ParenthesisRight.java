package repair.regen.language.keywords;

import org.modelcc.IModel;
import org.modelcc.Pattern;
import org.modelcc.Priority;

@Priority(value = 2)
@Pattern(regExp = "\\)")
public class ParenthesisRight implements IModel {

}
