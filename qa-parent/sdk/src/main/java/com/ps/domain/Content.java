package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Content implements Serializable {

    private int id ;//问卷内容id

    private int questionnaire_id;//问卷id

    private int number; //序号

    private String question;//问题

}
