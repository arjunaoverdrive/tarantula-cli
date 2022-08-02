package org.arjunaoverdrive.app.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.arjunaoverdrive.app.web.ApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CliRunner {

    @Autowired
    ApiController controller;
    @ShellMethod("init")
    public void init(){
        controller.init();
    }

    @ShellMethod(value = "Displays detailed search engine statistics: indexed sites, number of indexed sites, pages, lemmas, indexing errors, etc.")
    public void stats() throws JsonProcessingException {
        String json = controller.getStatistics();
        System.out.println(json);
    }

    @ShellMethod(value = "Starts indexing. Without arguments indexes all sites specified in the application.yml.\n\tWhen run with a URL as a parameter, indexes the specified site")
    public void index(@ShellOption( value = {"-u"}, defaultValue = "") String url, @ShellOption(value = {"-p"}, defaultValue = "")String page) throws JsonProcessingException {
        String json;
        if(!url.isEmpty()){
            json = controller.indexSite(url);
        } else if(!page.isEmpty()){
            json = controller.indexPage(page);
        }
        else {
            json = controller.startIndexing();
            json = json.replace("\\\"", "\"").replace("{", "\n{").replace("}", "}\n");
        }
        System.out.println(json);
    }


    @ShellMethod(value = "Stops indexing. Sometimes indexing doesn't stop immediately due to the specifics of the engine,\n\tbut if the command invocation returned true, it will be interrupted at some moment.")
    public void stop() throws JsonProcessingException {
        String json = controller.stopIndexing();
        System.out.println(json);
    }

}
