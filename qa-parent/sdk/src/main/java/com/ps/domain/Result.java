package com.ps.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author JIANGZI
 */
@Data
public class Result<T> implements Serializable {

    //数据
    private T date;

    //消息
    private String msg;


    //错误码
    private int code;
    public Result(T date, String msg, int code) {

        this.date = date;
        this.msg = msg;
        this.code = code;
    }


    public Result(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public Result() {
    }


}
