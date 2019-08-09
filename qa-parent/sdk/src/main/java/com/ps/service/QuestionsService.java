package com.ps.service;

import com.ps.domain.Questions;
import com.ps.domain.Result;

public interface QuestionsService {

    Result queryQuestions(); //查询问题

    Result queryDetail(Questions questions); /*查询问题明细*/

    Result issueQuestion(Questions questions);  //发布问题

    Result answerQuestions(Questions questions);//回答问题

    Result acceptQuestion(Questions questions);//采纳问题


}
