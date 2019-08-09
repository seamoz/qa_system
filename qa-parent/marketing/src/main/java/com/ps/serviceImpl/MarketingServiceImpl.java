package com.ps.serviceImpl;

import com.ps.domain.*;
import com.ps.mapper.MaketingMapper;
import com.ps.service.MarketingService;
import com.ps.service.RegisterService;
import com.ps.util.MyUtils;
import com.ps.util.checkData;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


@Service(version = "1.0.0")
public class MarketingServiceImpl implements MarketingService {

   @Autowired
    private MaketingMapper maketingMapper;

    @Autowired
    private  MarketingService marketingService;

    @Reference(version = "1.0.0")
    private RegisterService registerService;

    /**
     * kafka消息队列
     */
    @Autowired
    private  KafkaTemplate kafkaTemplate;

    /**
     * redis
     */
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 线程池
     */
    @Autowired
    private ExecutorService executorService;


    public static Logger logger = LoggerFactory.getLogger(MarketingServiceImpl.class);

    private Result result = new Result();


    /**
     * 问卷调查(创建问卷调查)
     * @param questionnaire
     * @return
     */
    @Override
    public Result creatQuestionnaire(Questionnaire questionnaire) {
        //创建问卷标题 ,给出积分
        maketingMapper.creatQuestionnaire(questionnaire);
        //返回主键
        int survey_id = questionnaire.getId();

        //创建调查内容
        Content content = new Content();
        Integer questionnaireContent =null;

        for (int i = 0; i < questionnaire.getContents().size() ; i++) {

            content.setNumber(questionnaire.getContents().get(i).getNumber());//存问题序号
            content.setQuestion(questionnaire.getContents().get(i).getQuestion());//问题
            content.setId(survey_id); //内容表id

            questionnaireContent = maketingMapper.addQuestionnaireContent(content);
        }

        if (questionnaireContent == null){

            result.setMsg("问卷创建失败");
            return result;
        }
        return new Result("问卷创建成功",1000);
    }


    /**
     * 用户进行填写调查表
     * @param resultQuestionnaire
     * @return
     */
    @Override
    public Result checkUser(ResultQuestionnaire resultQuestionnaire) {

        //检查用户是否已经填写调查表
        ResultQuestionnaire resultQuestionn = maketingMapper.checkUser(resultQuestionnaire);
       if (resultQuestionn != null){
            return new Result("你以填写过了",100);
       }

       //没填写就增加
        Integer addRecord = maketingMapper.addRecord(resultQuestionnaire);
       if (addRecord == null){
            return new Result("提交失败",10);
       }
       //增加后查出积分
        Integer integral = maketingMapper.checkIntegral(resultQuestionn.getProject_id());

        //赠送积分
        Integer updateIntegral = registerService.updateIntegral(resultQuestionn.getMid(), integral);
        if (updateIntegral == null){
            return new Result("赠送积分失败",101);
        }

        //增加积分流水
        Integer addIntegral = registerService.addIntegral(resultQuestionn.getMid(), integral);
        if (addIntegral == null){
            return new Result("增加积分流水失败",102);
        }

        return new Result("填写完成",1000);
    }


    /**
     * 送积分(搞活动送)
     */
    @KafkaListener(topics = "register")
    public void listenT1(ConsumerRecord<?, ?> cr){
        //int memberId =  JSONObject.toJSON(cr.value()).toString();

        int memberId = Integer.valueOf(cr.value().toString());
        //Question question = JSONObject.parseObject(value,Question.class);

        String start = "2019.7.25 00:00:00";
        String end  = "2019.8.25 23:59:59";

        if ("self".equals(cr.key())){
            registerService.updateIntegral(memberId,100);

        }else if("invite".equals(cr.key())){
            if (MyUtils.getTimeDifference(start,System.currentTimeMillis()) < 0 &&
                    MyUtils.getTimeDifference(end,System.currentTimeMillis()) > 0){

                registerService.updateIntegral(memberId,100*2);

            }
        }
        logger.info("{} ‐ {} : {}", cr.topic(), cr.key(), cr.value());
    }


    /**
     * 积分兑换商品活动
     * @param member_id
     * @param id
     * @return
     */
    @Override
    public Result pointExchangeActivity(int member_id, int id) {

        /**
         *根据用户ID查询用户
         */
        Member member = registerService.queryMember(member_id);

        /**
         * 根据商品ID 查询商品信息
         */
        Thing thing = maketingMapper.queryThing(id);

        /**
         * 校验用户 （空 ， 黑名单)
         */
        if (member == null){
            return new Result("用户名不存在",12);
        }

        /**
         * 校验用户积分是否足够
         */
        if (member.getMemberIntegral() < thing.getExchange_integral()){
            return new Result("你积分不够",1);
        }

        /**
         * 校验商品 (库存）
         */
        Integer hand = maketingMapper.checkKu(id);
        if (hand == 0 ){
            return new Result("库存没了",100);
        }

        /**
         * 校验商品 (时间）
         */
        if (!checkData.check(thing.getStart_time(),thing.getEnd_time())){
            return new Result("活动过期了",100);
        }

        /**
         *  校验用户是否已经兑换过
         */
        Integer memberExchange = maketingMapper.checkMemberExchange(member_id);
        System.out.println("是否已经兑换过:"+memberExchange);
        if ( memberExchange != null){
            return new Result("你已经兑换过了",100);
        }

        /**
         * 减库存
         */
        Integer integer = maketingMapper.reduceCount(id,thing.getThing_count()-1,thing.getVersion());
        if(integer == null){

            /**
             * 扣了积分,没拿到商品(退回积分)
             */
            Map<String,Object> map = new HashMap<>();
            map.put("id",member_id);
            map.put("integral",thing.getExchange_integral());
            kafkaTemplate.send("seckillTopic",map);

            return new Result("没有库存了,下次再来吧",100);
        }

        /**
         * 乐观锁(加了--版本号)
         */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 如果没兑换就生成订单(减去用户积分)
         */
        maketingMapper.insertOrder(member_id);
        registerService.reduceIntegral(member_id,thing.getExchange_integral());
        return new Result("生成订单成功",100);

    }


    /**
     * 防止大量请求刷流量(限制流量)
     * @param member_id
     * @param id
     * @return
     */
    @Override
    public Result bigBrushFlow(int member_id, int id) {
        //用户请求
        String memberQue = "user:limit:A"+member_id;
        //获取key
        String value  = redisTemplate.opsForValue().get(memberQue);

        /**
         * redis中没有记录(2019.8.5)
         */
        if (value == null){
            //排队
            boolean queue = exchangeQueue(member_id,id);

            if(queue){
                /**
                 *  排队成功(等待时间一天)
                 */
                redisTemplate.opsForValue().set(memberQue,"2",1,TimeUnit.DAYS);
                value="2";
            } else {
                /**
                 * 排队失败
                 */
                redisTemplate.opsForValue().set(memberQue, "1", 1, TimeUnit.SECONDS);
                value="1";
            }
        }

        if (value.equals("1")){
            return new Result("人数太多,稍后再试",13);
        }
        if (value.equals("2")){
            return new Result("排队成功",12);
        }


        /**
         * 存在key(返回成功,2019.8.4)
         */
        if(value != null){
            return new Result("请求成功",1);
        }

        /**
         *不存在(调用秒杀方法)并写入redis(过期时间为5秒钟)
         */
        marketingService.pointExchangeActivity(member_id,id);
        redisTemplate.opsForValue().set(memberQue, String.valueOf(member_id),5,TimeUnit.SECONDS);

        return new Result("秒杀成功",2);
    }


    /**
     * 兑换商品队列
     */
    public boolean exchangeQueue(int member_id,int id){
        long size = redisTemplate.opsForList().size("active_exChange");
        if(size >= 10000) {
            return false;
        }
        redisTemplate.opsForList().leftPush("active_exChange", member_id+"_"+id); // 返回值的判断
        return true;
    }


    /**
     * 多线程
     */
    @PostConstruct
    @Scheduled(cron = "0/5 * * * * ?")
    public void seckillGoods() throws InterruptedException {
        while (true){
            if(redisTemplate.opsForList().size("active_exChange") == 0){
             return;
            }

            /**
             *  一秒处理一次
             */
            Thread.sleep(1000);

            /**
                 * 开始消费(多线程)
                 */
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    String key = redisTemplate.opsForList().leftPop("active_exChange");
                    System.out.println("---------"+key+"------");

                    String member_id =  key.split("_")[0];
                    String stop_id =  key.split("_")[1];
                    System.out.println("用户member_id:"+member_id+"-----"+"商品id"+stop_id);



                    /**
                     * 调用秒杀方法
                     */
                    Result result = this.bigBrushFlow(Integer.valueOf(member_id), Integer.valueOf(stop_id));
                    System.out.println("调用秒杀方法: "+result);

                    /**
                     * 每正常处理一个消息,(手动移除)
                     */
                   redisTemplate.opsForValue().set("active_exChange","3");
                });
            }
        }
    }
}

