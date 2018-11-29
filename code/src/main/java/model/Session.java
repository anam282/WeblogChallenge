package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Session {

    private String clientAddress;
    private List<WebLog> weblogs;
    private Long sessionStartTime;
    private Long sessionEndTime;
    private Set<String> uniqueUrls;

    public Session(String clientAddress){
        this.clientAddress = clientAddress;
        this.weblogs = new ArrayList<>();
        this.sessionStartTime = Long.MAX_VALUE;
        this.sessionEndTime = Long.MIN_VALUE;
        this.uniqueUrls = new HashSet<>();
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public List<WebLog> getWeblogs() {
        return weblogs;
    }

    public Set<String> getUniqueUrls() {
        return this.uniqueUrls;
    }

    public void addWebLog(WebLog webLog) {
        sessionStartTime = Math.min(sessionStartTime, webLog.getTimeStampInMillis());
        sessionEndTime = Math.max(sessionEndTime, webLog.getTimeStampInMillis());
        if(webLog.getRequest() != null && webLog.getRequest().getUrl()!=null) {
            uniqueUrls.add(webLog.getRequest().getUrl());
        }
        this.weblogs.add(webLog);
    }

    public Long getSessionTime(){
        if(sessionStartTime > sessionEndTime) return null;
        return sessionEndTime - sessionStartTime;
    }

    public int getUniqueUrlCount() {
        return this.uniqueUrls.size();
    }

    @Override
    public String toString() {
        return "Session{" +
                "clientAddress='" + clientAddress + '\'' +
                ", weblogs=" + weblogs +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                '}';
    }

}
