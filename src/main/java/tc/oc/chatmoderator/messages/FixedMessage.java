package tc.oc.chatmoderator.messages;

import com.google.common.base.Preconditions;

import org.joda.time.Instant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Data class used to represent a potential fixed message.
 */
public class FixedMessage {

    /**
     * Represents the original message sent in chat.
     */
    private @Nonnull String original;

    /**
     * Represents the time that the original message was sent.
     * All violations pertaining to the original message will have this {@link org.joda.time.Instant}.
     */
    private @Nonnull Instant timeSent;

    /**
     * Represents the fixed message that may or may not be sent in chat.
     */
    private @Nullable String fixed;

    /**
     * Constructor for the {@link tc.oc.chatmoderator.messages.FixedMessage} class
     *
     * @param original The original message that was sent.
     */
    public FixedMessage(final String original, final Instant timeSent) {
        this.original = Preconditions.checkNotNull(original, "original");
        this.fixed = this.original;
        this.timeSent = Preconditions.checkNotNull(timeSent, "time");
    }

    /**
     * Constructor for the {@link tc.oc.chatmoderator.messages.FixedMessage} class
     *
     * @param original The original message that was sent.
     */
    public FixedMessage(final String original, final String fixed, final Instant timeSent) {
        this.original = Preconditions.checkNotNull(original, "original");
        this.fixed = Preconditions.checkNotNull(fixed, "fixed");
        this.timeSent = Preconditions.checkNotNull(timeSent, "time");
    }

    /**
     * Gets the original message.
     *
     * @return The original message.
     */
    public String getOriginal() {
        return this.original;
    }

    /**
     * Sets the contents of the message to be fixed.
     *
     * @param fixed The new fixed message.
     */
    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    /**
     * Gets the fixed message.
     *
     * @return The fixed message.
     */
    public String getFixed() {
        return this.fixed;
    }

    /**
     * Gets the Instant when this message was sent.
     *
     * @return When this message was sent.
     */
    public Instant getTimeSent() {
        return this.timeSent;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("FixedMessage{");

        builder.append("original='").append(this.getOriginal()).append('\'');
        builder.append(", fixed='").append(this.getFixed()).append('\'');
        builder.append(", timeSent=").append(this.timeSent);
        builder.append('}');

        return builder.toString();
    }
}
