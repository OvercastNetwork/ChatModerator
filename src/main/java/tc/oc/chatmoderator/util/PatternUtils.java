package tc.oc.chatmoderator.util;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Utility class for Patterns and ChatModerator
 */
public class PatternUtils {

    public static final ArrayList<Character> spaces = new ArrayList<>();

    static {
        spaces.add(' ');
        spaces.add('_');
        spaces.add('`');
        spaces.add('-');
        spaces.add('.');
        spaces.add('~');
        spaces.add('@');
        spaces.add('#');
        spaces.add('$');
        spaces.add('%');
        spaces.add('^');
        spaces.add('&');
        spaces.add('*');
        spaces.add('/');
        spaces.add('\\');
        spaces.add(';');
    }

    public static String getSplitPattern() {
        StringBuilder patternBuilder = new StringBuilder();

        for (Character c : spaces) {
            patternBuilder.append(Pattern.quote(c.toString()));
        }

        return "[" + patternBuilder.toString() + "]";
    }

    public static Pattern makePatternWordSpecific(Pattern basePattern) {
        return Pattern.compile('^' + basePattern.toString() + '$');
    }

    public static String makePatternWordSpecific(String basePattern) {
        StringBuilder builder = new StringBuilder();
        builder.append('^');
        builder.append(basePattern);
        builder.append('$');

        return builder.toString();
    }

    public static boolean doesPlayerExist(String name) {
        return Bukkit.getPlayerExact(name) != null;
    }
}
