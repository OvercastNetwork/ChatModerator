package tc.oc.chatmoderator;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.joda.time.Instant;
import tc.oc.chatmoderator.events.ViolationAddEvent;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.*;

/**
 * Represents a set of violations for an individual player.
 */
public final class PlayerViolationManager {
    private final Player player;
    private final Map<Class<? extends Violation>, ViolationSet> violations = new HashMap<>();
    private FixedMessage lastMessage;
    private boolean warned = false;

    private double score;
    private int messagesSinceLastViolation;
    
    /**
     * Creates a new violation set.
     *
     * @param player The player.
     */
    public PlayerViolationManager(final Player player) {
        this.player = Preconditions.checkNotNull(player, "Player");
        
        setUpViolations();
        this.score = 0;
        this.messagesSinceLastViolation = 0;
    }

    /**
     * Helper method to add all violations to the violation map (this is where their values are declared)
     * Add more of these as necessary...
     */
    private void setUpViolations() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ChatModerator");
        FileConfiguration pluginConfig = plugin.getConfig();

        Preconditions.checkArgument(plugin.isEnabled(), "plugin not enabled");

        this.violations.put(DuplicateMessageViolation.class, new ViolationSet(pluginConfig.getDouble("filters.duplicate-messages.default-level", 1)));
        this.violations.put(ServerIPViolation.class, new ViolationSet(pluginConfig.getDouble("filters.server-ip.default-level", 1)));
        this.violations.put(RepeatedCharactersViolation.class, new ViolationSet(0));
        this.violations.put(ProfanityViolation.class, new ViolationSet(pluginConfig.getDouble("filters.profanity.default-level", 1)));
        this.violations.put(AllCapsViolation.class, new ViolationSet(pluginConfig.getDouble("filters.all-caps.default-level", 1)));
        this.violations.put(LeetSpeakViolation.class, new ViolationSet(pluginConfig.getDouble("filters.leet.default-level", 1)));
        this.violations.put(BubbleViolation.class, new ViolationSet(pluginConfig.getDouble("filters.bubble.default-level", 1)));
    }
    
    /**
     * Gets the violation level for the specified violation type.
     *
     * @param violationType The type of violation.
     * @return The violation level.
     */
    public double getViolationLevel(final Class<? extends Violation> violationType) {
        double level = this.violations.get(Preconditions.checkNotNull(violationType)).getLevel();

        return level;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Adds a violation, and call the ViolationAddEvent for use elsewhere
     *
     * @param violation The violation to be added.
     */
    public void addViolation(final Violation violation) {
        Class<? extends Violation> type = Preconditions.checkNotNull(violation, "Violation").getClass();
        Set<Violation> violations = this.violations.get(type).getViolations();

        violations.add(violation);
        Bukkit.getServer().getPluginManager().callEvent(new ViolationAddEvent(violation.getPlayer(), violation));

        this.messagesSinceLastViolation = 0;
    }

    /**
     * Gets all violations.
     *
     * @return All violations.
     */
    public Set<Violation> getAllViolations() {
        Set<Violation> violations = new HashSet<>();
        for (ViolationSet violationSet : this.violations.values()) {
            for (Violation violation : violationSet.getViolations()) {
                violations.add(violation);
            }
        }
        return violations;
    }

    /**
     * Gets the violations of the specified type.
     *
     * @param type The type of violation to get.
     * @return The violations of the specified type.
     */
    public Set<Violation> getViolationsForType(final Class<? extends Violation> type) {
        Set<Violation> violations = this.violations.get(type).getViolations();

        return Collections.unmodifiableSet(violations);
    }

    /**
     * Gets all violations that occurred at the specified time.
     *
     * @param time The {@link org.joda.time.Instant} to search for.
     * @return The violations that happened at the specified time.
     */
    public Set<Violation> getViolationsForTime(Instant time) {
        Set<Violation> violations = new HashSet<Violation>();

        for(Violation v : this.getAllViolations()) {
            if(v.getTime().equals(time)) {
                violations.add(v);
            }
        }

        return violations;
    }

    /**
     * Gets the violation by querying for the specific hash code associated with the
     * {@link org.bukkit.event.player.AsyncPlayerChatEvent} that the original violation message was sent in.
     *
     * @param hashcode The hash code to query on.
     *
     * @return The {@link tc.oc.chatmoderator.violations.Violation} associated with that hashcode.
     */
    public List<Violation> getViolationsForHashCode(int hashcode) {
        List<Violation> violations = new ArrayList<>();

        for (Violation violation : this.getAllViolations()) {
            if (violation.getEvent().hashCode() == hashcode) {
                violations.add(violation);
            }
        }

        return violations;
    }

    public Instant getLastMessageTime() {
        return this.lastMessage == null ? null : this.lastMessage.getTimeSent();
    }

    public void setLastMessage(FixedMessage message) {
        this.lastMessage = message;
    }

    public FixedMessage getLastMessage() {
        return this.lastMessage;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void incrementMessagesSinceLastViolation() {
        this.messagesSinceLastViolation++;
    }

    public int getMessagesSinceLastViolation() {
        return this.messagesSinceLastViolation;
    }

    public void setWarned(boolean warned) {
        this.warned = warned;
    }

    public boolean isWarned() {
        return this.warned;
    }

    public void sendWarning(String message) {
        this.player.sendMessage(message);
    }

    /**
     * Stores a level and a set of violations per Violation type.
     */
    public class ViolationSet {
        /**
         * The level that the violation is worth
         */
        private double level;
        
        /**
         * The list of violations of this specific type
         */
        private final Set<Violation> violations = new HashSet<>();

        /**
         * Represents the callable constructor of ViolationSet
         * @param level The level for that violation
         */
        public ViolationSet(double level) {
            this.level = level;
        }
        
        /**
         * Gets the level assigned to this particular type of violation
         * @return The level
         */
        public double getLevel() {
            return this.level;
        }
        
        /**
         * Gets the violations associated with this type
         * @return The violations
         */
        public Set<Violation> getViolations() {
            return this.violations;
        }
    }
}
