package tc.oc.chatmoderator.listeners.managers;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.zones.Zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FilterManager {

    private final ChatModeratorPlugin plugin;
    private List<Filter> filters = new ArrayList<>();

    public FilterManager(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(plugin.isEnabled(), "plugin not enabled");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
    }

    /**
     * Gets all the filters.
     *
     * @return All the filters.
     */
    public List<Filter> getAllFilters() {
        return this.filters;
    }

    /**
     * Registers a filter. <b>Messages will be filtered in the order that they are registered in.</b>
     *
     * @param filter The filter to be registered.
     */
    public void registerFilter(final Filter filter) {
        this.filters.add(Preconditions.checkNotNull(filter, "Filter"));
        plugin.getLogger().info("Registered filter: " + filter.getClass().getSimpleName());

        Collections.sort(this.filters);
    }

    /**
     * Un-registers the specified filter.
     *
     * @param filter The filter to be unregistered.
     */
    public void unRegisterFilter(final Filter filter) {
        this.filters.remove(Preconditions.checkNotNull(filter, "Filter"));
        plugin.getLogger().info("Unregistered filter: " + filter.getClass().getSimpleName());
    }

    /**
     * Un-registers all filters.
     */
    public void unRegisterAllFilters() {
        for(Iterator<Filter> it = filters.iterator(); it.hasNext(); ) {
            Filter f = it.next();

            plugin.getLogger().info("Unregistered filter: " + f.getClass().getSimpleName());
            it.remove();
        }
    }

    /**
     * Un-registers all filters of the specified type.
     *
     * @param type The type of filter to be unregistered.
     */
    public void unRegisterFilters(final Class<? extends Filter> type) {
        Preconditions.checkNotNull(type, "Type");
        for(Iterator<Filter> it = filters.iterator(); it.hasNext(); ) {
            Filter f = it.next();

            if(f.getClass().equals(type)) {
                plugin.getLogger().info("Unregistered filter: " + f.getClass().getSimpleName());
                it.remove();
            }
        }
    }

    public List<Filter> getFiltersForZone(Zone zone) {
        List<Filter> filters = new ArrayList<>();

        for (Filter filter : this.filters) {
            if (!(zone.getExcludedFilters().contains(filter.getClass()))) {
                filters.add(filter);
            }
        }

        return filters;
    }


}
