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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession;
import org.springframework.session.data.gemfire.serialization.pdx.provider.PdxSerializableSessionSerializer;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EnableGemFireHttpSession(poolName = "DEFAULT",
        regionName = "test",
        maxInactiveIntervalInSeconds = 180
)
@SpringBootApplication
public class GemfireHttpsessionApplication {


    @Autowired
    private Environment env;

    public static void main(String[] args) throws IOException {

        SpringApplication.run(GemfireHttpsessionApplication.class, args);
    }

    @RequestMapping("/*")
    public String home() {
        return "index";
    }

    @Bean
    public ClientCache gemfireCache(@Value("${app.gemfire.locators:localhost[10334]") String locators) throws IOException {

        ClientCacheFactory factory = new ClientCacheFactory();
        if (env.acceptsProfiles("cloud")) {
            final JsonParser parser = JsonParserFactory.getJsonParser();
            Map services = new ObjectMapper().readValue(System.getenv("VCAP_SERVICES"), Map.class);
            Map credentials = (Map) ((Map) ((List) services.get("p-cloudcache")).get(0)).get("credentials");
            ClientAuthInitialize.setVCapServices(credentials);
            addLocators(factory, (List<String>) credentials.get("locators"));
        } else {
            addLocators(factory, Arrays.asList(locators.split(",")));
        }
        factory.setPoolSubscriptionEnabled(true);
        factory.setPdxSerializer(new PdxSerializableSessionSerializer());

        return factory.create();
    }

    private void addLocators(ClientCacheFactory clientCacheFactory, List<String> locators) {
        locators.forEach(it -> {
            ConnectionEndpoint endpoint = ConnectionEndpoint.parse(it);
            clientCacheFactory.addPoolLocator(endpoint.getHost(), endpoint.getPort());
        });
    }
}
