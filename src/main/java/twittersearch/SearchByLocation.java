package twittersearch;


import java.io.PrintWriter;
import java.util.logging.Logger;
import twitter4j.FilterQuery;

import twitter4j.StatusListener;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

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
        
        public static final String yourAccessToken="935448704802226177-DnVBSxS98pImTztRgd94hyOUGpH6hYT";
        public static final String yourAccessTokenSecret="xv1nKCSayygzv6ucZJzdhHNRxgDsI04EDi84xGFwkjlAw";
        public static final String yourConsumerKey="DDG2KsFSMaiza1w1QkRGySR8U";
        public static final String yourConsumerKeySecret="uX7oQqJoR8fvaAmNYVgV6tL4TddgmnL4u7l75UKItdp48Opom6";
	
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
              
                double[][] locations = {{-3.751144, 40.37872}, {-3.638535, 40.475942}}; //Madrid
                
                //double[][] locations = {{11.451874,48.073262}, {11.705933,48.201566}}; //Munich
                
                fq.locations(locations);
                String[] language = {"en"}; // English
                //String[] language = {"es"}; //Spanish
                fq.language(language);
                twitterStream.addListener(listener);
                twitterStream.filter(fq);		
	}
}
