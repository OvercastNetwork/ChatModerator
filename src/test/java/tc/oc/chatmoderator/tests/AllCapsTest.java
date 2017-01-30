package tc.oc.chatmoderator.tests;

import junit.framework.TestCase;
import org.junit.Test;
import tc.oc.chatmoderator.filters.core.AllCapsFilter;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Test for {@link tc.oc.chatmoderator.filters.core.AllCapsFilter}
 */
public class AllCapsTest extends TestCase {

    /**
     * Number of iterations to run the test on
     */
    private static final int TEST_ITERATIONS = 5;

    /**
     * Minimum characters for each character
     * (i.e., 5 and 'A' will result AAAAA)
     */
    private static final int MIN_CHARACTERS_PER_CHARACTER = 2;

    /**
     * The unique number of characters per word
     */
    private static final int UNIQUE_CHARACTERS_PER_WORD = 3;

    /**
     * The number of words per a sentence
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
    public void testAllCapsFilter() throws Exception {
        Pattern pattern = AllCapsFilter.getBasePattern(2);

        for (int i = 0; i < TEST_ITERATIONS; i++) {
            for (int j = 0; j < WORDS_PER_SENTENCE; j++) {
                for (int k = 0; k < UNIQUE_CHARACTERS_PER_WORD; k++) {
                    for (int l = 0; l < MIN_CHARACTERS_PER_CHARACTER; l++) {
                        char c = (char) (random.nextInt(26) + 'A');

                        this.builder.append(c);
                    }
                }

                builder.append(j == WORDS_PER_SENTENCE - 1 ? "" : " ");
            }

            String errorMessage = "testAllCapsFilter() failed with [" + builder.toString() + "] and pattern [" + pattern.toString() + "]";
            assertTrue(errorMessage, pattern.matcher(builder.toString()).find());
        }
    }

}
