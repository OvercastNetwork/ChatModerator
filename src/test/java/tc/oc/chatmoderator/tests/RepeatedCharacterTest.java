package tc.oc.chatmoderator.tests;

import junit.framework.TestCase;
import org.junit.Test;
import tc.oc.chatmoderator.filters.core.RepeatedCharactersFilter;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Test for {@link tc.oc.chatmoderator.filters.core.RepeatedCharactersFilter}
 */
public class RepeatedCharacterTest extends TestCase {

    /**
     * The number of iterations to the run the test on.
     */
    private static final int TEST_ITERATIONS = 5;

    /**
     * The minimum amount of characters to generate a Pattern for.
     */
    private static final int MIN_CHARACTERS = 2;

    /**
     * The unique number of characters for each word in the sentence.
     */
    private static final int UNIQUE_CHARACTERS_PER_WORD = 3;

    /**
     * The number of occurrences that that character should be generated
     */
    private static final int OCCURRENCES_PER_CHARACTER = 3;

    /**
     * The number of words per sentence.
     */
    private static final int WORDS_PER_SENTENCE = 5;

    private Random random;
    private StringBuilder builder;

    @Override
    public void setUp() {
        this.random = new Random();
        this.builder = new StringBuilder();
    }

    @Test
    public void testRepeatedCharactersFilter() throws Exception {
        Pattern pattern = RepeatedCharactersFilter.generatePattern(RepeatedCharacterTest.MIN_CHARACTERS);

        for (int i = 0; i < RepeatedCharacterTest.TEST_ITERATIONS; i++) {
            for (int j = 0; j < WORDS_PER_SENTENCE; j++) {
                for (int k = 0; k < UNIQUE_CHARACTERS_PER_WORD; k++) {
                    char c = (char) (random.nextInt(26) + 'a');

                    for (int l = 0; l < OCCURRENCES_PER_CHARACTER; l++) {
                        builder.append(c);
                    }
                }

                builder.append(j == WORDS_PER_SENTENCE - 1 ? "" : " ");
            }

            String errorMessage = "testRepeatedCharactersFilter() failed with [" + builder.toString() + "] and pattern [" + pattern.toString() + "]";
            assertTrue(errorMessage, pattern.matcher(builder.toString()).find());
        }
    }

}
