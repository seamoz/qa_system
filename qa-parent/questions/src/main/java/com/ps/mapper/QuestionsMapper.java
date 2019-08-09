package com.ps.mapper;

import com.ps.domain.Questions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionsMapper {

    //查询问题
    List<Questions> queryQuestions();

    /*查询问题明细*/
    List<Questions> queryDetail(Questions questions);

    //发布问题
    Integer issueQuestion(Questions questions);

    //增加积分流水记录
    Integer  addIntegrlWater(Questions questions);

    //减去提问者积分
    Integer minusIssue(Questions questions);

    // 回答问题
    Integer answerQuestion(Questions questions);

    //采纳问题
    Integer acceptQuestion(Questions questions);

    //采纳问题之后修改积分
    void updaupdateIntegral(Questions questions);

    //增加积分流水记录
    Integer  acceptIntegral(Questions questions);


}
