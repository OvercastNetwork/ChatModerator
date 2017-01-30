package tc.oc.chatmoderator.words;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of words.
 */
public class WordSet {

    /**
     * The actual set of words.
     */
    private List<Word> words;

    /**
     * Creates a new {@link tc.oc.chatmoderator.words.WordSet} with no words added.
     */
    public WordSet() {
        this.words = new ArrayList<>();
    }

    /**
     * Creates a new {@link tc.oc.chatmoderator.words.WordSet} with the given words.
     *
     * @param words The words.
     */
    public WordSet(List<Word> words) {
        this.words = Preconditions.checkNotNull(words);
    }

    /**
     * Appends the given word to the end of the list.
     *
     * @param word The word to append.
     */
    public void appendWord(Word word) {
        this.words.add(word);
    }

    /**
     * Gets the first unchecked word in the set.
     *
     * @return The first unchecked word.
     */
    public Word getFirstUncheckedWord() {
        for (Word word : this.words) {
            if (!(word.isChecked())) {
                return word;
            }
        }

        return null;
    }

    /**
     * Gets whether all of the words in the set have been checked.
     * If this is called and returns {@code true}, it is likely OK to move onto the next filter.
     *
     * @return Whether or not all of the words are checked.
     */
    public boolean areAllChecked() {
        for (Word word : this.words) {
            if (!(word.isChecked())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets all the words as a {@link java.util.List}.
     *
     * @return The words as a {@link java.util.List}.
     */
    public List<Word> toList() {
        return this.words;
    }

    /**
     * Gets the {@link java.lang.String} representation of word.
     *
     * @return All words separated by a space.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Word word : this.words) {
            builder.append(word.getWord()).append(' ');
        }

        return builder.toString().trim();
    }
}
