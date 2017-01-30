package tc.oc.chatmoderator.channels;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import me.anxuiz.settings.bukkit.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.ChatModeratorPlugin;

import com.github.rmsy.channels.event.ChannelMessageEvent;
import com.github.rmsy.channels.impl.SimpleChannel;
import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.settings.FilterOptions;
import tc.oc.chatmoderator.settings.Settings;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.AllCapsViolation;
import tc.oc.chatmoderator.violations.core.ProfanityViolation;
import tc.oc.chatmoderator.violations.core.ServerIPViolation;

public class SimpleFilteredChannel extends SimpleChannel {

    /**
     * Unicode Character 'HEAVY MULTIPLICATION VIOLATION'
     * <a href="http://www.fileformat.info/info/unicode/char/2716/index.htm">reference</a>
     */
    public static char WARNING_SYMBOL = '\u2716';

    protected final double scoreThreshold;
    protected float partial;
    protected final @Nullable ChatModeratorPlugin chatModerator;

    public SimpleFilteredChannel(String format, final Permission permission) {
        this(format, permission, ChatModeratorPlugin.MINIMUM_SCORE_NO_SEND, ChatModeratorPlugin.PARTIALLY_OFFENSIVE_RATIO);
    }

    public SimpleFilteredChannel(String format, Permission permission, double scoreThreshold, float partial) {
        super(format, permission);

        Preconditions.checkArgument(scoreThreshold > 0, "Score threshold must be greater than 0!");
        this.scoreThreshold = scoreThreshold;
        this.partial = partial;

        this.chatModerator = (ChatModeratorPlugin) Bukkit.getPluginManager().getPlugin("ChatModerator");
    }

    /**
     * Sends a new message to the channel.
     *
     * @param rawMessage The message to be sent.
     * @param sender     The message sender, or null for console.
     * @return Whether or not the message was sent.
     */
    @Override
    public boolean sendMessage(String rawMessage, @Nullable Player sender) {
        String sanitizedMessage = ChatColor.stripColor(Preconditions.checkNotNull(rawMessage, "Message"));

        ChannelMessageEvent event = new ChannelMessageEvent(rawMessage, sender, this);
        Bukkit.getPluginManager().callEvent(event);

        PlayerViolationManager violationManager = null;

        boolean forceNoSend = false;
        boolean offensive = false;
        int violationsAttached = 0;

        if (this.chatModerator != null && sender != null) {
            violationManager = this.chatModerator.getPlayerManager().getViolationSet(sender);
            violationsAttached = violationManager.getViolationsForHashCode(event.hashCode()).size();

            if (violationManager.getScore() > this.scoreThreshold) {
                // Check if the message is offending
                offensive = true;
            }

            for (Violation v : violationManager.getViolationsForHashCode(event.hashCode())) {
                if (v.isForceNoSend()) {
                    // Check if the message is force no send
                    forceNoSend = true;
                    break;
                }
            }
        }

        // send it to the console regardless
        this.sendMessageToViewer(sender, Bukkit.getConsoleSender(), sanitizedMessage, event, offensive, violationsAttached, violationManager, false);

        if (!event.isCancelled()) {
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                if (viewer.hasPermission(this.getListeningPermission())) {
                    this.sendMessageToViewer(sender, viewer, sanitizedMessage, event, offensive, violationsAttached, violationManager, forceNoSend);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void sendMessageToViewer(Player sender, CommandSender viewer, String sanitizedMessage, ChannelMessageEvent event, boolean offensive, int violationsAttached, PlayerViolationManager violationManager, boolean forceNoSend) {
        boolean senderPresent = sender != null;

        String senderName = senderPresent ? sender.getName(viewer) : "Console";
        String senderDisplayName = senderPresent ? sender.getDisplayName(viewer) : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console";

        String message = MessageFormat.format(
                this.getFormat(),
                senderName,
                senderDisplayName,
                event.getMessage(),
                sanitizedMessage
        );

        boolean allowSend = true;

        if (viewer instanceof Player) {
            switch (PlayerSettings.getManager((Player) viewer).getValue(Settings.FILTER_SETTING, FilterOptions.class)) {
                case NONE:
                    allowSend = true;
                    break;
                case OFFENSIVE:
                    allowSend = !offensive;
                    break;
                case ALL:
                    allowSend = violationsAttached == 0;
                    break;
            }
        }

        boolean isSender = viewer.equals(event.getSender());

        StringBuilder builder = new StringBuilder();

        if (isSender) {
            if (offensive || violationsAttached != 0) {
                if (violationManager.getScore() * this.partial < this.scoreThreshold) {
                    builder.append(ChatColor.GOLD);
                } else {
                    builder.append(ChatColor.RED);
                }

                builder.append(ChatColor.BOLD).append(WARNING_SYMBOL).append(ChatColor.RESET);
            }
            builder.append(this.underlineViolations(violationManager, message, event));
            viewer.sendMessage(builder.toString());
        } else if (allowSend && !forceNoSend) {
            this.sendMessageToViewer(sender, viewer, sanitizedMessage, event);
        }
    }

    private String underlineViolations(PlayerViolationManager violationManager, String raw, Event e) {
        String underlined = raw;

        for (Violation violation : violationManager.getViolationsForHashCode(e.hashCode())) {
            if (violation instanceof AllCapsViolation) {
                for (String word : ((AllCapsViolation) violation).getUpperCaseWords()) {
                    underlined = underlined.replace(word, underline(word));
                }
            } else if (violation instanceof ProfanityViolation) {
                for (String profanity : ((ProfanityViolation) violation).getProfanities()) {
                    underlined = underlined.replace(profanity, underline(profanity));
                }
            } else if (violation instanceof ServerIPViolation) {
                for (String ip : ((ServerIPViolation) violation).getIPAddresses()) {
                    underlined = underlined.replace(ip, underline(ip));
                }
            }
        }

        return underlined;
    }

    private String underline(String word) {
        return ChatColor.UNDERLINE + word + ChatColor.RESET;
    }

}
