package me.kailiq.twitterbot;

import me.kailiq.Main;
import me.kailiq.userdata.UserDataManager;
import net.dv8tion.jda.api.entities.Member;

public class TweetBot implements Runnable {
    @Override
    public void run() {
        for(String str : UserDataManager.getList()) {
            Member member = Main.getJDA().getGuilds().get(0).getMemberById(str);
            if(member != null) {
                UserDataManager udm = new UserDataManager(member);
                if(udm.getAccess() != null && udm.getSecret() != null) {
                    TwitterBot tb = new TwitterBot(udm.getAccess(),udm.getSecret());
                    tb.setup();
                }
            }
        }
    }
}
