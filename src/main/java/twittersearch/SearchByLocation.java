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
import static twittersearch.SearchByUser.yourAccessToken;
import static twittersearch.SearchByUser.yourAccessTokenSecret;
import static twittersearch.SearchByUser.yourConsumerKey;
import static twittersearch.SearchByUser.yourConsumerKeySecret;

/**
 * This is an example class that shows how to exploit the Twitter4J java library to interact with Twitter
 * 
 * Twitter4j: http://twitter4j.org/en/index.html
 * Download (version 4.0.1):http://twitter4j.org/archive/twitter4j-4.0.1.zip
 * JavaDoc: http://twitter4j.org/javadoc/index.html
 * Example code of Twitter4j: http://twitter4j.org/en/code-examples.html
 * 
 * @author Francesco Ronzano
 *
 */
public class SearchByLocation {

	private static Logger logger = Logger.getLogger(SearchByLocation.class.getName());
        
        public static final String yourAccessToken="213848539-HVOiCydgn5ms8ESeCWX48EaVUXWHiYR6fa0C6pmL";
        public static final String yourAccessTokenSecret="Qklg47pJabx6Dd2qeLUbaTesZkRxwq8h8j9NWfu9v8lVO";
        public static final String yourConsumerKey="GY5MSamRwo2RUn0EylXsXNiNC";
        public static final String yourConsumerKeySecret="5YH5Or4BpRP6FcgfFJZC3WY1VFhb26fOwXVq4vKnv0apDILD4L";
	
	public static void main(String[] args) {
		
                 // PrintWriter
                 PrintWriter pw;
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setJSONStoreEnabled(true);
		
		    cb.setDebugEnabled(true)
	            .setOAuthConsumerKey(yourConsumerKey)
	            .setOAuthConsumerSecret(yourConsumerKeySecret)
	            .setOAuthAccessToken(yourAccessToken)
	            .setOAuthAccessTokenSecret(yourAccessTokenSecret);

		
		
		
		
		
                 // create a stream to listen new tweets
                 TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

                // implements the interface Status Listener
                // (the paramether is the file path where the tweets will be saved)
                StatusListener listener = new twittersearch.MyStatusListener("data");

                // designs the query to filter the tweets in the stream 
                FilterQuery fq = new FilterQuery();

                // -- filter specific geografic area
                double[][] locations = {{0.440658, 40.885126}, {3.731063, 42.367324}}; //catalunia
                fq.locations(locations);


                twitterStream.addListener(listener);
                twitterStream.filter(fq);

               

		
	}

}
