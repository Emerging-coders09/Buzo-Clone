
package com.devarshi.Retrofitclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("templates")
    @Expose
    private List<Template> templates = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("page_size")
    @Expose
    private String pageSize;

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
