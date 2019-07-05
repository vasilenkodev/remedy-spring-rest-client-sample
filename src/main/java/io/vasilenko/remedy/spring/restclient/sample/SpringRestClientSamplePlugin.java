package io.vasilenko.remedy.spring.restclient.sample;

import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.thirdparty.org.slf4j.Logger;
import com.bmc.thirdparty.org.slf4j.LoggerFactory;
import io.vasilenko.remedy.spring.restclient.sample.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
public class SpringRestClientSamplePlugin extends ARFilterAPIPlugin {

    private final Logger log = LoggerFactory.getLogger(SpringRestClientSamplePlugin.class);

    private AnnotationConfigApplicationContext applicationContext;
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void initialize(ARPluginContext context) {
        applicationContext = new AnnotationConfigApplicationContext(SpringRestClientSamplePlugin.class);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        log.info("initialized");
    }

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> list) {
        ResponseEntity<List<User>> response = restTemplate.exchange("/vasilenkosergey/fake-online-rest-server/users/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });

        List<User> users = response.getBody();
        log.info("users: {}", users);
        return new ArrayList<>();
    }

    @Override
    public void terminate(ARPluginContext context) {
        applicationContext.close();
    }
}
