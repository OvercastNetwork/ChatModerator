package tc.oc.chatmoderator.words.factories;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;

import java.util.regex.Pattern;

/**
 * Factory class for building a set of Words.
 */
public class WordSetFactory {

    /**
     * The message to base the {@link tc.oc.chatmoderator.words.WordSet} off of.
     */
    private final FixedMessage message;

    /**
     * The {@link tc.oc.chatmoderator.words.WordSet} that we will eventually return.
     */
    private WordSet wordSet = new WordSet();

    /**
     * Whether or not to use the fixed message, or the original message sent in chat.
     */
    private boolean useFixed;

    /**
     * The pattern to split the words by.
     */
    private Pattern splitPattern;

    /**
     * Creates a {@link tc.oc.chatmoderator.words.factories.WordSetFactory} using the fixed message as a default.
     *
     * @param message The {@link tc.oc.chatmoderator.messages.FixedMessage} to base the {@link tc.oc.chatmoderator.words.WordSet} off of.
     */
    public WordSetFactory(final FixedMessage message, Pattern splitPattern) {
        this.message = Preconditions.checkNotNull(message);
        this.useFixed = true;
        this.splitPattern = Preconditions.checkNotNull(splitPattern);
    }

    public WordSetFactory(final FixedMessage message, Pattern splitPattern, boolean useFixed) {
        this.message = Preconditions.checkNotNull(message);
        this.useFixed = useFixed;
        this.splitPattern = Preconditions.checkNotNull(splitPattern);
    }

    public WordSetFactory(final FixedMessage message) {
        this.message = Preconditions.checkNotNull(message);
        this.useFixed = true;
        this.splitPattern = Pattern.compile("\\s");
    }

    /**
     * Creates a {@link tc.oc.chatmoderator.words.factories.WordSetFactory} with no defaults.
     *
     * @param message The {@link tc.oc.chatmoderator.messages.FixedMessage} to base the {@link tc.oc.chatmoderator.words.WordSet} off of.
     * @param useFixed Whether or not to use the fixed message from the message.
     */
    public WordSetFactory(final FixedMessage message, boolean useFixed) {
        this.message = Preconditions.checkNotNull(message);
        this.useFixed = useFixed;;
        this.splitPattern = Pattern.compile("\\s");
    }

    /**
     * Preforms all necessary logic to build the {@link tc.oc.chatmoderator.words.WordSet}.
     *
     * @return The current state of the factory.
     */
    public WordSetFactory build() {
        String base = useFixed ? message.getFixed() : message.getOriginal();

        for (String token : base.split(this.splitPattern.toString())) {
            wordSet.appendWord(new Word(token, false));
        }

        return this;
    }

    /**
     * Gets the {@link tc.oc.chatmoderator.words.WordSet}.
     * Typically called after {@code build()}.
     *
     * @return The {@link tc.oc.chatmoderator.words.WordSet}.
     */
    public WordSet getWordSet() {
        return this.wordSet;
    }

}
