package model;

import java.io.Serializable;

public class Request implements Serializable {

    private String method;
    private String url;
    private String httpVersion;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Request(String request) {
        try {
            String[] requestArr = request.split("\\s");
            method = requestArr[0];
            url = requestArr[1];
            httpVersion = requestArr[2];
        } catch (Exception e) {
            System.out.println(request);
            throw e;
        }

    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
