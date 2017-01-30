package tc.oc.chatmoderator.filters;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.words.factories.WordSetFactory;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class WeightedWordsFilter extends WeightedFilter {

    private final Whitelist whitelist;
    private boolean useFixed;

    /**
     * Represents the instantiable version of the WeightedFilter.
     *
     * @param playerManager    The base player manager.
     * @param exemptPermission The permission that will exempt you from the filter.
     * @param weights
     * @param priority
     */
    protected WeightedWordsFilter(PlayerManager playerManager, String exemptPermission, HashMap<Pattern, Double> weights, int priority, boolean useFixed, Whitelist whitelist, FixStyleApplicant.FixStyle fixStyle) {
        super(playerManager, exemptPermission, weights, priority, fixStyle);

        this.useFixed = useFixed;
        this.whitelist = whitelist;
    }

    protected Whitelist getWhitelist() {
        return this.whitelist;
    }

    @Nullable
    @Override
    public abstract FixedMessage filter(FixedMessage message, Player player, ZoneType type, Event event);

    protected WordSet makeWordSet(final FixedMessage message) {
        WordSetFactory factory = new WordSetFactory(message, this.useFixed);

        return factory.build().getWordSet();
    }
}
