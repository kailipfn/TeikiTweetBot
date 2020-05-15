package me.kailiq.utils;

import me.kailiq.Main;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.ArrayList;
import java.util.List;

public class PrivateChannel {
    public Member user;

    public PrivateChannel(Member user) {
        this.user = user;
    }
    public TextChannel createChannel() {
        String newChannelName = user.getEffectiveName();
        return Main.getJDA().getCategoriesByName("bot",false).get(0).createTextChannel(newChannelName).complete();
    }
    public boolean hasCreated() {
        String newChannelName = user.getEffectiveName().toLowerCase();
        List<String> list = new ArrayList<>();
        for(GuildChannel channel : Main.getJDA().getCategoriesByName("bot",false).get(0).getChannels()) {
            list.add(channel.getName());
        }
        return list.contains(newChannelName);
    }
    public TextChannel getChannel() {
        String newChannelName = user.getEffectiveName().toLowerCase();
        return Main.getJDA().getTextChannelsByName(newChannelName,false).get(0);
    }
}
