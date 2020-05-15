package me.kailiq.twitterbot;

import me.kailiq.Main;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBot {
    public String access;
    public String secret;
    public Twitter twitter;

    public TwitterBot(String access,String secret) {
        this.access = access;
        this.secret = secret;
    }
    public void setup() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Main.getConsumerKey())
                .setOAuthConsumerSecret(Main.getConsumerKey())
                .setOAuthAccessToken(access)
                .setOAuthAccessTokenSecret(secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public Status tweet(String string) {
        try {
            return twitter.updateStatus(string);
        }
        catch (TwitterException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
