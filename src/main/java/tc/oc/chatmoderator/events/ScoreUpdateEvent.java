package tc.oc.chatmoderator.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.base.Preconditions;

import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.violations.Violation;

public class ScoreUpdateEvent extends Event {

    private PlayerViolationManager violations;
    private double oldScore;
    private double newScore;
    private Violation cause;

    public ScoreUpdateEvent(PlayerViolationManager violations, double oldScore, Violation cause) {
        this.violations = Preconditions.checkNotNull(violations);
        this.oldScore = oldScore;
        this.newScore = this.violations.getScore();
        this.cause = cause;
    }

    public PlayerViolationManager getViolations() {
        return this.violations;
    }

    public double getOldScore() {
        return this.oldScore;
    }

    public double getNewScore() {
        return this.newScore;
    }

    public Violation getViolationCause() {
        return this.cause;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return ScoreUpdateEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return ScoreUpdateEvent.handlers;
    }
}
