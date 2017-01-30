package tc.oc.chatmoderator.util;

import org.bukkit.ChatColor;
import tc.oc.chatmoderator.words.Word;

/**
 * Represents a class to apply a fix style to a word.
 */
public final class FixStyleApplicant {

    public static Word fixWord(Word word, FixStyle style) {
        return new Word(FixStyleApplicant.fixString(word.getWord(), style));
    }

    public static String fixString(String string, FixStyle style) {
        StringBuilder builder = new StringBuilder();

        switch(style) {
            case MAGIC:
            case STAR:
            case UNDERSCORE:
            case DASH:
                builder.append(string.charAt(0));
                for (int i = 0; i < string.length() - 2; i++) {

                    switch (style) {
                        case MAGIC:
                            builder.append(ChatColor.MAGIC + "" + string.charAt(i + 1));
                            break;
                        case STAR:
                            builder.append("*");
                            break;
                        case UNDERSCORE:
                            builder.append("_");
                            break;
                        case DASH:
                            builder.append("-");
                            break;
                    }

                }
                builder.append(ChatColor.RESET + "" + string.charAt(string.length() - 1));
                break;
            case NONE:
                builder.append(string);
            case REMOVE:
                break;
        }

        return builder.toString();
    }

    /**
     * Defines the style to fix a offending word with.
     */
    public static enum FixStyle {
        /**
         * Fill the missing spaces with ChatColor.MAGIC.
         */
        MAGIC,

        /**
         * Remove the word entirely.
         */
        REMOVE,

        /**
         * Place stars (i.e., '*') in the middle of the word.
         */
        STAR,

        /**
         * Place underscores in the middle of the word.
         */
        UNDERSCORE,

        /**
         * Place dashes in the middle of the word.
         */
        DASH,

        /**
         * Leave the word as-is.
         */
        NONE;

        public static FixStyle getFixStyleFor(String name) {
            for (FixStyle style : values()) {
                if (style.name().equals(name)) {
                    return style;
                }
            }

            return NONE;
        }
    }
}
