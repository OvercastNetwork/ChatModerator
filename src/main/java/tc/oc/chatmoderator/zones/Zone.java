package tc.oc.chatmoderator.zones;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.filters.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the base-class for a Zone.
 */
public class Zone {

    /**
     * The list of Filters to nor run on a specific zone.
     */
    private List<Class<? extends Filter>> excludedFilters;

    /**
     * Whether this zone is enabled or not.
     */
    private boolean enabled;

    /**
     * Available constructor for a Zone.
     *
     * @param excludedFilters The filters to not run on a zone.
     * @param enabled Whether or not the zone is enabled.
     */
    public Zone(ArrayList<Class<? extends Filter>> excludedFilters, boolean enabled) {
        this.excludedFilters = Preconditions.checkNotNull(excludedFilters, "filters");
        this.enabled = enabled;
    }

    /**
     * Gets the excluded filters on a zone.
     *
     * @return The excluded filters.
     */
    public List<Class<? extends Filter>> getExcludedFilters() {
        return excludedFilters;
    }

    /**
     * Gets whether or not this zone is enabled.
     *
     * @return If the zone is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of a zone.
     *
     * @param enabled The state to set it to.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
