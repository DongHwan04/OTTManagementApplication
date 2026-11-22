package com.example.ottmanagementapplication;

import androidx.annotation.DrawableRes;

public class CommunityPost {
    private final String id;
    private final String serviceCategory; // "영상", "음악", "배달"
    private final String postType;        // "추천", "후기", "팁"
    private final String serviceName;
    @DrawableRes
    private final int serviceIconResId;
    private final String content;
    private final String userTag;

    public CommunityPost(String id, String serviceCategory, String postType,
                         String serviceName, @DrawableRes int serviceIconResId,
                         String content, String userTag) {
        this.id = id;
        this.serviceCategory = serviceCategory;
        this.postType = postType;
        this.serviceName = serviceName;
        this.serviceIconResId = serviceIconResId;
        this.content = content;
        this.userTag = userTag;
    }

    public String getId() { return id; }
    public String getServiceCategory() { return serviceCategory; }
    public String getPostType() { return postType; }
    public String getServiceName() { return serviceName; }
    public int getServiceIconResId() { return serviceIconResId; }
    public String getContent() { return content; }
    public String getUserTag() { return userTag; }
}