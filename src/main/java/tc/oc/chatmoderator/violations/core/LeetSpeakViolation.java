package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.ArrayList;
import java.util.List;

/**
 * Violation called when a user is speaking in "leet-speak".
 */
public class LeetSpeakViolation extends Violation {

    private List<String> words;

    /**
     * Public instantiable variant of the LeetSpeakViolation
     *
     * @param time The Instant that the message was sent at.
     * @param player The player that sent the offending message.
     * @param message The offending message that was sent.
     * @param level The severity of the violation.
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} where the violation occurred.
     */
    public LeetSpeakViolation(Instant time, Player player, FixedMessage message, double level, ArrayList<String> words, ZoneType zoneType, Event event) {
        super(time, player, message, level, true, zoneType, FixStyleApplicant.FixStyle.NONE, event);

        this.words = Preconditions.checkNotNull(words);
    }

    /**
     * Gets the words that were "leet" in nature.
     *
     * @return The l33t words.
     */
    public List<String> getWords() {
        return this.words;
    }
}
