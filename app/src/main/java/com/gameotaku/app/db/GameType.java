package com.gameotaku.app.db;

/**
 * Created by Vincent on 9/14/14.
 */
public class GameType {
    private int type_id;
    private String type_tag;
    private String type_name;
    private int type_count;

    public GameType() {
    }

    public GameType(String type_tag, String type_name) {
        this.type_tag = type_tag;
        this.type_name = type_name;
    }

    public GameType(int type_id, String type_tag, String type_name, int type_count) {
        this.type_id = type_id;
        this.type_tag = type_tag;
        this.type_name = type_name;
        this.type_count = type_count;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_tag() {
        return type_tag;
    }

    public void setType_tag(String type_tag) {
        this.type_tag = type_tag;
    }

    public int getType_count() {
        return type_count;
    }

    public void setType_count(int type_count) {
        this.type_count = type_count;
    }
}
