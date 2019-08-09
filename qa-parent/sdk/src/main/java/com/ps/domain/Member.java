package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Member implements Serializable,Cloneable{

    /** 会员id */
    private Integer memberId ;

    /** 会员名 */
    private String memberName ;

    /** 会员密码 */
    private String memberPassword ;

    /** 积分 */
    private Integer memberIntegral ;

    /*会员邀请码*/
    private  int user_code;


}