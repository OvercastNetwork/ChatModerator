package tc.oc.chatmoderator.violations;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.zones.ZoneType;

/**
 * Represents an individual violation.
 */
public abstract class Violation {
    protected final Instant time;
    protected final Player player;
    protected double level;
    protected final FixedMessage message;
    protected boolean cancelled = false;
    protected boolean fixed;
    protected final ZoneType zoneType;
    protected FixStyleApplicant.FixStyle fixStyle;
    protected final Event event;
    protected boolean forceNoSend;

    protected Violation(final Instant time, final Player player, final FixedMessage message, double level, boolean fixed, final ZoneType zoneType, final FixStyleApplicant.FixStyle fixStyle, Event event) {
        this(time, player,message, level, fixed, zoneType, fixStyle, event, false);
    }

    /**
     * For subclasses only.
     */
    protected Violation(final Instant time, final Player player, final FixedMessage message, double level, boolean fixed, final ZoneType zoneType, final FixStyleApplicant.FixStyle fixStyle, Event event, boolean forceNoSend) {
        this.time = Preconditions.checkNotNull(time, "Time");
        this.player = Preconditions.checkNotNull(player, "Player");
        this.message = Preconditions.checkNotNull(message, "Message");
        this.level = level;
        this.fixed = fixed;
        this.zoneType = Preconditions.checkNotNull(zoneType, "Zone type");
        this.fixStyle = fixStyle;
        this.event = Preconditions.checkNotNull(event, "event");
        this.forceNoSend = forceNoSend;
    }

    /**
     * Gets the time at which the message was sent.
     *
     * @return The time at which the message was sent.
     */
    public final Instant getTime() {
        return this.time;
    }

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public final FixedMessage getMessage() {
        return this.message;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Whether or not the violating chat message has been cancelled.
     *
     * @return Whether or not the violating chat message has been cancelled.
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether or not the violating chat message should be cancelled.
     *
     * @param cancelled Whether or not the violating chat message should be cancelled.
     */
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the {@link tc.oc.chatmoderator.zones.ZoneType} that the violation occurred in.
     *
     * @return The zone-type.
     */
    public final ZoneType getZoneType() {
        return this.zoneType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Violation{");
        sb.append("time=").append(time);
        sb.append(", player=").append(player);
        sb.append(", level=").append(level);
        sb.append(", message='").append(message).append('\'');
        sb.append(", cancelled=").append(cancelled);
        sb.append(", zone-type=").append(zoneType.name());
        sb.append(", type=").append(this.getClass().getSimpleName());
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets whether or not the violating chat message has been fixed.
     *
     * @return Whether or not the violating chat message has been fixed.
     */
    public boolean isFixed() {
        return this.fixed;
    }

    /**
     * Sets whether or not the violating chat message should be fixed.
     *
     * @param fixed Whether or not the violating chat message should be fixed.
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    /**
     * Gets the violation level.
     *
     * @return The violation level.
     */
    public double getLevel() {
        return this.level;
    }

    /**
     * Sets the violation level.
     *
     * @param level The level to set.
     */
    public void setLevel(double level) {
        this.level = level;
    }

    public FixStyleApplicant.FixStyle getFixStyle() {
        return this.fixStyle;
    }

    public void setFixStyle(FixStyleApplicant.FixStyle fixStyle) {
        this.fixStyle = Preconditions.checkNotNull(fixStyle);
    }

    /**
     * Gets the hash code from the original event.
     *
     * @return The hash code.
     */
    public final Event getEvent() {
        return this.event;
    }

    public final boolean isForceNoSend() {
        return this.forceNoSend;
    }

    public final void setForceNoSend(boolean forceNoSend) {
        this.forceNoSend = forceNoSend;
    }
}
