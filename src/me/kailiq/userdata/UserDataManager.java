package me.kailiq.userdata;


import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class UserDataManager {
    public Member member;
    public UserData userdata;

    public UserDataManager(Member member) {
        this.userdata = new UserData(member.getId());
        this.member = member;
    }

    public void setTicket(String name) {
        this.userdata.setString("ticket", name);
    }

    public String getTicket() {
        return this.userdata.getString("ticket");
    }

    public void setToken(String access,String secret) {
        this.userdata.setString("access",access);
        this.userdata.setString("secret",secret);
    }

    public String getAccess() {
        return this.userdata.getString("access");
    }

    public String getSecret() {
        return this.userdata.getString("secret");
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