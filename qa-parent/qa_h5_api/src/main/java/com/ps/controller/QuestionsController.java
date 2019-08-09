package com.ps.controller;

import com.ps.domain.Questions;
import com.ps.domain.Result;
import com.ps.service.QuestionsService;
import com.ps.util.SolrUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/h5")
public class QuestionsController {


    @Reference(version = "1.0.0")
    private QuestionsService questionsService;

    /**
     * 查询问题
     * @return
     */
    @PostMapping("/queryQuestions")
    public Result queryQuestions(){

        Result result = questionsService.queryQuestions();
        return result;
    }

    /**
     * 查询问题明细
     * @param questions
     * @return
     */
    @PostMapping("/queryDetail")
    public Result queryDetail(@RequestBody Questions questions){

        Result result = questionsService.queryDetail(questions);
        return result;
    }

    /**
     * 发布问题
     * @param questions
     * @return
     */
    @RequestMapping("/issueQuestion")
    public Result issueQuestion(@RequestBody Questions questions){

        Result result  = questionsService.issueQuestion(questions);
        return result;
    }

    /**
     * 回答问题
     * @param questions
     * @return
     */
    @RequestMapping("/answerQuestions")
    public Result answerQuestions(@RequestBody Questions questions){

        Result result = questionsService.answerQuestions(questions);
        return result;
    }

    /**
     * 采纳问题
     * @param questions
     * @return
     */
    @RequestMapping("/acceptQuestion")
    public Result acceptQuestion(@RequestBody Questions questions){

        Result result = questionsService.acceptQuestion(questions);
        return result;
    }


    @RequestMapping("/querySolr")
    public void querySolr() throws IOException, SolrServerException {
        SolrUtil solrUtil = new SolrUtil();
        List<Questions> like = solrUtil.querySolr("喜欢");
        for (Questions questions : like) {
            System.out.println(questions);
        }
    }


}
