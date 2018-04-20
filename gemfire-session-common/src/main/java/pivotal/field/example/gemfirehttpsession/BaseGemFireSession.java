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
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.session.data.gemfire.serialization.pdx.provider.PdxSerializableSessionSerializer;
import org.springframework.session.data.gemfire.serialization.pdx.support.ComposablePdxSerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BaseGemFireSession {
    public static final String VISIT_COUNTER = "visitCounter";
    public static final String UID = "uid";
    public static final String USER_OBJECT = "user_object";

    public static final String X_AUTH_TOKEN_KEY = "X-Auth-Token";

    @Autowired
    protected Environment env;


    @Bean
    public ClientCache gemfireCache(@Value("${app.gemfire.locators:localhost[10334]") String locators) throws IOException {

        ClientCacheFactory factory = new ClientCacheFactory();
        if (env.acceptsProfiles("cloud")) {
            Map services = new ObjectMapper().readValue(System.getenv("VCAP_SERVICES"), Map.class);
            Map credentials = (Map) ((Map) ((List) services.get("p-cloudcache")).get(0)).get("credentials");
            ClientAuthInitialize.setVCapServices(credentials);
            addLocators(factory, (List<String>) credentials.get("locators"));
        } else {
            addLocators(factory, Arrays.asList(locators.split(",")));
        }
        factory.setPoolSubscriptionEnabled(true);
        factory.setPdxSerializer(ComposablePdxSerializer.compose(
                new PdxSerializableSessionSerializer(),
                new ReflectionBasedAutoSerializer("pivotal.field.example.*")
        ));

        return factory.create();
    }

    protected void addLocators(ClientCacheFactory clientCacheFactory, List<String> locators) {
        locators.forEach(it -> {
            ConnectionEndpoint endpoint = ConnectionEndpoint.parse(it);
            clientCacheFactory.addPoolLocator(endpoint.getHost(), endpoint.getPort());
        });
    }
}
