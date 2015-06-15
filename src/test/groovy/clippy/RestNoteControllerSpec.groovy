package clippy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.get

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


}
