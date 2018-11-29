# WeblogChallenge
This is an interview challenge for Paytm Labs. Please feel free to fork. Pull Requests will be ignored.

The challenge is to make make analytical observations about the data using the distributed tools below.

## Solution:

### Assumptions:
1. Every unique combination of IP and port number is considered a unique client/user.
2. The user inactivity window is taken as 15 minutes.
3. Session time has been defined as the difference between the timestamps of the first and last logs for that session. So in case a session has only one log (request), the session time will be 0 millisecond.
4. Lines that did not match the regex are ignored. 22 such lines were found in this dataset.

### Results:
1. Average session time : 14487.58 milliseconds
2. Session times for top 10 engaged users: 

            | Client                | Session time (ms) |
            | ------------------    | ----------------- |
            | 213.239.204.204:35094 | 2065587           |
            | 103.29.159.138:57045  | 2065406           |
            | 203.191.34.178:10400  | 2065346           |
            | 78.46.60.71:58504     | 2064862           |
            | 54.169.191.85:15462   | 2061688           |
            | 103.29.159.186:27174  | 2060874           |
            | 122.169.141.4:11486   | 2060130           |
            | 122.169.141.4:50427   | 2059096           |
            | 103.29.159.62:55416   | 2058993           |
            | 103.29.159.213:59453  | 2058531           |
## Processing & Analytical goals:

1. Sessionize the web log by IP. Sessionize = aggregrate all page hits by visitor/IP during a session.
    https://en.wikipedia.org/wiki/Session_(web_analytics)

2. Determine the average session time

3. Determine unique URL visits per session. To clarify, count a hit to a unique URL only once per session.

4. Find the most engaged users, ie the IPs with the longest session times

## Additional questions for Machine Learning Engineer (MLE) candidates:
1. Predict the expected load (requests/second) in the next minute

2. Predict the session length for a given IP

3. Predict the number of unique URL visits by a given IP

## Tools allowed (in no particular order):
- Spark (any language, but prefer Scala or Java)
- Pig
- MapReduce (Hadoop 2.x only)
- Flink
- Cascading, Cascalog, or Scalding

If you need Hadoop, we suggest 
HDP Sandbox:
http://hortonworks.com/hdp/downloads/
or 
CDH QuickStart VM:
http://www.cloudera.com/content/cloudera/en/downloads.html


### Additional notes:
- You are allowed to use whatever libraries/parsers/solutions you can find provided you can explain the functions you are implementing in detail.
- IP addresses do not guarantee distinct users, but this is the limitation of the data. As a bonus, consider what additional data would help make better analytical conclusions
- For this dataset, complete the sessionization by time window rather than navigation. Feel free to determine the best session window time on your own, or start with 15 minutes.
- The log file was taken from an AWS Elastic Load Balancer:
http://docs.aws.amazon.com/ElasticLoadBalancing/latest/DeveloperGuide/access-log-collection.html#access-log-entry-format



## How to complete this challenge:

A. Fork this repo in github
    https://github.com/PaytmLabs/WeblogChallenge

B. Complete the processing and analytics as defined first to the best of your ability with the time provided.

C. Place notes in your code to help with clarity where appropriate. Make it readable enough to present to the Paytm Labs interview team.

D. Complete your work in your own github repo and send the results to us and/or present them during your interview.

## What are we looking for? What does this prove?

We want to see how you handle:
- New technologies and frameworks
- Messy (ie real) data
- Understanding data transformation
This is not a pass or fail test, we want to hear about your challenges and your successes with this particular problem.
