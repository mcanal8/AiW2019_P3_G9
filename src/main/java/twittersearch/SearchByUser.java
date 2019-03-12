/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersearch;






import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.FilterQuery;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
/**
 *
 * @author UPF
 */
public class SearchByUser {
        private static Logger logger = Logger.getLogger(SearchByKeyword.class.getName());
	public static final String yourAccessToken="213848539-HVOiCydgn5ms8ESeCWX48EaVUXWHiYR6fa0C6pmL";
        public static final String yourAccessTokenSecret="Qklg47pJabx6Dd2qeLUbaTesZkRxwq8h8j9NWfu9v8lVO";
        public static final String yourConsumerKey="GY5MSamRwo2RUn0EylXsXNiNC";
        public static final String yourConsumerKeySecret="5YH5Or4BpRP6FcgfFJZC3WY1VFhb26fOwXVq4vKnv0apDILD4L";
	public static void main(String[] args) {
		
                // PrintWriter
                PrintWriter pw;
		// 1) Instantiate a Twitter Factory
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setJSONStoreEnabled(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		
		// 2) Instantiate a new Twitter client
		// Go to https://dev.twitter.com/ to register a new Twitter App and get credentials
		Twitter twitter = tf.getInstance();
		AccessToken accessToken = new AccessToken(yourAccessToken,
                        yourAccessTokenSecret);
		twitter.setOAuthConsumer(yourConsumerKey, yourConsumerKeySecret);
		twitter.setOAuthAccessToken(accessToken);
                
                Paging pagingInstance = new Paging();
		Integer pageNum = 1;
		Integer elementsPerPage = 10;
		pagingInstance.setPage(pageNum);
		pagingInstance.setCount(elementsPerPage);
		
		long userId = 245965494; // this is the user ID of @JosepGuardiola
             
		ResponseList<Status> timeline;
		try {
			timeline = twitter.getUserTimeline(userId, pagingInstance);
			if(timeline != null && timeline.size() > 0) {
				System.out.println("Retrieved " + timeline.size() + " tweets (user ID: "  + userId + ", page: " + (pageNum - 1) + ". Tweets per page: " + elementsPerPage + ")");
				Iterator<Status> statusIter = timeline.iterator();
				Integer countTw = 1;
				while(statusIter.hasNext()) {
					Status status = statusIter.next();
					if(status != null && status.getCreatedAt() != null) {
						System.out.println(countTw++ + " > @" + status.getUser().getScreenName() + " (" + status.getCreatedAt().toString() + ") : " + status.getText() + "\n");
					}
				}
			}
		} catch (TwitterException e) {
			logger.info("Exception while searching for tweets of a user timeline: " + e.getMessage());
			e.printStackTrace();
		}
                    
        }
                
                
                
                
}
