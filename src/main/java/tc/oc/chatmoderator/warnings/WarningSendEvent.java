package tc.oc.chatmoderator.warnings;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import tc.oc.chatmoderator.events.ViolationAddEvent;

import com.google.common.base.Preconditions;

public class WarningSendEvent extends Event {

    private @Nullable String message;
    private final Player player;
    private final ViolationAddEvent parent;

    public WarningSendEvent(final Player player, ViolationAddEvent parent) {
        this(player, parent, null);
    }

    public WarningSendEvent(final Player player, ViolationAddEvent parent, String message) {
        this.player = Preconditions.checkNotNull(player, "player cannot be null");
        this.parent = parent;
        this.message = message;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public ViolationAddEvent getParent() {
        return this.parent;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return WarningSendEvent.handlers;
    }
}
