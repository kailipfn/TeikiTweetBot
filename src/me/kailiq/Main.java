package me.kailiq;

import me.kailiq.config.Config;
import me.kailiq.listener.JoinListener;
import me.kailiq.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    private static JDA jda;
    private static String consumerKey;
    private static String secretKey;
    public static void main(String[] args) {
        Config discord = new Config("discord");
        String token = discord.getString("token");
        if (token == null) {
            System.out.println("The initial settings has started...");
            discord.setString("token", "token");
            System.out.println("Complete! Please boot again.");
        }
        else {
            if(token.equals("token")) {
                System.out.println("Invalid token!");
            }
            else {
                try {
                    jda = new JDABuilder(token).addEventListeners(new JoinListener()).addEventListeners(new MessageListener()).build();
                    jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("!help でヘルプ表示"));
                } catch (LoginException e) {
                    e.printStackTrace();
                    System.out.println("Login Failed");
                }
            }
        }
        Config twitter = new Config("twitter");
        String consumerkey = twitter.getString("consumer");
        String secretkey = twitter.getString("secret");
        if (consumerkey == null || secretkey == null) {
            twitter.setString("consumer", "consumer");
            twitter.setString("secret", "secret");
        }
        else {
            if(consumerkey.equals("consumer") || secretkey.equals("secret")) {
                System.out.println("Please setup token.");
            }
            else {
                Main.consumerKey = consumerkey;
                Main.secretKey = secretkey;
            }
        }
    }
    public static JDA getJDA() {
        return jda;
    }
    public static String getConsumerKey() {
        return consumerKey;
    }
    public static String getSecretKey() {
        return secretKey;
    }
}
