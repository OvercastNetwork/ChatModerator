package tc.oc.chatmoderator.util;

import tc.oc.chatmoderator.words.CorrectedWord;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling {@link tc.oc.chatmoderator.messages.FixedMessage}.
 */
public final class MessageUtils {

    public static List<CorrectedWord> splitWithWeights(Map<Pattern, Double> weights, List<Word> messageWords) {
        List<CorrectedWord> correctedWords = new ArrayList<>();
        Map<Pattern, Double> perWordWeights = MessageUtils.makeWeightsPerWord(weights);

        for (int i = 0; i < messageWords.size(); i++) {
            for (int j = messageWords.size(); j >= 0; j--) {
                if (i >= j) continue;

                List<Word> subWordList = messageWords.subList(i, j);

                StringBuilder builder = new StringBuilder();
                for (Word subWord : subWordList) {
                    builder.append(subWord.getWord());
                }

                for (Pattern p : perWordWeights.keySet()) {
                    Matcher matcher = p.matcher(builder.toString());

                    if (matcher.matches()) {
                        ArrayList<Word> subWordListCopy = new ArrayList<>(subWordList.size());
                        for (Word w : subWordList) {
                            subWordListCopy.add(w);
                        }

                        correctedWords.add(new CorrectedWord(builder.toString(), subWordListCopy));
                    }
                }
            }
        }

        return correctedWords;
    }

    private static Map<Pattern, Double> makeWeightsPerWord(Map<Pattern, Double> weights) {
        Map<Pattern, Double> wordSpecificWeights = new HashMap<>();

        for (Pattern pattern : weights.keySet()) {
            Pattern wordSpecificPattern = PatternUtils.makePatternWordSpecific(pattern);

            wordSpecificWeights.put(wordSpecificPattern, weights.get(pattern));
        }

        return wordSpecificWeights;
    }

    public static WordSet evaluate(List<CorrectedWord> correctedWords, WordSet wordSet) {
        return CorrectedWord.replaceComponents(correctedWords, wordSet);
    }


}