package com.vismaad.naad.rest.model.JBfeedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JBFeedback {
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("username")
    @Expose
    private String username;


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JBFeedback(String feedback, String username) {
        this.feedback = feedback;
        this.username = username;

    }
}
