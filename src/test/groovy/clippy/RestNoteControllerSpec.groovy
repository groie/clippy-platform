package clippy

import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by ilkkaharmanen on 15.06.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
class RestNoteControllerSpec extends Specification {

    @Value('${local.server.port}')
    int port
    def client

    void setup() {
        client = new RESTClient("http://localhost:${port}")
    }

    void "default endpoint answers"() {
        when:
        def response = client.get([path: "/api/v1/"]);
        then:
        with(response) {
            status == 200
            contentType == "text/plain"
        }
    }
}
