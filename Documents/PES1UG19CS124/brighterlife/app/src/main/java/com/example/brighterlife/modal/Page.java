package com.example.brighterlife.modal;

public class Page {
    private String title;
    private String content;
    private Integer imageId;
    public Page(){}
    public Page(String title,String content,Integer imageId){
        this.title=title;
        this.content=content;
        this.imageId=imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
