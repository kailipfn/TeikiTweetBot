package me.kailiq.listener;

import me.kailiq.utils.PrivateChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Role role = event.getJDA().getRolesByName("member",false).get(0);
        event.getGuild().addRoleToMember(event.getMember(),role).queue();
        TextChannel channel = event.getJDA().getTextChannelsByName("join",false).get(0);
        channel.sendMessage(event.getMember().getAsMention() + " が参加しました！").queue();
        if(event.getMember().getUser().hasPrivateChannel()) {
            net.dv8tion.jda.api.entities.PrivateChannel pc = event.getMember().getUser().openPrivateChannel().complete();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("定期ツイートBot管理サーバーへようこそ！");
            embed.setColor(Color.YELLOW);
            embed.addField("","!help でコマンドが確認できます！",false);
            embed.addField("","利用お待ちしております。",false);
            pc.sendMessage(embed.build()).queue();
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        PrivateChannel pc = new PrivateChannel(event.getMember());
        if(pc.hasCreated()) {
            pc.getChannel().delete().queue();
            System.out.println("as");
        }
    }
}
