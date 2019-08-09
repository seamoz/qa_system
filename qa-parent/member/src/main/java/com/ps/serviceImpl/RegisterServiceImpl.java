package com.ps.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.domain.Member;
import com.ps.domain.Result;
import com.ps.mapper.RegisterMapper;
import com.ps.service.RegisterService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Service(version = "1.0.0")
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    private Result result = new Result();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Integer register(Member member) {
        Integer register = registerMapper.register(member);

        if (register == null){
            result.setMsg("注册失败");
            return 0;
        }

      /*  Integer userCode = registerMapper.invitedUsers(member.getUser_code());
        if (member.getUser_code() == userCode){
            registerMapper.addMemberIntegral(member.getMemberId(),member.getMemberIntegral());
        }*/
        kafkaTemplate.send("register", "self", member.getMemberId()+"");
        result.setMsg("注册成功");
        return 1;
    }


    /*邀请注册码*/
/*    public void registerCode(){


    }*/


    /*邀请用户注册送积分*/
    @Override
    public Result invitedUsers(int memberId,int integral) {
        Integer invitedUsers = registerMapper.invitedUsers(memberId,integral);

        kafkaTemplate.send("register", "invite", memberId+"");
        if (invitedUsers == null){
            result.setMsg("积分赠送失败");
            return result;
        }
        return new Result("积分赠送成功",123);
    }

    /**
     * 根据用户ID查询
     * @param memberId
     * @return
     */
    @Override
    public Member queryMember(int memberId) {
        Member member = registerMapper.queryMember(memberId);
        return member;
    }

    /**
     * 扣用户积分
     * @param memberId
     * @param integral
     * @return
     */
    @Override
    public Integer reduceIntegral(int memberId, int integral) {
        Integer integral1 = registerMapper.reduceIntegral(memberId, integral);
        return integral1;
    }


    @Override
    public Result queryIntegral(int memberId) {
        List<Member> members = registerMapper.queryIntegral(memberId);
        return new Result(members,"查询成功",1230);
    }

    //赠送积分给用户
    @Override
    public Integer updateIntegral(int memberId, int integral) {
        Integer updateIntegral = registerMapper.updateIntegral(memberId,integral);
        return updateIntegral;
    }

    //  增加积分流水
    @Override
    public Integer addIntegral(int memberId,int integral) {
        Integer addIntegral = registerMapper.addIntegral(memberId,integral);
       return addIntegral;
    }

    /**
     *监听秒杀系统活动退积分处理
     * @param cr
     */
    @KafkaListener(topics = "seckillTopic")
    public void setKafkaTemplate(ConsumerRecord cr){
        //获取主键ID
        String  value = (String) cr.value();

        JSONObject json =  JSONObject.parseObject(value);
        Integer id = (Integer) json.get("id");
        Integer integral = (Integer) json.get("integral");

        //补偿用户积分
        registerMapper.updateIntegral(id,integral);
        //增加一条流水记录
        registerMapper.addIntegral(id,integral);

    }
}
