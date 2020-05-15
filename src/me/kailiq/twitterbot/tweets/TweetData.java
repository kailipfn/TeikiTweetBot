package me.kailiq.twitterbot.tweets;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.Properties;

public class TweetData {
    private String name;
    private File file = new File("");
    private Properties properties;
    public TweetData(String name) {
        this.name = name;

        File dic = new File(file.getAbsolutePath() + File.separator + "tweetdata");
        File f = new File(file.getAbsolutePath() + File.separator + "tweetdata" + File.separator + name + ".yml");

        try {
            if(!dic.exists()) {
                if(dic.mkdir()) {
                }
            }
            if(!f.exists()) {
                f.createNewFile();
            }
            Properties prop = new Properties();
            InputStream istream = new FileInputStream(f);
            prop.load(istream);
            this.properties = prop;
            this.file = f;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void save(File f) {
        try {
            properties.store(new FileOutputStream(f), "設定変更");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setString(String key,String a) {
        properties.setProperty(key, a);
        save(file);
    }

    public void setInt(String key,int a) {
        properties.setProperty(key, a + "");
        save(file);
    }

    public void setBoolean(String key,boolean a) {
        properties.setProperty(key, a + "");
        save(file);
    }

    public void remove(String key) {
        properties.remove(key);
        save(file);
    }

    public Object get(String key) {
        try {
            Properties prop = properties;
            InputStream istream = new FileInputStream(file);
            prop.load(istream);
            return prop.get(key);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(String key) {
        try {
            Properties prop = properties;
            InputStream istream = new FileInputStream(file);
            prop.load(istream);
            return prop.getProperty(key);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt(String key) {
        try {
            Properties prop = properties;
            InputStream istream = new FileInputStream(file);
            prop.load(istream);
            String result = prop.getProperty(key);
            if(NumberUtils.isNumber(result)) {
                return Integer.parseInt(result);
            }
            else {
                return 0;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        try {
            Properties prop = properties;
            InputStream istream = new FileInputStream(file);
            prop.load(istream);
            String result = prop.getProperty(key);
            if(result.equals("true") || result.equals("false")) {
                return Boolean.parseBoolean(result);
            }
            else {
                return false;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    public TweetData getConfig() {
        return this;
    }

    public File getConfigFile() {
        return this.file;
    }
}
