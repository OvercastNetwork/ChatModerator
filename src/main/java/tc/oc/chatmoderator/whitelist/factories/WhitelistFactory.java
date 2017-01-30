package tc.oc.chatmoderator.whitelist.factories;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.factories.ChatModeratorFactory;
import tc.oc.chatmoderator.whitelist.Whitelist;

import java.util.ArrayList;
import java.util.List;

public class WhitelistFactory implements ChatModeratorFactory {

    private ChatModeratorPlugin plugin;

    private String path;

    private final List<String> whitelist;

    public WhitelistFactory(ChatModeratorPlugin plugin, String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled!");

        this.plugin = Preconditions.checkNotNull(plugin);
        this.path = Preconditions.checkNotNull(path);

        this.whitelist = new ArrayList<>();
    }

    public WhitelistFactory build() {
        this.whitelist.addAll(plugin.getConfig().getStringList(path));

        if (plugin.isDebugEnabled()) {
            for (String s : this.whitelist) {
                plugin.getLogger().info("Added whitelisted string: " + s);
            }
        }

        return this;
    }

    public Whitelist getWhitelist() {
        return new Whitelist(this.whitelist);
    }
}
