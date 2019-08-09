package com.ps.service;

import com.ps.domain.Member;
import com.ps.domain.Result;


public interface RegisterService {

    //注册
    Integer register(Member member);

    //查询用户积分流水
    Result queryIntegral(int memberId);

    //赠送积分给用户
    Integer updateIntegral(int memberId,int integral);

    //  增加积分流水
    Integer addIntegral(int memberId,int integral);

    //邀请用户注册送积分
    Result invitedUsers(int memberId,int integral);

    //根据ID查询用户
    Member queryMember(int memberId);

    //减掉用户的积分
    Integer reduceIntegral(int memberId,int integral );



}
