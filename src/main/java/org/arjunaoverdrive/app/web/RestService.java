package org.arjunaoverdrive.app.web;

import org.apache.tomcat.util.codec.binary.Base64;
import org.arjunaoverdrive.app.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RestService {

    private final RestTemplate restTemplate;
    private final ConfigProperties properties;

    @Autowired
    public RestService(RestTemplate restTemplate, ConfigProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }


    public ResponseEntity getStatistics(){
        HttpHeaders headers = setHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(Endpoints.STATISTICS.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity startIndexing() {
        HttpHeaders headers = setHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(Endpoints.START_URL.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity indexSite(String site) {
        HttpHeaders headers = setHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(Endpoints.INDEX_SITE.getUrl()+"?url="+site, HttpMethod.POST, entity, String.class);
        return response;
    }

    public ResponseEntity stopIndexing() {
        HttpHeaders headers = setHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(Endpoints.STOP_URL.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity indexPage(String url) {
        HttpHeaders headers = setHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(Endpoints.INDEX_PAGE.getUrl()+"?url="+url,HttpMethod.POST, entity, String.class );
        return response;
    }

    private HttpHeaders setHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers = setAuthHeaders(headers);
        headers = setContentType(headers);
        return headers;
    }

    private HttpHeaders setAuthHeaders(HttpHeaders headers){
        String auth = properties.getUserName()+":"+properties.getUserPassword();
        byte[]encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        return headers;
    }

    private HttpHeaders setContentType(HttpHeaders headers){
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
