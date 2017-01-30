package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.WordFilter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.util.PatternUtils;
import tc.oc.chatmoderator.util.FixStyleApplicant.FixStyle;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.AllCapsViolation;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters out and dispatches violations on messages that have part or all containing all capital letters.
 */
public class AllCapsFilter extends WordFilter {

    private final Pattern pattern;

    private final short maxChars;

    /**
     * Publicly insatiable version of the AllCapsFilter.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that exempts a player from the AllCapsFilter.
     * @param maxChars The maximum number of characters before the filter kicks in.
     */
    public AllCapsFilter(PlayerManager playerManager, String exemptPermission, int priority, Whitelist whitelist, final short maxChars) {
        super(playerManager, exemptPermission, priority, true, whitelist, FixStyleApplicant.FixStyle.NONE);
        Preconditions.checkArgument(maxChars > -1, "Max chars must be at least zero!");

        this.maxChars = maxChars;
        this.pattern = Pattern.compile("[A-Z]{" + (maxChars+1) + ",}");
    }

    /**
     * Filters out messages written in all-caps
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Override
    public @Nullable FixedMessage filter(FixedMessage message, Player player, ZoneType type, Event event) {
        if (player.hasPermission(this.getExemptPermission())) {
            return message;
        }

        WordSet wordSet = this.makeWordSet(message);

        Matcher matcher = null;
        Set<String> upperCaseWords = new HashSet<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);
        Violation violation = new AllCapsViolation(message.getTimeSent(), player, message, violationManager.getViolationLevel(AllCapsViolation.class), upperCaseWords, type, event);

        for (Word word : wordSet.toList()) {
            Preconditions.checkNotNull(word.getWord(), "word");

            if (this.whitelist.containsWord(word, false) || PatternUtils.doesPlayerExist(word.getWord())) {
                word.setChecked(true);
                continue;
            }

            matcher = this.pattern.matcher(word.getWord());

            while (matcher.find()) {
                upperCaseWords.add(matcher.group().trim());

                FixStyleApplicant.fixString(message.getFixed(), FixStyle.NONE);
            }

            /*
            if (upperCaseWords.size() > 0) {
                violationManager.addViolation(violation);
                upperCaseWords.clear();
            }
            */

            word.setChecked(true);
        }

        return message;
    }

    /**
     * Get the pattern for all-caps.
     *
     * @return The pattern.
     */
    public static Pattern getBasePattern(int maxChars) {
        return Pattern.compile("[A-Z0-9]{" + maxChars + ",}");
    }
}
