package org.arjunaoverdrive.app.web;

import org.apache.tomcat.util.codec.binary.Base64;
import org.arjunaoverdrive.app.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.CacheRequest;
import java.util.Arrays;

@Service
public class RestService {

    private final RestTemplate restTemplate;
    private final ConfigProperties properties;
    private final HttpEntity entity;

    @Autowired
    public RestService(RestTemplate restTemplate, ConfigProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
        this.entity = getHttpEntity();
    }


    public ResponseEntity getStatistics() {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.STATISTICS.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity startIndexing() {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.START_URL.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity indexSite(String site) {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.INDEX_SITE.getUrl() + "?url=" + site, HttpMethod.POST, entity, String.class);
        return response;
    }

    public ResponseEntity stopIndexing() {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.STOP_URL.getUrl(), HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity indexPage(String url) {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.INDEX_PAGE.getUrl() + "?url=" + url, HttpMethod.POST, entity, String.class);
        return response;
    }

    public ResponseEntity search(String query, String site, Integer offset, Integer limit) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 20 : limit;
        String params = "?query=" + query + "&site=" + site + "&offset=" + offset + "&limit=" + limit;
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.SEARCH.getUrl() + params, HttpMethod.GET, entity, String.class);
        return response;
    }

    public ResponseEntity getListOfErrorPages() {
        ResponseEntity<String> response =
                restTemplate.exchange(Endpoints.ERRORS.getUrl(),HttpMethod.GET, entity, String.class);
        return response;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        setAuthHeaders(headers);
        setContentType(headers);
        return headers;
    }

    private void setAuthHeaders(HttpHeaders headers) {
        String auth = properties.getUserName() + ":" + properties.getUserPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
    }

    private void setContentType(HttpHeaders headers) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = getHeaders();
        return new HttpEntity(headers);
    }
}
