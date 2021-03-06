package com.coolwallpaper.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "tb_local_picture".
 */
public class LocalPicture implements java.io.Serializable {

    private Long id;
    private String name;
    private String path;
    private java.util.Date crateTime;
    private Long size;

    public LocalPicture() {
    }

    public LocalPicture(Long id) {
        this.id = id;
    }

    public LocalPicture(Long id, String name, String path, java.util.Date crateTime, Long size) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.crateTime = crateTime;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public java.util.Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(java.util.Date crateTime) {
        this.crateTime = crateTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
