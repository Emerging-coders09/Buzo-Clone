package com.devarshi.Retrofitclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExampleTitleList {

    @SerializedName("templates")
    @Expose
    private List<String> templates = null;

    public List<String> getTemplates() {
        return templates;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }

}
