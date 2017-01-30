package tc.oc.chatmoderator.template;

import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

/**
 * Represents a filter template.
 */
public class Template {

    /**
     * The template to base the pattern off of.
     */
    private String template;

    /**
     * The word to insert into a template.
     */
    private String word;

    /**
     * The resulting pattern of combining the template and word together.
     */
    private Pattern result;

    /**
     * Instantiates a template factory, specifies how to filter out a word in ChatModerator.
     *
     * @param template The template to go off of.
     * @param word The word to insert into the template.
     */
    public Template(String template, String word) {
        this.template = Preconditions.checkNotNull(template, "template");
        this.word = Preconditions.checkNotNull(word, "word");
    }

    /**
     * Preforms the necessary logic to build the pattern.
     *
     * @return The current state of the template.
     */
    public Template build() {
        this.result = Pattern.compile(
                template.replace("%FIRST", word.charAt(0) + "")
                        .replace("%REST", word.substring(1))
                        .replace("%WHOLE", word),
                Pattern.CASE_INSENSITIVE
        );

        return this;
    }

    /**
     * Gets the resulting pattern, usually called after {@code build()}.
     *
     * @return The pattern.
     */
    public Pattern getPattern() {
        return this.result;
    }
}
