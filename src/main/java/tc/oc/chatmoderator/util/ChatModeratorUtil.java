package tc.oc.chatmoderator.util;

import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple key-value store to map a short-name to the class representing a filter or violation.
 */
public class ChatModeratorUtil {

    /**
     * The key-value store for short-names to filter classes.
     */
    public static final Map<String, Class<? extends Filter>> filters = new HashMap<>();

    /**
     * The key-value store for short-names to violations.
     */
    public static final Map<String, Class<? extends Violation>> violations = new HashMap<>();

    /**
     * The key-value store for short-names to violations.
     */
    public static final Map<Class<? extends Filter>, String> permissions = new HashMap<>();

    /**
     * Sets up both the key-value stores.
     */
    static {
        filters.put("all-caps", AllCapsFilter.class);
        filters.put("duplicate-messages", DuplicateMessageFilter.class);
        filters.put("server-ip", IPFilter.class);
        filters.put("profanity", ProfanityFilter.class);
        filters.put("repeated-characters", RepeatedCharactersFilter.class);
        filters.put("leet-speak", LeetSpeakFilter.class);
        filters.put("bubbles", BubbleFilter.class);

        violations.put("all-caps", AllCapsViolation.class);
        violations.put("duplicate-messages", DuplicateMessageViolation.class);
        violations.put("server-ip", ServerIPViolation.class);
        violations.put("profanity", ProfanityViolation.class);
        violations.put("repeated-characters", RepeatedCharactersViolation.class);
        violations.put("leet-speak", LeetSpeakViolation.class);
        violations.put("bubbles", BubbleViolation.class);

        permissions.put(AllCapsFilter.class, "chatmoderator.filters.exempt.all-caps");
        permissions.put(DuplicateMessageFilter.class, "chatmoderator.filters.exempt.duplicate-messages");
        permissions.put(IPFilter.class, "chatmoderator.filters.exempt.server-ip");
        permissions.put(ProfanityFilter.class, "chatmoderator.filters.exempt.profanity");
        permissions.put(RepeatedCharactersFilter.class, "chatmoderator.filters.exempt.repeated-characters");
        permissions.put(LeetSpeakFilter.class, "chatmoderator.filters.exempt.leet-speak");
        permissions.put(BubbleFilter.class, "chatmoderator.filters.exempt.bubbles");
    }
}
