package me.kailiq.twitterbot.tweets;

import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TweetDataManager {
    public Member member;
    public TweetData tweetdata;

    public TweetDataManager(Member member) {
        this.tweetdata = new TweetData(member.getId());
        this.member = member;
    }

    public void setTweet(int a) {
        tweetdata.setInt("tweets",a);
    }

    public int getTweet() {
        if(tweetdata.get("tweets") != null) {
            return tweetdata.getInt("tweets");
        }
        return 0;
    }

    public void addTweet(String str) {
        int a = getTweet() + 1;
        setTweet(a);
        tweetdata.setString(a + "",str);
    }

    public void removeTweet(int a) {
        tweetdata.setString(a + "",null);
    }

    public String getTweet(int a) {
        return tweetdata.getString(a + "");
    }
}
