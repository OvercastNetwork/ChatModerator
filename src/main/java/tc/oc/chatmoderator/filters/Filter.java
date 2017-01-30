package tc.oc.chatmoderator.filters;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;

/**
 * Represents a filter that filters chat messages, dispatching violations if necessary.
 */
public abstract class Filter implements Comparable<Filter> {
    protected final PlayerManager playerManager;
    protected final String exemptPermission;
    protected final int priority;
    protected final FixStyleApplicant.FixStyle defaultFixStyle;

    protected Filter(final PlayerManager playerManager, final String exemptPermission, int priority, final FixStyleApplicant.FixStyle defaultFixStyle) {
        this.playerManager = Preconditions.checkNotNull(playerManager);
        this.exemptPermission = Preconditions.checkNotNull(exemptPermission, "Exempt permission");
        this.defaultFixStyle = Preconditions.checkNotNull(defaultFixStyle, "Default fix style");
        Preconditions.checkArgument(priority >= 0, "Priority must be greater than or equal to zero!");

        this.priority = priority;
    }

    /**
     * Gets the permission that allows players to be exempt from the filter
     * 
     * @return The permission
     */
    public String getExemptPermission() {
        return this.exemptPermission;
    }

    protected PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    protected FixStyleApplicant.FixStyle getDefaultFixStyle() {
        return this.defaultFixStyle;
    }

    /**
     * Gets the priority for the filter (the order in which the filter should be run, lower = sooner.
     *
     * @return The priority.
     */
    public int getPriority() {
        return this.priority;
    }

    public int compareTo(Filter f1) {
        if(this.getPriority() > f1.getPriority()) {
            return 1;
        } else if(this.getPriority() == f1.getPriority()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Filters a message. When this happens, violations are dispatched if necessary, and listeners of the violations can
     * modify the message.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Nullable
    public abstract FixedMessage filter(FixedMessage message, final Player player, ZoneType type, Event event);

}
