package com.ps.domain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.List;

@Data
public class Questions implements Serializable {

    @Field("issue_id")
    private int issue_id; //问题id

    @Field("memberId")
    private int memberId;// 会员id

    @Field("issueContent")
    private String issueContent;//问题内容

    @Field("answerId")
    private int answerId;//回答id

    @Field("integral")
    private int integral; // 积分

    @Field("accept_id")
    private int accept_id;//采纳id

    @Field("answer")
    private List<Answer> answer;//答案实体类对象

}
