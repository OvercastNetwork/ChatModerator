package tc.oc.chatmoderator.messages;

import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.words.factories.WordSetFactory;

import java.util.regex.Pattern;

public class FixedWordMessage extends FixedMessage {

    private WordSet originalWordSet;
    private WordSet fixedWordSet;

    public FixedWordMessage(FixedMessage message, Pattern splitPattern) {
        super(message.getOriginal(), message.getTimeSent());

        this.originalWordSet = new WordSetFactory(message, splitPattern, false).build().getWordSet();
        this.fixedWordSet = new WordSetFactory(message, splitPattern, true).build().getWordSet();
    }

    public WordSet getOriginalWordSet() {
        return this.originalWordSet;
    }

    public WordSet getFixedWordSet() {
        return this.fixedWordSet;
    }
}
