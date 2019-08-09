package com.ps.mapper;

import com.ps.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jazlyn
 */
@Mapper
@Repository
public interface RegisterMapper {
    /*注册*/
    Integer register(Member member);

    /*查询用户积分流水*/
    List<Member> queryIntegral(int memberId);

    //赠送积分给用户
    Integer updateIntegral(int memberId,int integral);

    //  增加积分流水
    Integer addIntegral(int memberId,int integral);

    //邀请用户注册送积分
    Integer invitedUsers(int memberId,int integral);

    //给用户增加积分
    Integer addMemberIntegral(int memberId,int integral);

    //根据用户ID查询用户
    Member queryMember(int memberId);

    //减掉用户的积分
    Integer reduceIntegral(int memberId,int integral);


}
