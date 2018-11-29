import model.Session;
import model.WebLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solution {

    public static void main(String[] args) {

        //Create a SparkContext to initialize
        SparkConf conf = new SparkConf().setMaster("local").setAppName("PaytmChallenge");

        // Create a Java version of the Spark Context
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the text into a Spark RDD, which is a distributed representation of each line of text
        JavaRDD<String> textFile = sc.textFile("/home/anam/paytm/WeblogChallenge/data/2015_07_22_mktplace_shop_web_log_sample.log");
        JavaPairRDD<String, List<WebLog>> clientIpToWebLogs = textFile.map(WebLog::getWebLog)
                .filter(w -> w != null && w.getClientAddress() != null) // ignoring unparsable log lines
                .mapToPair(w -> {
                    List<WebLog> session = new ArrayList<>();
                    session.add(w);
                    return new Tuple2<>(w.getClientAddress(), session);
                })
                // aggregating requests by client
                // all requests having same ip and port are assumed to belong to one client
                .reduceByKey((acc, curr) -> {
                    acc.addAll(curr);
                    return acc;
                });

        //1. Sessionization of log file per user
        JavaPairRDD<String, List<Session>> clientIPToSessions = clientIpToWebLogs
                .mapToPair(pair -> new Tuple2<>(pair._1, Session.sessionize(pair._1, pair._2)));

        //2. Calculation of average session time
        JavaRDD<Session> sessions = clientIPToSessions.flatMap(pair -> pair._2.iterator());
        Double averageSessionTime = sessions.mapToDouble(Session::getSessionTime).mean();
        System.out.println("Average time : " + averageSessionTime + " milliseconds");

        //3. Unique url visit per session
        JavaPairRDD<String, Integer> uniqueUrlCountPerSession = sessions
                .mapToPair(s -> new Tuple2<>(s.getClientAddress(), s.getUniqueUrls().size()));
        uniqueUrlCountPerSession.saveAsTextFile("/tmp/webreq/uniqueUrlPerSession");

        //4. Most engaged users
        JavaPairRDD<Long, String> sessionTimeToUsers = sessions
                .mapToPair(s -> new Tuple2<>(s.getSessionTime(), s.getClientAddress()))
                .sortByKey(false);
        List<Tuple2<Long, String>> mostEngagedUsers = sessionTimeToUsers
                .take(10);
        System.out.println("Session times for top 10 engaged users: ");
        for (Tuple2<Long, String> engagedUser : mostEngagedUsers) {
            System.out.println("Client: " + engagedUser._2 + " Session time in milliseconds: " + engagedUser._1);
        }

//        System.out.println("unparsed :" + countUnparsedLines(textFile));

    }

    private static long countUnparsedLines(JavaRDD<String> textFile) {
        return textFile.map(s -> {
            WebLog webLog = WebLog.getWebLog(s);
            if (webLog == null || webLog.getRequest() == null || webLog.getRequest().getUrl() == null) {
                return s;
            } else return null;
        }).filter(Objects::nonNull).count();
    }

}