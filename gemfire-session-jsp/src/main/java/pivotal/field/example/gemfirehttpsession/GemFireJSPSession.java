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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@EnableGemFireHttpSession(poolName = "DEFAULT",
        regionName = "test",
        maxInactiveIntervalInSeconds = 180
)
@SpringBootApplication
public class GemFireJSPSession extends BaseGemFireSession {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(GemFireJSPSession.class, args);
    }

    @RequestMapping("/*")
    public String home(HttpSession session) {
        System.out.println("session = " + session);
        return "index";
    }

    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new CookieHttpSessionIdResolver();
    }
}
