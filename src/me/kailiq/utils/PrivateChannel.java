package me.kailiq.utils;

import me.kailiq.Main;
import me.kailiq.userdata.UserDataManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class PrivateChannel {
    public Member user;
    public UserDataManager udm;

    public PrivateChannel(Member user) {
        this.user = user;
        this.udm = new UserDataManager(user);
    }
    public TextChannel createChannel() {
        String newChannelName = RandomStringUtils.random(10,"abcdefghijklnmopqrstuvwxyz");
        udm.setTicket(newChannelName);
        return Main.getJDA().getCategoriesByName("bot",false).get(0).createTextChannel(newChannelName).complete();
    }
    public boolean hasCreated() {
        List<String> list = new ArrayList<>();
        for(GuildChannel channel : Main.getJDA().getCategoriesByName("bot",false).get(0).getChannels()) {
            list.add(channel.getName());
        }
        return list.contains(udm.getTicket());
    }
    public TextChannel getChannel() {
        String newChannelName = udm.getTicket();
        return Main.getJDA().getTextChannelsByName(newChannelName,false).get(0);
    }
}
