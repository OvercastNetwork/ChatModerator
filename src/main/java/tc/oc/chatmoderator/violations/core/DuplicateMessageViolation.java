package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Duration;
import org.joda.time.Instant;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

/**
 * Violation called when a too many messages are being sent too fast.
 */
public class DuplicateMessageViolation extends Violation {

    /**
     * The time since the last message.
     */
    private final Duration timeSinceLast;

    /**
     * Instantiates a DuplicateMessageViolation.
     *
     * @param time The time that the violation occurred.
     * @param player The player that sent the message.
     * @param message The message that was sent.
     * @param timeSinceLast The instant since the last message.
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} in which the violation occurred.
     */
    public DuplicateMessageViolation(Instant time, Player player, FixedMessage message, Duration timeSinceLast, ZoneType zoneType, Event event) {
        super(time, player, message, 1, true, zoneType, FixStyleApplicant.FixStyle.NONE, event);

        this.timeSinceLast = Preconditions.checkNotNull(timeSinceLast);
    }

    /**
     * Gets the difference between the message and the last message that was sent by the player.
     *
     * @return The duration of time since the last message was sent.
     */
    public Duration getTimeSinceLast() {
        return this.timeSinceLast;
    }

}
