package com.ps.controller;

import com.ps.domain.Questionnaire;
import com.ps.domain.Result;
import com.ps.domain.ResultQuestionnaire;
import com.ps.service.MarketingService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JIANGZI
 */
@RestController
@RequestMapping("/h5")
public class MaketingController {

    @Reference(version = "1.0.0")
    private MarketingService marketingService;

    /**
     * 创建问卷调查
     * @param questionnaire
     * @return
     */
    @RequestMapping("/creatQuestionnaire")
    public Result creatQuestionnaire(@RequestBody Questionnaire questionnaire){

        Result result = marketingService.creatQuestionnaire(questionnaire);
        return result;

    }

    /**
     * 用户进行填写调查表
     * @param resultQuestionnaire
     * @return
     */
    @RequestMapping("/checkUser")
    public Result checkUser(@RequestBody ResultQuestionnaire resultQuestionnaire){

        Result result = marketingService.checkUser(resultQuestionnaire);
        return result;
    }

    /**
     * 积分兑换商品活动
     * @param member_id
     * @param id
     * @return
     */
    @RequestMapping("/pointExchangeActivity/{member_id}/{id}")
    public Result pointExchangeActivity(@PathVariable("member_id") int member_id,@PathVariable("id")  int id) {
        Result result = marketingService.pointExchangeActivity(member_id,id);
        return result;
    }

    /**
     * 防止大量请求刷流量(限制流量)
     * @return
     */
    @RequestMapping("/bigBrushFlow/{member_id}/{id}")
    public Result bigBrushFlow(@PathVariable("member_id") int member_id, @PathVariable("id")  int id){
        Result result = marketingService.bigBrushFlow(member_id, id);
        return result;
    }
}
