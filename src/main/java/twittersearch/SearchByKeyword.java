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
public class SearchByKeyword {

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
		
		
		System.out.println("*********************************************");
		System.out.println("***** search for tweets using keywords ******");
		
		String queryString = "BBC";
                File outDir=new File("./data/keywords/"+queryString);
                System.out.println(outDir.getAbsolutePath());
                if(!outDir.exists()) outDir.mkdir();
                
		Query query = new Query(queryString);
                int top=10;
		query.count(top); // sets the number of tweets to return per page, up to a max of 100
	        QueryResult result;
		try {
			result = twitter.search(query);
			Integer countTw = 1;
                        
			System.out.println("Query result for " + queryString + ":");
			for (Status status : result.getTweets()) {
                            pw=new PrintWriter(new FileWriter(outDir+
                                    File.separator+"tweet_"+countTw+".json"));
                            System.out.println(countTw++ + " > @" + status.getUser().getScreenName() + " (" + status.getCreatedAt().toString() + ") : " + status.getText() + "\n");
                            String json=TwitterObjectFactory.getRawJSON(status);

                            pw.print(json);
                            pw.flush();
                            pw.close();
                            System.out.println(json);
                        }
		} catch (TwitterException e) {
			logger.info("Exception while searching for tweets by a query string: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException ex) {
                Logger.getLogger(SearchByKeyword.class.getName()).log(Level.SEVERE, null, ex);
            }
	    
        
        
		
		
                    
               

		
	}

}
