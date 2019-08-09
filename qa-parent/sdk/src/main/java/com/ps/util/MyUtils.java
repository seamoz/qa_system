package com.ps.util;


import com.ps.domain.Member;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author:Lisa
 * @create:2019/06/29
 */
public class MyUtils {

    /**
     * 获取传过来的时间与打那个钱时间的时间差
     * @param time
     * @return  相差的秒
     */
    public static  float  getTimeDiff(String time){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long currentTime = System.currentTimeMillis();

        float diffTime = 0;
        try {
            diffTime = currentTime - df.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diffTime / 1000;
    }


    /**
     * 获取验证码（4位数字）
     */
    public static String getVerifyCode(){
        Random random = new Random();
        String str = "";
        for (int i=0; i<4; i++){
            str += random.nextInt(9);
        }

        return str;
    }


    /**
     * 给密码加盐，并做MD5签名
     * @return
     */
    public static String getMd5Psw(Member member) {
        Map<String,String> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj2.compareTo(obj1);
            }
        });

//        加上签名安全值
        map.put("secret_key","fasgdgfvx21324567");
        map.put("username",member.getMemberName());
        map.put("password",member.getMemberPassword());

        StringBuffer md5 = new StringBuffer();

        //降序排序


//        拼接值
        for (String key:map.keySet()) {
            md5.append(map.get(key));
        }
        return MD5Util.getMd5Code(md5.toString());
    }


    /**
     *   根据会话获取token，并解密
     */
    public static String getToken(String token, String tokenKey){
        if(token == null){
            return null;
        }

        try {
//            返回解密后的凭证
            return AESUtil.decode(tokenKey,token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取用户真实IP地址，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，
     * 取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * @param request http请求
     * @return 客户IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader( "x-forwarded-for" );
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) )
        {
            ip = request.getHeader( "Proxy-Client-IP" );
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) )
        {
            ip = request.getHeader( "WL-Proxy-Client-IP" );
        }
        if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) )
        {
            ip = request.getRemoteAddr();
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*----ip = "+ip+"----*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return ip;
    }

    /**
     * 获取两个毫秒之间的时间差
     * @param startTime  开始的时间
     * @param currentTime   当前时间
     * @return  相差的分钟
     */
    public  static  float  getTimeDifference(String startTime ,long currentTime){

        float diffTime =  currentTime - Long.valueOf(startTime);

        return diffTime/ (1000 * 60);
    }

    /**
     *  获取凭证
     * @param request
     * @param phone  手机号
     * @return
     */
    public static String getVoucher(HttpServletRequest request, String phone, String tokenKey){
        StringBuilder voucher = new StringBuilder();

//      手机号
        voucher.append(phone);
        voucher.append("&");

//      加上ip地址
        voucher.append(getIpAddress(request));
        voucher.append("&");

//      加上时间，毫秒
        voucher.append(System.currentTimeMillis());

        try {
//            返回加密后的凭证
            return  AESUtil.encode(tokenKey, voucher.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 返回前台的方法
     * @param obj 返回前台的数据
     * @return
     */
 /*   public static ResultInfo getReturn(Object obj){
        //       根据code获取对应的提示语
        String msg = ErrorCode.statusOf(0).getMsg();

        return new ResultInfo(msg, 0, obj);
    }*/
}
