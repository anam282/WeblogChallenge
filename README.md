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
2. Result for Sessions per client and Unique urls per session can be found here https://www.dropbox.com/s/wtar60w90xxh6y5/webreq.tar.gz?dl=0
3. Top 10 engaged users: 

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
