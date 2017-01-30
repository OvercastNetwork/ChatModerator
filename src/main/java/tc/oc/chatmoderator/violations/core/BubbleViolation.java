package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.LinkedList;

public class BubbleViolation extends Violation {

    private LinkedList<Character> bubbles;

    public BubbleViolation(Instant time, Player player, FixedMessage message, double level, ZoneType zoneType, Event event, LinkedList<Character> bubbles) {
        super(time, player, message, level, true, zoneType, FixStyleApplicant.FixStyle.NONE, event);

        this.bubbles = Preconditions.checkNotNull(bubbles, "bubbles");
    }

    public ImmutableList<Character> getBubbles() {
        return ImmutableList.copyOf(this.bubbles);
    }
}
