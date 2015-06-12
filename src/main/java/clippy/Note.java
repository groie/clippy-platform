package clippy;

/**
 * Created by ilkkaharmanen on 11.06.2015.
 */
public class Note {

    private String key;
    private String value;
    private Integer version;

    public Note() {
    }

    public Note(String key, String value, Integer version) {
        this.key = key;
        this.value = value;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Integer getVersion() {
        return version;
    }
}
