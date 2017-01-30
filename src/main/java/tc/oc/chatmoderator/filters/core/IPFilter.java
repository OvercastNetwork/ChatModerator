package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.WeightedFilter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.ServerIPViolation;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to check for IP addresses in messages.
 */
public class IPFilter extends WeightedFilter {

    private Whitelist whitelist;

    public IPFilter(PlayerManager playerManager, String exemptPermission, HashMap<Pattern, Double> weights, int priority, FixStyleApplicant.FixStyle fixStyle, Whitelist whitelist) {
        super(playerManager, exemptPermission, weights, priority, fixStyle);

        this.whitelist = whitelist;
    }

    /**
     * Filters a message. When this happens, violations are dispatched if necessary, and listeners of the violations can
     * modify the message. If the player is exempt (via permissions) from this filter, the message that was passed in is
     * returned instead of the filter processing it.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Nullable
    @Override
    public FixedMessage filter(FixedMessage message, final Player player, ZoneType type, Event event) {
        if (player.hasPermission(this.getExemptPermission())) {
            return message;
        }

        Matcher matcher = null;
        Set<String> ipAddresses = new HashSet<>();

        PlayerViolationManager violations = this.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "Player"));

        for (Pattern p : this.getWeights().keySet()) {
            matcher = p.matcher(Preconditions.checkNotNull(message.getFixed(), "fixed"));

            while (matcher.find()) {
                boolean skip = false;

                for (String s : this.whitelist.getWhitelist()) {
                    if (matcher.group().contains(s)) {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    ipAddresses.add(matcher.group());
                }
            }

            if (ipAddresses.size() > 0) {
                Violation violation = new ServerIPViolation(message.getTimeSent(), player, message, this.getWeightFor(p), ImmutableSet.copyOf(ipAddresses), type, FixStyleApplicant.FixStyle.NONE, event);
                violation.setForceNoSend(true);
                violations.addViolation(violation);
                ipAddresses.clear();
            }
            matcher = null;
        }

        return message;
    }
}
