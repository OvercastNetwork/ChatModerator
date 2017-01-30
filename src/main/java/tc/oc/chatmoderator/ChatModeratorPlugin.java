package tc.oc.chatmoderator;

import com.github.rmsy.channels.ChannelsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import tc.oc.chatmoderator.channels.SimpleFilteredChannel;
import tc.oc.chatmoderator.factories.core.LeetSpeakFilterFactory;
import tc.oc.chatmoderator.factories.core.TemplateFactory;
import tc.oc.chatmoderator.factories.core.WeightedFilterFactory;
import tc.oc.chatmoderator.factories.core.ZoneFactory;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.listeners.ChatModeratorListener;
import tc.oc.chatmoderator.listeners.DebugListener;
import tc.oc.chatmoderator.settings.Settings;
import tc.oc.chatmoderator.scores.ScoreUpdateListener;
import tc.oc.chatmoderator.util.ChatModeratorUtil;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.warnings.ViolationPreWarningListener;
import tc.oc.chatmoderator.whitelist.factories.WhitelistFactory;
import tc.oc.chatmoderator.zones.ZoneType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Plugin class.
 */
public class ChatModeratorPlugin extends JavaPlugin {
    private boolean debugEnabled;
    private Set<Listener> listeners;
    private PlayerManager playerManager;
    private Configuration configuration;

    public static int MINIMUM_SCORE_NO_SEND;
    public static float PARTIALLY_OFFENSIVE_RATIO;

    /**
     * Gets whether or not debug mode is enabled.
     *
     * @return Whether or not debug mode is enabled.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void onDisable() {
        for (Listener listener : this.listeners) {
            if (listener instanceof ChatModeratorListener) {
                ((ChatModeratorListener) listener).getZoneManager().unRegisterAllZones();
                ((ChatModeratorListener) listener).getFilterManager().unRegisterAllFilters();
            }
            HandlerList.unregisterAll(listener);
        }
        this.playerManager = null;
        this.configuration = null;
    }

    @Override
    public void onEnable() {
        // Set up configuration, copy defaults, etc etc
        this.saveDefaultConfig();
        this.reloadConfig();
        this.configuration = this.getConfig();
        this.saveResource(new File("dictionary.yml").getPath(), false);

        // Set up the listeners and player manager
        this.listeners = new HashSet<>();
        this.playerManager = new PlayerManager(this);
        
        // Add debug options
        this.debugEnabled = this.configuration.getBoolean("debug.enabled", false);
        if (this.debugEnabled) {
            this.listeners.add(new DebugListener());
        }
        
        // Register settings
        Settings.register();

        // Initialize the listener, add filters as necessary
        ChatModeratorListener moderatorListener = new ChatModeratorListener(this);
        setUpFilters(moderatorListener);
        setUpZones(moderatorListener);

        this.listeners.add(moderatorListener);
        this.listeners.add(new ScoreUpdateListener(this.getPlayerManager()));
        this.listeners.add(new ViolationPreWarningListener(this.getPlayerManager()));

        // And register all the events.
        for (Listener listener : this.listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }

        MINIMUM_SCORE_NO_SEND = getConfig().getInt("channels.minimum-score-no-send", 30);
        PARTIALLY_OFFENSIVE_RATIO = (float) getConfig().getDouble("channels.partially-offensive", 0.65d);

        ChannelsPlugin channelsPlugin = ChannelsPlugin.get();
        channelsPlugin.setDefaultChannel(
            new SimpleFilteredChannel(
                getConfig().getString("channels.global.format"),
                new Permission("channels.global", PermissionDefault.TRUE),
                MINIMUM_SCORE_NO_SEND,
                PARTIALLY_OFFENSIVE_RATIO
            )
        );
        channelsPlugin.setGlobalChannel(channelsPlugin.getDefaultChannel());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("chatmoderator.reload")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        this.onDisable();
        this.onEnable();

        sender.sendMessage(ChatColor.AQUA + "[ChatModerator] - " + ChatColor.DARK_GREEN + "Successfully reloaded config and registered all filters and zones.");

        return true;
    }

    /**
     * Sets up all the filters in the CMListener.
     *
     * Lower priorities gets run first.
     */
    private void setUpFilters(ChatModeratorListener moderatorListener) {
        moderatorListener.getFilterManager().registerFilter(
            new DuplicateMessageFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(DuplicateMessageFilter.class),
                getConfig().getLong("config.delay-between-messages", 250),
                getConfig().getLong("config.delay-between-same-messages", 3000),
                getConfig().getInt("filters.duplicate-messages.priority")
            ));

        moderatorListener.getFilterManager().registerFilter(
            new IPFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(IPFilter.class),
                new WeightedFilterFactory(this, "filters.server-ip.expressions").build().getWeights(),
                getConfig().getInt("filters.server-ip.priority"),
                FixStyleApplicant.FixStyle.getFixStyleFor(getConfig().getString("filters.ipfilter.fix-style", "NONE")),
                new WhitelistFactory(this, "filters.server-ip.whitelist").build().getWhitelist()
            ));

        moderatorListener.getFilterManager().registerFilter(
            new ProfanityFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(ProfanityFilter.class),
                new TemplateFactory(this, "filters.profanity").build().getWeights(),
                getConfig().getInt("filters.profanity.priority"),
                new WhitelistFactory(this, "filters.profanity.whitelist").build().getWhitelist(),
                FixStyleApplicant.FixStyle.getFixStyleFor(getConfig().getString("filters.profanity.fix-style", "NONE"))
            ));

        moderatorListener.getFilterManager().registerFilter(
            new AllCapsFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(AllCapsFilter.class),
                getConfig().getInt("filters.all-caps.priority"),
                new WhitelistFactory(this, "filters.all-caps.whitelist").build().getWhitelist(),
                (short) getConfig().getInt("filters.all-caps.max-length", -1)
            ));

        moderatorListener.getFilterManager().registerFilter(
            new RepeatedCharactersFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(RepeatedCharactersFilter.class),
                getConfig().getInt("filters.repeated-characters.count"),
                getConfig().getInt("filters.repeated-characters.priority")
            ));

        moderatorListener.getFilterManager().registerFilter(
            new BubbleFilter(
                this.getPlayerManager(),
                ChatModeratorUtil.permissions.get(BubbleFilter.class),
                getConfig().getInt("filters.bubbles.priority")
            ));
    }

    /**
     * Sets up all the zones for the ChatModeratorListener.
     *
     * @param moderatorListener The {@link tc.oc.chatmoderator.listeners.ChatModeratorListener} to work off of.
     */
    private void setUpZones(ChatModeratorListener moderatorListener) {
        moderatorListener.getZoneManager().registerZone(ZoneType.CHAT, new ZoneFactory(this, "zones.chat").build().getZone());
        moderatorListener.getZoneManager().registerZone(ZoneType.SIGN, new ZoneFactory(this, "zones.signs").build().getZone());
        moderatorListener.getZoneManager().registerZone(ZoneType.CHANNEL, new ZoneFactory(this, "zones.channel").build().getZone());
    }

    /**
     * Gets the player manager.
     *
     * @return The player manager.
     */
    public final PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
