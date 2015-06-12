package clippy;

import org.dalesbred.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * Created by ilkkaharmanen on 11.06.2015.
 */
@Repository
public class NoteDB {

    @Autowired
    private Database database;

    @PostConstruct
    public void init() {
        database.update("CREATE TABLE note (key VARCHAR(50) PRIMARY KEY, value VARCHAR(2000), version INTEGER)");
    }

    public Note saveNote(Note note) {
        database.update("INSERT INTO note values(?,?,?)", note.getKey(), note.getValue(), 0);

        return getNote(note.getKey());
    }

    @Transactional
    public Note updateNote(Note note) {
        Note oldNote = getNote(note.getKey());
        if (oldNote == null) {
            return saveNote(note);
        } else if (!Objects.equals(oldNote.getVersion(), note.getVersion())) {
            throw new IllegalArgumentException("Trying to update old version");
        } else {
            int updates = database.update("UPDATE note SET value = ?, version = ? WHERE key = ? and version = ?", note.getValue(), note.getVersion().intValue() + 1, note.getKey(), note.getVersion());
            return getNote(note.getKey());
        }
    }

    public Note getNote(String key) {
        return database.findUniqueOrNull(Note.class, "select key, value, version from note where key = ?", key);
    }

}
