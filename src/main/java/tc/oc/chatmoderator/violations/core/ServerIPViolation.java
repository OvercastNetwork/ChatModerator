package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.joda.time.Instant;

import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Added when a player posts a server IP in chat.
 */
public class ServerIPViolation extends Violation {
    private Set<String> ipAddresses = new HashSet<>();

    /**
     * Publicly instantiable variant of ServerIPViolation.
     *
     * @param time The time that the violation occurred.
     * @param player The player that sent the offending message.
     * @param message The offending message that was sent.
     * @param level The level that the violation carries.
     * @param ipAddresses The selection of {@link java.net.InetAddress} that didn't pass the {@link tc.oc.chatmoderator.filters.core.RepeatedCharactersFilter}
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} where the violation occurred.
     */
    public ServerIPViolation(final Instant time, final Player player, final FixedMessage message, final double level, final Set<String> ipAddresses, ZoneType zoneType, FixStyleApplicant.FixStyle fixStyle, Event event) {
        super(time, player, message, level, true, zoneType, fixStyle, event);

        for (String ipAddress : Preconditions.checkNotNull(ipAddresses, "IP addresses")) {
            this.ipAddresses.add(Preconditions.checkNotNull(ipAddress, "IP address"));
        }
    }

    /**
     * Gets the sent IP addresses.
     *
     * @return The sent IP addresses.
     */
    public Set<String> getIPAddresses() {
        return Collections.unmodifiableSet(this.ipAddresses);
    }
    
    /**
     * Sets the sent IP addresses.
     * 
     * @param ipAddresses The sent IP addresses
     */
    protected void setIPAddresses(Set<String> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }
}
