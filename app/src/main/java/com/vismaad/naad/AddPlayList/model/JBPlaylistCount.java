package com.vismaad.naad.AddPlayList.model;

import java.io.Serializable;

/**
 * Created by satnamsingh on 19/07/18.
 */

public class JBPlaylistCount implements Serializable {


    /**
     * name : testt
     * shabads_count : 1
     */

    private String name;
    private String shabads_count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShabads_count() {
        return shabads_count;
    }

    public void setShabads_count(String shabads_count) {
        this.shabads_count = shabads_count;
    }
}
