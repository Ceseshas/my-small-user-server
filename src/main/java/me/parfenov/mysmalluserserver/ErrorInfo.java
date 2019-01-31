package me.parfenov.mysmalluserserver;

class ErrorInfo {

    public final String url;
    public final String message;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.message = ex.getLocalizedMessage();
    }

    public ErrorInfo(String url, String message) {
        this.url = url;
        this.message = message;
    }
}