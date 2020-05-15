package me.kailiq.listener;

import me.kailiq.utils.PrivateChannel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        User user = event.getAuthor();
        Member member = event.getMember();
        String content = message.getContentDisplay();

        if (user.isBot()) {
            return;
        }
        if (channel.getName().equals("commands")) {
            message.delete().queue();

            if (content.equals("!create")) {
                PrivateChannel pc = new PrivateChannel(member);
                if (!pc.hasCreated()) {
                    if (member != null) {
                        TextChannel tc = pc.createChannel();
                        Message m = channel.sendMessage("専用テキストチャンネルを作成しました！" + tc.getAsMention() + " からご確認ください。").complete();
                        m.delete().queueAfter(10,TimeUnit.SECONDS);
                        Role role = event.getJDA().getRolesByName("member", false).get(0);
                        tc.putPermissionOverride(role).setDeny(Permission.VIEW_CHANNEL).queue();
                        tc.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).queue();
                    }
                } else {
                    Message m = channel.sendMessage("すでにあなたのBot管理チャンネルは存在しています！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                }
            } else {
                Message m = channel.sendMessage("このチャンネルでそのコマンドは送信できません！").complete();
                m.delete().queueAfter(5,TimeUnit.SECONDS);
            }
            if (content.equals("!delete")) {
                PrivateChannel pc = new PrivateChannel(member);
                if (pc.hasCreated()) {
                    TextChannel ch = pc.getChannel();
                    ch.delete().queue();
                    Message m = channel.sendMessage("削除に成功しました！").complete();
                    m.delete().queueAfter(4,TimeUnit.SECONDS);
                } else {
                    Message m = channel.sendMessage("あなたのBot管理チャンネルは存在していません！").complete();
                    m.delete().queueAfter(5,TimeUnit.SECONDS);
                }
            }
        }
    }

}
