package com.nsl.app.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sys on 5/27/2017.
 */

public class RejectionCommentVo {

    @SerializedName("comments")
    @Expose
    public String comments;
    @SerializedName("comments_given_by")
    @Expose
    public String commentsGivenBy;
    @SerializedName("comments_given_by_id")
    @Expose
    public String commentsGivenById;
    @SerializedName("created_date")
    @Expose
    public String createdDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
