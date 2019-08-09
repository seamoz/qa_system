package com.ps.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Questionnaire implements Serializable {

    private int id;//问卷id

    private int memberId;//会员id

    private String answer;//回答

    private int questionnaireId;//问卷调查id

    private int detail;//问卷明细id

    private int integral;//积分

    private String questionnaire_theme;//问卷主题

    private List<Content> contents;//问卷内容对象


}
