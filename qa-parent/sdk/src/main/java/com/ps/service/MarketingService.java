package com.ps.service;

import com.ps.domain.Content;
import com.ps.domain.Questionnaire;
import com.ps.domain.Result;
import com.ps.domain.ResultQuestionnaire;

public interface MarketingService {

    Result creatQuestionnaire(Questionnaire questionnaire); //创建问卷调查

    Result checkUser (ResultQuestionnaire resultQuestionnaire); //检查用户是否已经填写调查表

    Result pointExchangeActivity(int member_id, int id);//积分兑换商品活动

    Result bigBrushFlow(int member_id, int id);//防止大量请求刷流量

}
