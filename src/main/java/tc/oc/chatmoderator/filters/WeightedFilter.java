package tc.oc.chatmoderator.filters;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.util.FixStyleApplicant;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents a filter that filters chat messages with a set of expressions to apply, each with their own weight.
 */
public abstract class WeightedFilter extends Filter {

    /**
     * Maps the pattern to search on to a double which represents a violation level.
     */
    private final Map<Pattern, Double> weights;

    /**
     * Represents the instantiable version of the WeightedFilter.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that will exempt you from the filter.
     */
    public WeightedFilter(final PlayerManager playerManager, String exemptPermission, final HashMap<Pattern, Double> weights, int priority, FixStyleApplicant.FixStyle fixStyle) {
        super(playerManager, exemptPermission, priority, fixStyle);

        this.weights = Preconditions.checkNotNull(weights);
    }

    /**
     * Gets the key-value store of all weights in the class
     *
     * @return The weights.
     */
    protected Map<Pattern, Double> getWeights() {
        return this.weights;
    }

    /**
     * Gets the weight for a specific expression.
     *
     * @param expression The expression.
     * @return The weight associated with that specific expression.
     */
    public Double getWeightFor(String expression) {
        return this.getWeightFor(Pattern.compile(Preconditions.checkNotNull(expression, "expression")));
    }

    public Double getWeightFor(Pattern expression) {
        return this.weights.get(Preconditions.checkNotNull(expression, "expression"));
    }

}
