package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultQuestionnaire implements Serializable {

    private  int result_id ; //结果表ID

    private int mid; //用户ID

    private int project_id; //问卷表ID

    private String result; //结果

}
