package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.LeetSpeakViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeetSpeakFilter extends Filter {

    private Map<Character, List<Pattern>> dictionary;
    private ArrayList<Pattern> patterns;

    public LeetSpeakFilter(PlayerManager playerManager, String exemptPermission, int priority, Map<Character, List<Pattern>> dictionary) {
        super(playerManager, exemptPermission, priority, FixStyleApplicant.FixStyle.NONE);

        this.dictionary = Preconditions.checkNotNull(dictionary);
        this.patterns = new ArrayList<>();

        for (List<Pattern> list : dictionary.values()) {
            for (Pattern p : list) {
                patterns.add(p);
            }
        }

        Collections.sort(patterns, new Comparator<Pattern>() {
            @Override
            public int compare(Pattern p1, Pattern p2) {
                return p2.toString().length() - p1.toString().length();
            }
        });
    }

    /**
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
    public FixedMessage filter(FixedMessage message, Player player, ZoneType type, Event event) {
        if (player.hasPermission(this.getExemptPermission())) {
            return message;
        }

        PlayerViolationManager violations = this.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "Player"));
        ArrayList<String> words = new ArrayList<>();
        Violation violation = new LeetSpeakViolation(message.getTimeSent(), player, message, violations.getViolationLevel(LeetSpeakViolation.class), words, type, event);

        for (Pattern pattern : this.patterns) {

            Matcher matcher = pattern.matcher(message.getFixed());

            while (matcher.find()) {
                message.setFixed(message.getFixed().replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(this.getCharacterFor(pattern).toString())));
                words.add(StringEscapeUtils.escapeJava(matcher.group()));
            }
        }

        if (words.size() > 0) {
            violations.addViolation(violation);
        }

        return message;
    }

    public Character getCharacterFor(Pattern pattern) {
        for (Character character : dictionary.keySet()) {
            if (dictionary.get(character).contains(pattern)) {
                return character;
            }
        }

        return null;
    }
}
