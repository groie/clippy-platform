package clippy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.get
import static com.jayway.restassured.RestAssured.given

/**
 * Created by ilkkaharmanen on 15.06.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
class RestNoteControllerSpec extends Specification {

    @Autowired
    NoteDB noteDB;

    @Value('${local.server.port}')
    int port
    def urlPrefix = "http://localhost";
    def path = "/api/v1/"

    def baseUrl;

    void setup() {
        baseUrl = "${urlPrefix}:${port}${path}"
    }

    void cleanup() {
        // TODO: setup transactional tests when the DB is extracted from the core
        noteDB.deleteAll()
    }

    void "default endpoint answers"() {
        when:
        def response = get(baseUrl)
        then:
        with(response) {
            statusCode == 200
            contentType ==~ /text\/plain.*/
        }
    }

    void "read non-existing note creates new one and returns it"() {
        when:
        def response = get("${baseUrl}newnote")
        then:
        with(response) {
            statusCode == 200
            contentType ==~ /application\/json.*/
        }

        with(response.jsonPath()) {
            getString("key") == "newnote"
            getString("value") == ""
            getInt("version") == 0
        }
    }

    void "reading existing note is returned"() {
        when:
        noteDB.saveNote(new Note("savednote", "savedvalue", 0))
        def response = get("${baseUrl}savednote")
        then:
        with(response) {
            statusCode == 200
            contentType ==~ /application\/json.*/
        }

        with(response.jsonPath()) {
            getString("key") == "savednote"
            getString("value") == "savedvalue"
            getInt("version") == 0
        }
    }

    void "updating or creating a note over api"() {
        when:
        def response = given().contentType("application/json")
                              .body(new Note("postingnote", "postingvalue", 0))
                              .post("${baseUrl}postingnote")
        then:
        with(response) {
            statusCode == 200
            contentType ==~ /application\/json.*/
        }

        with(response.jsonPath()) {
            getString("key") == "postingnote"
            getString("value") == "postingvalue"
            getInt("version") == 0
        }
    }


    void "updating with old version number fails"() {
        when:
        def note = noteDB.saveNote(new Note("savednote", "savedvalue", 1))
        def oldNote = new Note(note.key, note.value, 0)
        def response = given().contentType("application/json")
                .body(oldNote)
                .post("${baseUrl}savednote")
        then:
        with(response) {
            statusCode == 409
            contentType ==~ /application\/json.*/
        }
    }

}
