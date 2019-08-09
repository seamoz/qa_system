package com.ps.mapper;

import com.ps.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MaketingMapper {

    List<Questionnaire> creatQuestionnaire(Questionnaire questionnaire); //增加一条问卷标题记录 (返回主键)


    Integer addQuestionnaireContent(Content content);//创建调查内容

    ResultQuestionnaire checkUser(ResultQuestionnaire resultQuestionnaire); //检查用户是否已经填写调查表

    Integer addRecord(ResultQuestionnaire resultQuestionnaire); // 增加填写记录

    Integer checkIntegral(int id); //增加后查出积分

    Thing queryThing(int id); //根据商品ID 查询商品信息

    Integer checkKu(int id);//校验商品 (库存）

    Integer checkMemberExchange(int member_id);//校验用户是否已经兑换过

    Integer reduceCount(int id,int thing_count,int version);//减少库存

    void insertOrder(int member_id);//生成订单


}
