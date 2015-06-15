package clippy

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

    @Value('${local.server.port}')
    int port
    def path = "/api/v1/"

    void "default endpoint answers"() {
        when:
        def url = "http://localhost:${port}${path}"
        def response = get(url)
        then:
        with(response) {
            statusCode == 200
            contentType ==~ /text\/plain.*/
        }
    }

    void "read non-existing note creates new one and returns it"() {
        when:
        def response = get("http://localhost:${port}${path}newnote")
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
}
