package tc.oc.chatmoderator.warnings;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.events.ViolationAddEvent;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

public class ViolationPreWarningListener implements Listener {

    private final @Nonnull PlayerManager manager;
    public static final List<Class> WARNABLE_VIOLATIONS = new ArrayList<>();

    static {
        WARNABLE_VIOLATIONS.add(AllCapsViolation.class);
        WARNABLE_VIOLATIONS.add(DuplicateMessageViolation.class);
        WARNABLE_VIOLATIONS.add(ProfanityViolation.class);
        WARNABLE_VIOLATIONS.add(ServerIPViolation.class);
    }

    public ViolationPreWarningListener(@Nonnull PlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerRecieveViolation(ViolationAddEvent event) {
        Player player = event.getPlayer();
        WarningSendEvent warningEvent = new WarningSendEvent(player, event);
        Bukkit.getServer().getPluginManager().callEvent(warningEvent);

        PlayerViolationManager violations = this.manager.getViolationSet(event.getPlayer());

        if (!violations.isWarned() && warningEvent.getMessage() != null) {
            violations.setWarned(true);
            violations.sendWarning(warningEvent.getMessage());
        }
    }

    @EventHandler
    public void handleWarningMessage(WarningSendEvent event) {
        Violation violation = event.getParent().getViolation();

        if (WARNABLE_VIOLATIONS.contains(violation.getClass())) {
            StringBuilder message = new StringBuilder(ChatColor.RED.toString());
            if (violation.getClass() == DuplicateMessageViolation.class) {
                message.append("This message was not sent because you are sending the same message too fast.");
            } else {
                message.append("This message was not sent to ")
                       .append(violation.isForceNoSend() ? "any" : "some")
                       .append(" players because it contained potentially offensive content.");
            }

            event.setMessage(message.toString());
        } else {
            event.setMessage(null);
        }
    }
}
