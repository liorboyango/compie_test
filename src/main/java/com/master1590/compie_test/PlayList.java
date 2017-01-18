package com.master1590.compie_test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by master1590 on 18/01/17.
 */

public class PlayList {
    private String title;
    private ArrayList<Video> videos = new ArrayList<>();

    public PlayList(){}

    public PlayList(String title, ArrayList<Video> videos) {
        this.title = title;
        this.videos = videos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video){
        if(videos!=null&&video!=null)
            videos.add(video);
    }

    @Override
    public String toString(){
        return title;
    }
}
