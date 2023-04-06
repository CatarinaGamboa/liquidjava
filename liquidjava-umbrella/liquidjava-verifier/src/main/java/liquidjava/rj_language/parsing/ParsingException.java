package liquidjava.rj_language.parsing;

public class ParsingException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParsingException(String errorMessage) {
        super(errorMessage);
    }

    public ParsingException(Exception e){
        super(e);
    }

}
