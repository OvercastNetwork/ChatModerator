package tc.oc.chatmoderator.listeners.managers;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.zones.Zone;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ZoneManager {

    private final ChatModeratorPlugin plugin;
    private Map<ZoneType, Zone> zones = new HashMap<>();

    public ZoneManager(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(plugin.isEnabled(), "plugin not enabled");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
    }

    /**
     * Registers a zone.
     *
     * @param type The type of zone being registered.
     * @param zone The zone associated with that type.
     */
    public void registerZone(final ZoneType type, final Zone zone) {
        if (this.zones.containsKey(type)) {
            throw new IllegalArgumentException("ChatModeratorListener already contains this zone!");
        }

        this.zones.put(type, zone);
        plugin.getLogger().info("Registered zone: " + type.name());
    }

    /**
     * Removes a single zone for a specific type.
     *
     * @param type The type of zone being removed.
     */
    public void unRegisterZone(final ZoneType type) {
        this.zones.remove(type);
        plugin.getLogger().info("Unregistered zone: " + type.name());
    }

    /**
     * Un-registers all zones.
     */
    public void unRegisterAllZones() {
        for(Iterator<ZoneType> it = this.zones.keySet().iterator(); it.hasNext(); ) {
            ZoneType type = it.next();

            plugin.getLogger().info("Unregistered zone: " + type.name());
            it.remove();
        }
    }

    /**
     * Gets the zone of a specific type.
     *
     * @param type The type to query on.
     * @return The zone for that type.
     */
    public Zone getZone(ZoneType type) {
        return this.zones.get(type);
    }
}
