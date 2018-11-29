package model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebLog implements Serializable, Comparable<WebLog> {
    private Instant timeStamp;
    private String elb;
    private String clientAddress;
    private String backendAddress;
    private Double requestProcessingTime;
    private Double backendProcessingTime;
    private Double responseProcessingTime;
    private Integer elbStatusCode;
    private Integer backendStatusCode;
    private Long receivedBytes;
    private Long sentBytes;
    private Request request;
    private String userAgent;
    private String sslCipher;
    private String sslProtocol;

    public static WebLog getWebLog(String log) {
        List<String> logLine = parseLog(log);
        if(logLine.size() != 15) {
            return null;
        } else {
            return new WebLog(logLine);
        }
    }

    public WebLog(List<String> logLine) {
        timeStamp = Instant.parse(logLine.get(0));
        elb = logLine.get(1);
        clientAddress = logLine.get(2);
        backendAddress = logLine.get(3);
        requestProcessingTime = logLine.get(4).startsWith("-")?null:Double.parseDouble(logLine.get(4));
        backendProcessingTime = logLine.get(5).startsWith("-")?null:Double.parseDouble(logLine.get(5));
        responseProcessingTime = logLine.get(6).startsWith("-")?null:Double.parseDouble(logLine.get(6));
        elbStatusCode = Integer.parseInt(logLine.get(7));
        backendStatusCode = Integer.parseInt(logLine.get(8));
        receivedBytes = Long.parseLong(logLine.get(9));
        sentBytes = Long.parseLong(logLine.get(10));
        request = logLine.get(11).startsWith("\"-")? null : new Request(logLine.get(11));
        userAgent = logLine.get(12);
        sslCipher = logLine.get(13);
        sslProtocol = logLine.get(14);
    }

    private static List<String> parseLog(String log) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".*?\")\\s*").matcher(log);
        while(m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }
        return list;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public Long getTimeStampInMillis() {
        return timeStamp.toEpochMilli();
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getElb() {
        return elb;
    }

    public void setElb(String elb) {
        this.elb = elb;
    }

    public String getBackendAddress() {
        return backendAddress;
    }

    public void setBackendAddress(String backendAddress) {
        this.backendAddress = backendAddress;
    }

    public Double getRequestProcessingTime() {
        return requestProcessingTime;
    }

    public void setRequestProcessingTime(Double requestProcessingTime) {
        this.requestProcessingTime = requestProcessingTime;
    }

    public Double getBackendProcessingTime() {
        return backendProcessingTime;
    }

    public void setBackendProcessingTime(Double backendProcessingTime) {
        this.backendProcessingTime = backendProcessingTime;
    }

    public Double getResponseProcessingTime() {
        return responseProcessingTime;
    }

    public void setResponseProcessingTime(Double responseProcessingTime) {
        this.responseProcessingTime = responseProcessingTime;
    }

    public Integer getElbStatusCode() {
        return elbStatusCode;
    }

    public void setElbStatusCode(Integer elbStatusCode) {
        this.elbStatusCode = elbStatusCode;
    }

    public Integer getBackendStatusCode() {
        return backendStatusCode;
    }

    public void setBackendStatusCode(Integer backendStatusCode) {
        this.backendStatusCode = backendStatusCode;
    }

    public Long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSslCipher() {
        return sslCipher;
    }

    public void setSslCipher(String sslCipher) {
        this.sslCipher = sslCipher;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    @Override
    public String toString() {
        return "WebLog{" +
                "timeStamp=" + timeStamp +
                ", elb='" + elb + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", backendAddress='" + backendAddress + '\'' +
                ", requestProcessingTime=" + requestProcessingTime +
                ", backendProcessingTime=" + backendProcessingTime +
                ", responseProcessingTime=" + responseProcessingTime +
                ", elbStatusCode=" + elbStatusCode +
                ", backendStatusCode=" + backendStatusCode +
                ", receivedBytes=" + receivedBytes +
                ", sentBytes=" + sentBytes +
                ", request=" + request +
                ", userAgent='" + userAgent + '\'' +
                ", sslCipher='" + sslCipher + '\'' +
                ", sslProtocol='" + sslProtocol + '\'' +
                '}';
    }

    public int compareTo(WebLog webLog){
        return this.getTimeStampInMillis().compareTo(webLog.getTimeStampInMillis());
    }
}
