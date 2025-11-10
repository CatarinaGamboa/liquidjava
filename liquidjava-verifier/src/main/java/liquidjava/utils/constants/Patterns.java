package liquidjava.utils.constants;

import java.util.regex.Pattern;

public final class Patterns {
    public static final Pattern THIS = Pattern.compile("#this_\\d+");
    public static final Pattern INSTANCE = Pattern.compile("^#(.+)_[0-9]+$");
}
