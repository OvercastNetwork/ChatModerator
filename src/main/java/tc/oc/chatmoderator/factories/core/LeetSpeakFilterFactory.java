package tc.oc.chatmoderator.factories.core;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.factories.ChatModeratorFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Factory class for building the {@link tc.oc.chatmoderator.filters.core.LeetSpeakFilter}
 */
public class LeetSpeakFilterFactory implements ChatModeratorFactory {

    /**
     * The base plugin, used for accessing configuration.
     */
    private ChatModeratorPlugin plugin;

    /**
     * The key-value store of the characters of the alphabet to their possible "leet" alternatives.
     */
    private Map<Character, List<Pattern>> dictionary;

    /**
     * The path to search on in the dictionary.
     */
    private String path;

    /**
     * The file to use as reference when searching in the dictionary.
     */
    private File dictionaryFile;

    /**
     * Available constructor for creating the LeetSpeakFilterFactory object.
     *
     * @param plugin The base plugin.
     * @param path The path to search on in the config.
     * @param file The dictionary file to use as reference.
     */
    public LeetSpeakFilterFactory(ChatModeratorPlugin plugin, File file, String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled!");
        Preconditions.checkArgument(file.exists(), "Dictionary file does not exist!");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.path = Preconditions.checkNotNull(path);
        this.dictionaryFile = file;

        this.dictionary = new HashMap<>();
    }

    /**
     * Parses the config specified in the constructor.
     *
     * @return The state of the {@link LeetSpeakFilterFactory} object.
     */
    public LeetSpeakFilterFactory build() {
        ConfigurationSection dictionarySection = new YamlConfiguration().loadConfiguration(dictionaryFile).getConfigurationSection(path);

        for(Map.Entry<String, Object> entry : dictionarySection.getValues(false).entrySet()) {
            Character reference = null;

            try {
                reference = entry.getKey().charAt(0);
            } catch (NullPointerException e) {
                plugin.getLogger().severe("Leet-speak entry " + entry.getKey() + " was not able to be parsed.");
                e.printStackTrace();
                continue;
            }

            ArrayList<Pattern> translations = new ArrayList<>();

            for (String s : (ArrayList<String>) entry.getValue()) {
                try {
                    translations.add(Pattern.compile(Pattern.quote(s)));
                } catch (PatternSyntaxException e) {
                    plugin.getLogger().info("Error parsing: " + reference.toString() + " - " + s);
                    e.printStackTrace();
                }
            }

            if (plugin.isDebugEnabled()) {
                plugin.getLogger().info(reference.toString() + " -- " + translations.toString());
            }

            dictionary.put(reference, translations);
        }

        return this;
    }

    /**
     * Gets the dictionary associated with this filter.  Typically called after {@code build()}.
     * @return The dictionary
     */
    public Map<Character, List<Pattern>> getDictionary() {
        return this.dictionary;
    }

    /**
     * Gets the path to search on.
     *
     * @return The path.
     */
    private String getPath() {
        return this.path;
    }
}
