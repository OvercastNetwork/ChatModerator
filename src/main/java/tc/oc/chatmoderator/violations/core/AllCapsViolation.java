package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.Set;

/**
 * Violation called when a player writes a word or set of words in all-caps.
 */
public class AllCapsViolation extends Violation {

    private final ImmutableSet<String> upperCaseWords;

    /**
     * Publicly insatiable variant of the AllCapsViolation.
     *
     * @param time The time when the violation happened.
     * @param player The player that sent the message.
     * @param message The message that was sent.
     * @param upperCaseWords The Set of words that was in all uppercase.
     * @param level The severity of the violation.
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} in which the violation occurred.
     */
    public AllCapsViolation(Instant time, Player player, FixedMessage message, double level, Set<String> upperCaseWords, ZoneType zoneType, Event event) {
        super(time, player, message, level, true, zoneType, FixStyleApplicant.FixStyle.NONE, event);

        this.upperCaseWords = ImmutableSet.copyOf(Preconditions.checkNotNull(upperCaseWords));
    }

    /**
     * Gets the words that were uppercase.
     *
     * @return The uppercase words.
     */
    public Set<String> getUpperCaseWords() {
        return this.upperCaseWords;
    }
}
