package tc.oc.chatmoderator.words;

import com.google.common.base.Preconditions;

import java.util.List;

public class CorrectedWord extends Word {

    protected List<Word> components;

    public CorrectedWord(String word, List<Word> components) {
        super(word);

        this.components = Preconditions.checkNotNull(components);
    }

    public static WordSet replaceComponents(List<CorrectedWord> correctedWords, WordSet wordSet) {
        for (CorrectedWord correctedWord : correctedWords) {
            int startingIndex = wordSet.toList().indexOf(correctedWord.getComponents().get(0));

            for (Word word : correctedWord.getComponents()) {
                wordSet.toList().remove(word);
            }

            Word replacementWord = new Word(correctedWord.getWord());
            replacementWord.setChecked(true);

            if (startingIndex > -1) {
                wordSet.toList().add(startingIndex, replacementWord);
            }
        }

        return wordSet;
    }

    public List<Word> getComponents() {
        return this.components;
    }
}
