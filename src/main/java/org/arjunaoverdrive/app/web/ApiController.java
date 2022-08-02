package org.arjunaoverdrive.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ApiController {

    private final RestService service;
    private final ObjectMapper mapper;

    @Autowired
    public ApiController(RestService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public String init() {
        return LocalDateTime.now().toString();
    }

    @GetMapping("/statistics")
    public String getStatistics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = service.getStatistics().getBody().toString();
        JsonNode root = mapper.readTree(json);
        String stats = JsonParser.traverse(root, 0);

        return stats;
    }

    @GetMapping("/startIndexing")
    public String startIndexing() throws JsonProcessingException {

        String json = (String) service.startIndexing().getBody();
        JsonNode root = mapper.readTree(json);
        String response = JsonParser.traverse(root, 0);
        return response;
    }

    @GetMapping("/stopIndexing")
    public String stopIndexing() throws JsonProcessingException {

        String json = service.stopIndexing().getBody().toString();
        JsonNode root = mapper.readTree(json);
        String response = JsonParser.traverse(root, 0);
        return response;
    }

    @PostMapping("/indexSite")
    public String indexSite(@RequestParam(name = "url") String url) throws JsonProcessingException {

        String json = service.indexSite(url).getBody().toString();
        JsonNode root = mapper.readTree(json);
        String response = JsonParser.traverse(root, 0);
        return response;
    }

    @PostMapping("/indexPage")
    public String indexPage(@RequestParam(name = "url") String url) throws JsonProcessingException{
        String json = service.indexPage(url).getBody().toString();
        JsonNode root = mapper.readTree(json);
        String response = JsonParser.traverse(root, 0);
        return response;

    }
}
