package com.master1590.compie_test;

/**
 * Created by master1590 on 18/01/17.
 */

public class Video {
    private String title;
    private String link;
    private String thumb;
    private String code;

    public Video(){}

    public Video(String title, String link, String thumb) {
        this.title = title;
        this.link = link;
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        try {
            setCode(link.split("v=")[1]);
        }catch (Exception e){
        }
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return title;
    }
}
