package com.devarshi.Retrofitclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilteredData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("video_webm")
    @Expose
    private String videoWebm;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("is_hot")
    @Expose
    private Boolean isHot;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("watermark_position")
    @Expose
    private String watermarkPosition;
    @SerializedName("total_duration")
    @Expose
    private Integer totalDuration;
    @SerializedName("part_duration")
    @Expose
    private Integer partDuration;
    @SerializedName("watermark_type")
    @Expose
    private String watermarkType;
    @SerializedName("watermark_image")
    @Expose
    private String watermarkImage;
    @SerializedName("has_text")
    @Expose
    private Integer hasText;
    @SerializedName("is_premium")
    @Expose
    private Integer isPremium;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("activate")
    @Expose
    private Integer activate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("video_url_webm")
    @Expose
    private String videoUrlWebm;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("zip_url")
    @Expose
    private String zipUrl;
    @SerializedName("is_new")
    @Expose
    private Boolean isNew;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoWebm() {
        return videoWebm;
    }

    public void setVideoWebm(String videoWebm) {
        this.videoWebm = videoWebm;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getWatermarkPosition() {
        return watermarkPosition;
    }

    public void setWatermarkPosition(String watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getPartDuration() {
        return partDuration;
    }

    public void setPartDuration(Integer partDuration) {
        this.partDuration = partDuration;
    }

    public String getWatermarkType() {
        return watermarkType;
    }

    public void setWatermarkType(String watermarkType) {
        this.watermarkType = watermarkType;
    }

    public String getWatermarkImage() {
        return watermarkImage;
    }

    public void setWatermarkImage(String watermarkImage) {
        this.watermarkImage = watermarkImage;
    }

    public Integer getHasText() {
        return hasText;
    }

    public void setHasText(Integer hasText) {
        this.hasText = hasText;
    }

    public Integer getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Integer isPremium) {
        this.isPremium = isPremium;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getActivate() {
        return activate;
    }

    public void setActivate(Integer activate) {
        this.activate = activate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrlWebm() {
        return videoUrlWebm;
    }

    public void setVideoUrlWebm(String videoUrlWebm) {
        this.videoUrlWebm = videoUrlWebm;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

}

