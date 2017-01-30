package tc.oc.chatmoderator.factories.core;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.factories.ChatModeratorFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class that serves the purpose to parse and set-up configuration sections for filters.
 */
public class WeightedFilterFactory implements ChatModeratorFactory {

    /**
     * The base plugin.
     */
    private final ChatModeratorPlugin plugin;

    /**
     * The path to search for in the config
     */
    private final String path;

    /**
     * Maps the a Pattern for a WeightedFilter to a weight.
     */
    private HashMap<Pattern, Double> weights;

    /**
     * Publicly instantiable variant of the WeightedFilterFactory.
     *
     * @param plugin The base plugin.
     * @param path The path to search in for parameters.
     */
    public WeightedFilterFactory(final ChatModeratorPlugin plugin, final String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled");

        this.plugin = plugin;
        this.path = Preconditions.checkNotNull(path, "path");

        this.weights = new HashMap<>();
    }

    /**
     * Parses the configuration and lets debuggers know of its findings.
     *
     * @return The current state of the WeightedFilterFactory object.
     */
    @SuppressWarnings("unchecked")
    public WeightedFilterFactory build() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);

        for (Object o : plugin.getConfig().getList(path)) {
            Map<String, Object> map = (Map<String, Object>) o;

            Pattern pattern = Pattern.compile((String) map.get("regex"));
            Double value = (Double) map.get("level");

            if(plugin.isDebugEnabled()) {
                plugin.getLogger().info("Added weighted expression: " + pattern.pattern() + " -- " + value);
            }

            weights.put(pattern, value);
        }

        return this;
    }

    /**
     * Gets the path to search in.
     *
     * @return The path.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Gets the key-value store of patterns to weights.
     *
     * @return The weights.
     */
    public HashMap<Pattern, Double> getWeights() {
        return this.weights;
    }

}
