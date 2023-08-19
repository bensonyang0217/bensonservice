package com.benson.bensonservice.constants;

public enum ExternalServiceUrl {
    AUTH_NOTIFY("https://notify-bot.line.me/oauth/authorize"),
    TOKEN_NOTIFY("https://notify-bot.line.me/oauth/token"),
    API_NOTIFY("https://notify-api.line.me/api/notify");

    private final String url;

    ExternalServiceUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
