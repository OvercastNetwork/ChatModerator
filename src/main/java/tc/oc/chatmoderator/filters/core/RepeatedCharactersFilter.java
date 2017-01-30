package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.util.PatternUtils;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.RepeatedCharactersViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepeatedCharactersFilter extends Filter {

    private final Pattern pattern;
    private final int characterCount;

    public RepeatedCharactersFilter(PlayerManager playerManager, String exemptPermission, final int characterCount, int priority) {
        super(playerManager, exemptPermission, priority, FixStyleApplicant.FixStyle.NONE);

        Preconditions.checkArgument(characterCount > 1, "Character count must be greater than 1.");
        this.characterCount = characterCount;

        this.pattern = RepeatedCharactersFilter.generatePattern(this.characterCount - 1);
    }

    /**
     * Called when a player sends a message in chat.  Searches for repeated characters greater or equal to a certain length.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Nullable
    @Override
    public FixedMessage filter(FixedMessage message, final Player player, ZoneType type, Event event) {
        if (player.hasPermission(this.getExemptPermission())) {
            return message;
        }

        Matcher matcher = this.pattern.matcher(message.getFixed());
        List<String> repeatedCharacters = new ArrayList<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "player"));
        Violation violation = new RepeatedCharactersViolation(message.getTimeSent(), player, message, violationManager.getViolationLevel(RepeatedCharactersViolation.class), repeatedCharacters, type, event);

        while(matcher.find()) {
            if (PatternUtils.doesPlayerExist(matcher.group())) {
                continue;
            }

            repeatedCharacters.add(matcher.group());

            char replace = matcher.group().length() > 0 ? matcher.group().charAt(0) : ' ';

            // We still need to keep this here because it allows us to catch profanity with many characters
            message.setFixed(message.getFixed().replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(replace + "")));
        }

        if(repeatedCharacters.size() > 0) {
//            violationManager.addViolation(violation);
        }

        return message;
    }

    /**
     * Generates a pattern for a number of repeated characters greater than charCount.
     *
     * @param charCount The amount of characters to allow before triggering the filter.
     *
     * @return The compiled pattern.
     */
    public static Pattern generatePattern(int charCount) {
        StringBuilder builder = new StringBuilder();
        builder.append("(.)\\1{");
        builder.append(charCount);
        builder.append(",}");

        return Pattern.compile(builder.toString());
    }
}
