package me.kailiq.listener;

import me.kailiq.utils.PrivateChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
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
        }
    }

}
