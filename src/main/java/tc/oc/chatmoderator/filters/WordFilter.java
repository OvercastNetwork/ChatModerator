package tc.oc.chatmoderator.filters;

import com.google.common.base.Preconditions;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.words.factories.WordSetFactory;

public abstract class WordFilter extends Filter {

    private boolean useFixed;

    protected Whitelist whitelist;

    protected WordFilter(PlayerManager playerManager, String exemptPermission, int priority, boolean useFixed, Whitelist whitelist, FixStyleApplicant.FixStyle fixStyle) {
        super(playerManager, exemptPermission, priority, fixStyle);

        this.useFixed = useFixed;
        this.whitelist = Preconditions.checkNotNull(whitelist);
    }

    protected WordSet makeWordSet(final FixedMessage message) {
        WordSetFactory factory = new WordSetFactory(message, this.useFixed);

        return factory.build().getWordSet();
    }
}
