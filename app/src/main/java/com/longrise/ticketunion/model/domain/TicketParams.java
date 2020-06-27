package com.longrise.ticketunion.model.domain;

public class TicketParams {
    private String url;
    private String title;

    public TicketParams() {
    }

    public TicketParams(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TicketParams{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
