package com.ps.serviceImpl;

import com.ps.domain.Questions;
import com.ps.domain.Result;
import com.ps.mapper.QuestionsMapper;
import com.ps.service.QuestionsService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JIANGZI
 */
@Service(version = "1.0.0")
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private QuestionsMapper questionsMapper;

    private Result result = new Result();
    /**
     * 查询问题
     * @return
     */
    @Override
    public Result queryQuestions() {
        List<Questions> query = questionsMapper.queryQuestions();

        if(query == null){
            result.setMsg("数据为空");
            result.setCode(1001);
            return result;
        }

        return new Result(query,"查询成功",1000);
    }

    /**
     * 查询问题明细
     * @param questions
     * @return
     */
    @Override
    public Result queryDetail(Questions questions) {
        List<Questions> queryDetail = questionsMapper.queryDetail(questions);
        if (queryDetail == null){
            result.setMsg("数据为空");
            result.setCode(123);
            return result;
        }
        return new Result(queryDetail,"查询成功",110);
    }

    /**
     * 发布问题
     * @param questions
     * @return
     */
    @Override
    public Result issueQuestion(Questions questions) {

        Integer issueQuestion = questionsMapper.issueQuestion(questions);
        if (issueQuestion == null){
            result.setMsg("发布问题失败");
            return result;
        }

        questionsMapper.minusIssue(questions);//减去提问者的积分
        questionsMapper.addIntegrlWater(questions);//增加流水记录

        return new Result("发布问题成功",100);
    }

    /**
     * 回答问题
     * @param questions
     * @return
     */
    @Override
    public Result answerQuestions(Questions questions) {
        Integer answerQuestions = questionsMapper.answerQuestion(questions);
        if (answerQuestions ==  null){
            result.setMsg("回答失败");
            return result;
        }

        return new Result(answerQuestions,"回答成功",100);
    }

    /**
     * 采纳问题
     * @param questions
     * @return
     */
    @Transactional
    @Override
    public Result acceptQuestion(Questions questions) {
        Integer acceptQuestion = questionsMapper.acceptQuestion(questions);
        if (acceptQuestion == null){
            result.setMsg("采纳问题失败");
            return result;
        }
        
        questionsMapper.updaupdateIntegral(questions);//采纳后,修改积分
        questionsMapper.acceptIntegral(questions);//增加流水记录
        return new Result(acceptQuestion,"采纳问题成功",123);
    }


}
