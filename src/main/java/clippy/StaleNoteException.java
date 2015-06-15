package clippy;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ilkkaharmanen on 15.06.2015.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Newer version of note exists")
public class StaleNoteException extends RuntimeException {
}
