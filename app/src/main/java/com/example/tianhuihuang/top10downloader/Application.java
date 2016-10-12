package com.example.tianhuihuang.top10downloader;

/**
 * Created by tianhuihuang on 2016-09-19.
 */
public class Application {
    private String name;
    private String artist;
    private String releaseDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    //然后在arrayAdapter里面自动更新这个函数
    public String toString(){
        return  "Name: "+getName()+"\n"+
                "Artist: "+getArtist()+"\n"+
                "Release Date: "+getReleaseDate()+"\n";
    }

}
