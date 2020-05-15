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

    public void setTicket(String name) {
        this.tweetdata.setString("ticket", name);
    }

    public String getTicket() {
        return this.tweetdata.getString("ticket");
    }

    public void setToken(String access,String secret) {
        this.tweetdata.setString("access",access);
        this.tweetdata.setString("secret",secret);
    }

    public String getAccess() {
        return this.tweetdata.getString("access");
    }

    public String getSecret() {
        return this.tweetdata.getString("secret");
    }

    public static List<String> getList() {
        File file = new File("");
        File dic = new File(file.getAbsolutePath() + File.separator + "userdata");
        List<String> str = new ArrayList<>();
        if(dic.listFiles() != null) {
            for(File f : dic.listFiles()) {
                str.add(f.getName());
            }
        }
        return str;
    }
}
