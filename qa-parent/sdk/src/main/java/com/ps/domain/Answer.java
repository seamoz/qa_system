package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Answer implements Serializable {

    private int answer_id;//回答id

    private int member_id;//会员id

    private int issue_id;// 问题id

    private String answer_content;//回答内容

}
