package tc.oc.chatmoderator.factories.core;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.factories.ChatModeratorFactory;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.util.ChatModeratorUtil;
import tc.oc.chatmoderator.zones.Zone;

import java.util.ArrayList;

/**
 * Factory class for parsing the configuration for zones
 */
public class ZoneFactory implements ChatModeratorFactory {

    /**
     * The base ChatModerator plugin.
     */
    private final ChatModeratorPlugin plugin;

    /**
     * The path for the zone to parse on.
     */
    private final String path;

    /**
     * The zone that we will return.
     */
    private Zone zone;

    /**
     * A list of excluded filters for this zone.
     */
    private ArrayList<Class<? extends Filter>> excludedFilters;

    /**
     * Whether or not that this zone is enabled.
     */
    private boolean enabled;

    /**
     * Insatiable ZoneFactory constructor.
     *
     * @param plugin The plugin to parse from.
     * @param path The path for the parser to parse on.
     */
    public ZoneFactory(final ChatModeratorPlugin plugin, final String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled!");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.path = Preconditions.checkNotNull(path, "path");

        this.excludedFilters = new ArrayList<>();

        this.zone = new Zone(excludedFilters, enabled);
    }

    /**
     * Actually parse the zone that we were supposed to parse.
     *
     * @return The state of this factory class.
     */
    public ZoneFactory build() {
        enabled = plugin.getConfig().getBoolean(this.path + ".enabled");

        for(String s : plugin.getConfig().getStringList(this.path + ".excluded-filters")) {
            excludedFilters.add(ChatModeratorUtil.filters.get(s.toLowerCase()));
        }

        this.zone.setEnabled(enabled);

        return this;
    }

    /**
     * Gets the built zone.  Typically called after parsing.
     *
     * @return The zone.
     */
    public Zone getZone() {
        return this.zone;
    }

    /**
     * Gets the path to parse on.
     *
     * @return The path.
     */
    public String getPath() {
        return this.path;
    }
}
