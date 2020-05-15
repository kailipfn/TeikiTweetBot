package me.kailiq.listener;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.kailiq.Main;
import me.kailiq.twitterbot.TwitterBot;
import me.kailiq.userdata.UserDataManager;
import me.kailiq.utils.PrivateChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    public static Map<Member,Boolean> map1 = new HashMap<>();
    public static Map<Member,OAuthConsumer> map2 = new HashMap<>();
    public static Map<Member,OAuthProvider> map3 = new HashMap<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if(!event.isFromGuild()) {
            return;
        }
        if (user.isBot()) {
            return;
        }
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        Member member = event.getMember();
        String content = message.getContentDisplay();

        if (channel.getName().equals("commands")) {
            message.delete().queue();

            if (content.equals("!create")) {
                PrivateChannel pc = new PrivateChannel(member);
                if (!pc.hasCreated()) {
                    if (member != null) {
                        TextChannel tc = pc.createChannel();
                        Message m = channel.sendMessage("専用テキストチャンネルを作成しました！" + tc.getAsMention() + " からご確認ください。").complete();
                        m.delete().queueAfter(5,TimeUnit.SECONDS);
                        Role role = event.getJDA().getRolesByName("member", false).get(0);
                        tc.putPermissionOverride(role).setDeny(Permission.VIEW_CHANNEL).queue();
                        tc.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queue();
                    }
                } else {
                    Message m = channel.sendMessage("すでにあなたのBot管理チャンネルは存在しています！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                }
            }
            if (content.equals("!delete")) {
                PrivateChannel pc = new PrivateChannel(member);
                if (pc.hasCreated()) {
                    TextChannel ch = pc.getChannel();
                    ch.delete().queue();
                    Message m = channel.sendMessage("削除に成功しました！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                } else {
                    Message m = channel.sendMessage("あなたのBot管理チャンネルは存在していません！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                }
            }
            if(content.equals("!get")) {
                PrivateChannel pc = new PrivateChannel(member);
                if (pc.hasCreated()) {
                    TextChannel ch = pc.getChannel();
                    Message m = channel.sendMessage("あなたのBot管理チャンネルは" + ch.getAsMention() + "です！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                } else {
                    Message m = channel.sendMessage("あなたのBot管理チャンネルは存在していません！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                }
            }
            if(content.equals("!help")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setTitle("コマンドのヘルプ！");
                embed.addField("!create","Botを管理する用のチャンネルを作成します",false);
                embed.addField("!delete","Botを管理する用のチャンネルを削除します",false);
                embed.addField("!get","Botを管理する用のチャンネルを所得します",false);
                Message m = channel.sendMessage(embed.build()).complete();
                m.delete().queueAfter(7,TimeUnit.SECONDS);
            }
            if(content.equals("!test")) {
                assert member != null;
                UserDataManager udm = new UserDataManager(member);
                TwitterBot tb = new TwitterBot(udm.getAccess(),udm.getSecret());
                tb.setup();
                tb.tweet("てすとついーとだにょ！ \n http://pic.twitter.com/oA8bWn9W8O");
            }
        }
        PrivateChannel pc = new PrivateChannel(member);
        if(channel.getName().equals(pc.getChannel().getName())) {
            if(map1.get(member) != null) {
                if (map1.get(member)) {
                    try {
                        OAuthConsumer consumer = map2.get(member);
                        OAuthProvider provider = map3.get(member);
                        provider.retrieveAccessToken(consumer, content);
                        UserDataManager udm = new UserDataManager(member);
                        udm.setToken(consumer.getToken(), consumer.getTokenSecret());
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("アカウントの追加に成功しました。");
                        embed.setColor(Color.GREEN);
                        channel.sendMessage(embed.build()).queue();
                        map1.put(member, false);
                        map2.put(member, null);
                        map3.put(member, null);
                    } catch (OAuthMessageSignerException | OAuthNotAuthorizedException | OAuthExpectationFailedException | OAuthCommunicationException e) {
                        e.printStackTrace();
                        channel.sendMessage("アカウントの追加に失敗しました。").queue();
                        map1.put(member, false);
                    }
                }
            }
            if(content.equals("!setup")) {
                OAuthConsumer consumer = new DefaultOAuthConsumer(
                        Main.getConsumerKey(),
                        Main.getSecretKey());

                OAuthProvider provider = new DefaultOAuthProvider(
                        "https://api.twitter.com/oauth/request_token",
                        "https://api.twitter.com/oauth/access_token",
                        "https://api.twitter.com/oauth/authorize");

                String authUrl = "";
                try {
                    authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("セットアップを開始します。こちらのURLを開いて表示されたPINを入力してください。");
                    embed.setColor(Color.YELLOW);
                    embed.addField("URL", authUrl, false);
                    channel.sendMessage(embed.build()).queue();
                    map1.put(member,true);
                    map2.put(member,consumer);
                    map3.put(member,provider);
                } catch (OAuthMessageSignerException | OAuthNotAuthorizedException | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
