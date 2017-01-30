package tc.oc.chatmoderator.words;

import com.google.common.base.Preconditions;

/**
 * Represents a single word, perhaps part of a larger sentence.
 */
public class Word {

    /**
     * The actual word in a String representation.
     */
    private String word;

    /**
     * Whether that word has been evaluated by any given {@link tc.oc.chatmoderator.filters.Filter}.
     */
    private boolean checked;

    /**
     * Instantiates a word, defaults the checked state to false.
     *
     * @param word The word.
     */
    public Word(String word) {
        this.word = Preconditions.checkNotNull(word);

        this.checked = false;
    }

    /**
     * Instantiate a word.
     *
     * @param word The word.
     * @param checked Whether that word has been already checked (usually false).
     */
    public Word(String word, boolean checked) {
        this.word = Preconditions.checkNotNull(word);

        this.checked = checked;
    }

    /**
     * Gets whether this word has been checked.
     *
     * @return The state of the word.
     */
    public boolean isChecked() {
        return this.checked;
    }

    /**
     * Gets the word as a String representation.
     *
     * @return The word as a String.
     */
    public String getWord() {
        return this.word;
    }

    /**
     * Sets the word to a new word.  Typically called when fixing a word.
     *
     * @param word The word to set to.
     */
    public void setWord(String word) {
        this.word = Preconditions.checkNotNull(word);
    }

    /**
     * Marks the word as having been checked.
     *
     * @param checked The new checked state.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
