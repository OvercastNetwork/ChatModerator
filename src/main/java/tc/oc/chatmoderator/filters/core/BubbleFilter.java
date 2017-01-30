package tc.oc.chatmoderator.filters.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.BubbleViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class BubbleFilter extends Filter {

    private static final char[] bubbleChars = new char[52];
    public static final int BUBBLE_CONSTANT = 9398;

    static {
        for (int i = 0; i < bubbleChars.length; i++) {
            char c = (char) (i + BUBBLE_CONSTANT);
            bubbleChars[i] = c;
        }
    }

    public BubbleFilter(PlayerManager playerManager, String permission, int priority) {
        super(playerManager, permission, priority, FixStyleApplicant.FixStyle.NONE);
    }

    @Nullable
    @Override
    public FixedMessage filter(FixedMessage message, Player player, ZoneType type, Event event) {
        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);

        Instant now = Instant.now();
        LinkedList<Character> chars = new LinkedList<>();

        StringBuilder fixed = new StringBuilder();

        for (int i = 0; i < message.getOriginal().toCharArray().length; i++) {
            char c = message.getOriginal().toCharArray()[i];

            boolean isBubble = false;

            for (char containingChar : bubbleChars) {
                if (c == containingChar) {
                    isBubble = true;
                    fixed.append(bubbleToAlphabetic(containingChar));
                    chars.add(containingChar);
                    break;
                }
            }

            if (!isBubble) {
                fixed.append(c);
            }
        }

        message.setFixed(fixed.toString());

        if (chars.size() > 0) {
            Violation violation = new BubbleViolation(now, player, message, 0, type, event, chars);
            this.playerManager.getViolationSet(player).addViolation(violation);
        }

        return message;
    }

    private static char bubbleToAlphabetic(char in) {
        char c = (char) ((int) in - BUBBLE_CONSTANT + 97 - 26);
        return c;
    }
}
