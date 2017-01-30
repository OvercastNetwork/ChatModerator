package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Duration;
import org.joda.time.Instant;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.core.DuplicateMessageViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;

public class DuplicateMessageFilter extends Filter {

    /**
     * The delay in milliseconds between two messages before they are allowed to be sent.
     */
    private long delay;

    /**
     * The minimum delay in milliseconds between two messages where they will not be allowed to be sent if they are
     * the same message.  (i.e., "Hello, world!" (1 second later) "Hello, world!" -> blocked)
     */
    private long sameMessageDelay;

    public DuplicateMessageFilter(PlayerManager playerManager, String exemptPermission, long delay, long sameMessageDelay, int priority) {
        super(playerManager, exemptPermission, priority, FixStyleApplicant.FixStyle.NONE);

        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0...");
        Preconditions.checkArgument(sameMessageDelay > 0, "Same message delay must be greater than 0...");

        this.delay = delay;
        this.sameMessageDelay = sameMessageDelay;
    }

    /**
     * Filters a message to make sure that it was not sent too soon before the last message.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Override
    public @Nullable FixedMessage filter(FixedMessage message, Player player, ZoneType type, Event event) {
        if (player.hasPermission(this.getExemptPermission())) {
            return message;
        }

        PlayerViolationManager violationSet = this.getPlayerManager().getViolationSet(player);

        if (violationSet.getLastMessage() == null) {
            return message; // Let the overarching filter handle the setting of the new message
        }

        Instant now = Instant.now();
        Instant lastMessage = violationSet.getLastMessageTime();
        Duration difference = new Duration(lastMessage, now);

        boolean shouldAddViolation = false;

        if (lastMessage.withDurationAdded(this.delay, 1).isAfter(now)) {
            shouldAddViolation = true;
        } else if (lastMessage.withDurationAdded(this.sameMessageDelay, 1).isAfter(now) && this.isSameMessage(violationSet.getLastMessage(), message)) {
            shouldAddViolation = true;
        }

        if (shouldAddViolation) {
            DuplicateMessageViolation violation = new DuplicateMessageViolation(message.getTimeSent(), player, message, difference, type, event);
            violation.setForceNoSend(true);
            violationSet.addViolation(violation);
        }

        return message;
    }

    private boolean isSameMessage(FixedMessage lastMessage, FixedMessage currentMessage) {
        if (lastMessage != null && currentMessage != null) {
            return lastMessage.getOriginal().toLowerCase().equals(currentMessage.getOriginal().toLowerCase());
        } else {
            return false;
        }
    }
}
