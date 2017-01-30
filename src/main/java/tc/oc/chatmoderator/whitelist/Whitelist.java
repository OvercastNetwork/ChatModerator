package tc.oc.chatmoderator.whitelist;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import tc.oc.chatmoderator.words.Word;

import java.util.List;

public class Whitelist {

    private final List<String> whitelist;

    public Whitelist(List<String> whitelist) {
        this.whitelist = Preconditions.checkNotNull(whitelist, "Whitelist");
    }

    public boolean containsWord(Word word, boolean caseInsensitive) {
        return containsString(word.getWord(), caseInsensitive);
    }

    public boolean containsString(String word, boolean caseInsensitive) {
        for (String s : this.whitelist) {
            if (caseInsensitive) {
                if (s.equalsIgnoreCase(word)) {
                    return true;
                }
            } else {
                if (s.equals(word)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ImmutableList<String> getWhitelist() {
        return ImmutableList.copyOf(whitelist);
    }
}
