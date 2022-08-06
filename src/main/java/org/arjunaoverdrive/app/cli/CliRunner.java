package org.arjunaoverdrive.app.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.swing.interop.SwingInterOpUtils;
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

    @ShellMethod(value = "Starts indexing. Without arguments indexes all sites specified in the application.yml." +
            "\n\tWhen run with the -u flag and a URL as a parameter, indexes the specified site;" +
            "\n\twhen run with the -p flag and a page URL as a parameter, indexes a page")
    public void index(@ShellOption( value = {"-u"}, defaultValue = "") String url, @ShellOption(value = {"-p"}, defaultValue = "")String page) {
        String json;
        if(!url.isEmpty()){
            json = controller.indexSite(url);
        }
        else if(!page.isEmpty()){
            json = controller.indexPage(page);
        }
        else {
            json = controller.startIndexing();
            json = json.replace("\\\"", "\"").replace("{", "\n{").replace("}", "}\n");
        }
        System.out.println(json);
    }

    @ShellMethod(value = "Stops indexing. Sometimes indexing doesn't stop immediately due to the specifics of the engine," +
            "\n\tbut if the command invocation returned true, it will be interrupted at some moment.")
    public void stop() throws JsonProcessingException {
        String json = controller.stopIndexing();
        System.out.println(json);
    }

    @ShellMethod(value = "Search string supplied as the query parameter. Options:\n\t" +
            "-q - query to look for (required)\n\t-s - site\n\t-o offset (default 0)\n\t-l - limit(default 20).")
    public void search(@ShellOption(value = {"-q"}) String query,
                       @ShellOption(value = {"-s"}, defaultValue = "") String site,
                       @ShellOption(value = {"-o"}, defaultValue = "0") String offset,
                       @ShellOption(value = {"-l"}, defaultValue = "20") String limit){
        String json = controller.search(query, site, Integer.valueOf(offset), Integer.valueOf(limit));
        System.out.println(json);
    }

    @ShellMethod(value = "List pages not indexed due to status other than 200")
    public void errors() throws JsonProcessingException{
        String json = controller.getListOfErrorPages();
        System.out.println(json);
    }
}
