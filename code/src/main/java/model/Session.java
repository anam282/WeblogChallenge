package model;

import java.util.*;

public class Session {

    // 15 minutes in milliseconds
    public static final Long USER_INACTIVITY_WINDOW = 900000L;

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
        //this is the time at which the first request of this session is received by the load balancer
        sessionStartTime = Math.min(sessionStartTime, webLog.getTimeStampInMillis());

        //this is the time at which the last request of this session is received by the load balancer
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

    /**
     * Takes weblogs of a client and converts to list of sessions
     * Uses a time window approach with user inactivity window set to 15 minutes
     *
     * @param clientAddress
     * @param webLogs
     * @return
     */
    public static List<Session> sessionize(String clientAddress, List<WebLog> webLogs) {
        Collections.sort(webLogs);
        List<Session> sessions = new ArrayList<>();
        if (webLogs.isEmpty()) return sessions;
        Long previousRequestTime = 0L;
        Session session = null;
        for (WebLog webLog : webLogs) {
            if (session == null || webLog.getTimeStampInMillis() - previousRequestTime >= USER_INACTIVITY_WINDOW) {
                session = new Session(clientAddress);
                sessions.add(session);
            }
            session.addWebLog(webLog);
            previousRequestTime = webLog.getTimeStampInMillis();
        }
        return sessions;
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
