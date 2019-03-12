package twittersearch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author UPF
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;

public class MyStatusListener implements StatusListener{
    
    private Writer out;
    private String filePath;
    private String fs;
    
    public MyStatusListener(String filePath) {
        this.filePath = filePath;
        fs = System.getProperty("file.separator"); //file separator is different for windows (\) and unix (/)
    }
    
    @Override
    public void onStatus(Status status) {
        
        Long tweetID = status.getId();
        String userName = status.getUser().getName();
        String tweetText = status.getText();
        
        // write tweet on standard output
        System.out.println("=======================");
        System.out.println("ID: "+tweetID);
        System.out.println("User: "+userName);
        System.out.println("Text: "+tweetText);
        
        try {
            //write in file [id].txt username and tweet
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath+fs+"txt"+fs+tweetID+".txt"), "UTF-8"));
            out.write(userName +": "+ tweetText);
            out.flush();
            
            //write in file [id].json the tweet raw JSON
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath+fs+"json"+fs+tweetID+".json"), "UTF-8"));
            out.write(TwitterObjectFactory.getRawJSON(status));
            out.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void onException(Exception arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onDeletionNotice(StatusDeletionNotice arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onScrubGeo(long arg0, long arg1) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onStallWarning(StallWarning arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onTrackLimitationNotice(int arg0) {
        // TODO Auto-generated method stub
        
    }
}

    

