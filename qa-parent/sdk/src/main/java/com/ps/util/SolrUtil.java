package com.ps.util;

import com.ps.domain.Questions;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolrUtil {

    /**
     * 对象绑定
     * @throws IOException
     * @throws SolrServerException
     * @return
     */
    public List<Questions> querySolr(String value) throws IOException, SolrServerException {

        //构建一个客户端
        HttpSolrClient httpSolrClient = new HttpSolrClient.Builder("http://127.0.0.1:8983/sorl").build();

        //参数
        Map<String,String> map = new HashMap();
        map.put("q","like:"+value+"");

        QueryResponse response = httpSolrClient.query("qa",new MapSolrParams(map));

        List<Questions> solrQuestions = response.getBeans(Questions.class);

       return solrQuestions;

    }

}
