package clippy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by ilkkaharmanen on 11.06.2015.
 */
@RestController
public class NoteController {

    @Autowired
    private NoteDB noteDB;

    @RequestMapping(value = "/", method = GET)
    public String welcome() {
        return "Welcome to Clippy";
    }

    @RequestMapping(value = "/{key}", method = GET)
    public Note getNote(@PathVariable("key") String key) {
        Note note = noteDB.getNote(key);

        if (note == null) {
            return noteDB.saveNote(new Note(key, "", 0));
        } else {
            return note;
        }
    }


    @RequestMapping(value = "/{key}", method = POST)
    public Note saveNote(@PathVariable("key") String key, @RequestBody Note note) {
        return noteDB.updateNote(note);
    }
}
