/*
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package pivotal.field.example.gemfirehttpsession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@EnableGemFireHttpSession(poolName = "DEFAULT",
        regionName = "test",
        maxInactiveIntervalInSeconds = 180
)
@SpringBootApplication
@RestController
public class GemFireRestSession extends BaseGemFireSession {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(GemFireRestSession.class, args);
    }

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @RequestMapping("/home")
    public String home(HttpSession httpSession) throws UnknownHostException, JsonProcessingException {
        SomeData someData = (SomeData) httpSession.getAttribute(USER_OBJECT);
        if (someData != null) {
            someData.getLocations().add("REST  - " + InetAddress.getLocalHost().getCanonicalHostName());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(someData);
        }
        return "bad token";
    }
}
