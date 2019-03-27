package osmparser;

public class Tag {
    private final String tagKey;
    private final String tagValue;

    public Tag(String tagKeyToMatch) {
        this(tagKeyToMatch, null);
    }

    public Tag(String tagKey, String tagValue) {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
    }

    public String getTagKey() {
        return tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    @Override
    public String toString() {
        return tagValue == null
            ? String.format("WayTag{tagKey='%s'}", tagKey)
            : String.format("WayTag{tagKey='%s', tagValue='%s'}", tagKey, tagValue);
    }
}
