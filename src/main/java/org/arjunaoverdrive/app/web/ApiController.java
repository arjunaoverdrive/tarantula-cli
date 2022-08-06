package org.arjunaoverdrive.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ApiController {

    private final RestService service;
    private final JsonParser jsonParser;

    @Autowired
    public ApiController(RestService service) {
        this.service = service;
        this.jsonParser = JsonParser.getJsonParser();
    }

    @GetMapping("/")
    public String init() {
        return LocalDateTime.now().toString();
    }

    @GetMapping("/statistics")
    public String getStatistics() {
        String json = service.getStatistics().getBody().toString();
        return jsonParser.convertJsonToString(json);
    }

    @GetMapping("/startIndexing")
    public String startIndexing()  {

        String json = (String) service.startIndexing().getBody();
        return jsonParser.convertJsonToString(json);
    }

    @GetMapping("/stopIndexing")
    public String stopIndexing()  {
        String json = service.stopIndexing().getBody().toString();
        return jsonParser.convertJsonToString(json);
    }

    @PostMapping("/indexSite")
    public String indexSite(@RequestParam(name = "url") String url)  {

        String json = service.indexSite(url).getBody().toString();
        return jsonParser.convertJsonToString(json);
    }

    @PostMapping("/indexPage")
    public String indexPage(@RequestParam(name = "url") String url) {
        String json = service.indexPage(url).getBody().toString();
        return jsonParser.convertJsonToString(json);
    }

    @GetMapping("/search")
    public String search(@RequestParam String query,
                         @RequestParam(required = false) String site,
                         @RequestParam(required = false) Integer offset,
                         @RequestParam(required = false) Integer limit){
        String json = service.search(query, site,offset, limit).getBody().toString();
        return jsonParser.convertJsonToString(json);
    }

    public String getListOfErrorPages() {
        String json = service.getListOfErrorPages().getBody().toString();
        return jsonParser.convertJsonToString(json);
    }
}
