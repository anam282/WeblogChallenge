import model.Session;
import model.WebLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main {

    public static final Long USER_INACTIVITY_WINDOW = 900000L;

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

    public static void main(String[] args) {

        //Create a SparkContext to initialize
        SparkConf conf = new SparkConf().setMaster("local").setAppName("PaytmChallenge");

        // Create a Java version of the Spark Context
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the text into a Spark RDD, which is a distributed representation of each line of text
        JavaRDD<String> textFile = sc.textFile("/home/anam/IdeaProjects/PaytmChallenge/src/main/resources/2015_07_22_mktplace_shop_web_log_sample.log");
        JavaPairRDD<String, List<WebLog>> clientIpToRequest = textFile.map(s -> WebLog.getWebLog(s)
        ).filter(w -> w != null && w.getClientAddress() != null
        ).mapToPair(w -> {
                    List<WebLog> session = new ArrayList<>();
                    session.add(w);
                    return new Tuple2<>(w.getClientAddress(), session);
                }
        ).reduceByKey((a, b) -> {
            a.addAll(b);
            return a;
        });

        //list of log files or request url that did not match the regex and hence could not be parsed
        JavaRDD<String> unparsedLogs = textFile.map(s -> {
            WebLog webLog = WebLog.getWebLog(s);
            if (webLog == null || webLog.getRequest() == null) {
                return s;
            } else return null;
        }).filter(s -> s != null);
        System.out.println("Unparsed Files Count: " + unparsedLogs.count());
        unparsedLogs.saveAsTextFile("/tmp/webreq/unparsed");

        //1. Sessionization of log file per user
        JavaPairRDD<String, List<Session>> clientIPToSessions = clientIpToRequest
                .mapToPair(w -> new Tuple2<>(w._1, sessionize(w._1, w._2)));

        //2. Calculation of average session time
        JavaRDD<Session> sessions = clientIPToSessions.flatMap(w -> w._2.iterator());
        Double averageSessionTime = sessions.mapToDouble(w -> w.getSessionTime()).mean();
        System.out.println("Average time : " + averageSessionTime + " miliseconds");

        //3. Most engaged users
        JavaPairRDD<Long, String> sessionTimeToUsers = sessions
                .mapToPair(s -> new Tuple2<>(s.getSessionTime(), s.getClientAddress()))
                .sortByKey(false);
        List<Tuple2<Long, String>> mostEngagedUsers  = sessionTimeToUsers
                .take(10);
        System.out.println("Session time with most engaged users: " + mostEngagedUsers);

        //4. Unique url visit per session
        JavaPairRDD<String, Set<String>> uniqueUrlsPerSession = sessions
                .mapToPair(s -> new Tuple2<>(s.getClientAddress(), s.getUniqueUrls()));
        JavaPairRDD<String, Integer> uniqueUrlCountPerSession = uniqueUrlsPerSession
                .mapToPair(s-> new Tuple2<>(s._1, s._2.size()));
        uniqueUrlCountPerSession.saveAsTextFile("/tmp/webreq/uniqueUrls");
    }

}