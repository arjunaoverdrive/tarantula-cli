package org.arjunaoverdrive.app.web;

public enum Endpoints {
    STATISTICS("http://localhost:9999/admin/api/statistics"),
    START_URL("http://localhost:9999/admin/api/startIndexing"),
    STOP_URL("http://localhost:9999/admin/api/stopIndexing"),
    INDEX_PAGE("http://localhost:9999/admin/api/indexPage"),
    INDEX_SITE("http://localhost:9999/admin/api/indexSite"),
    SEARCH("http://localhost:9999/admin/api/search"),
    ERRORS("http://localhost:9999/admin/api/errors");


    Endpoints(String url) {
        this.url = url;
    }

    private String url;
    public String getUrl(){
        return url;
    }

}
