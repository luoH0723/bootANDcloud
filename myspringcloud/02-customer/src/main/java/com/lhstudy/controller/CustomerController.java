package com.lhstudy.controller;

import com.lhstudy.client.SearchClient;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private EurekaClient eurekaClient;

    @Autowired
    private SearchClient searchClient;

    @GetMapping("/customer")
    public String customer(){
//        //1.通过eurekaClient获取到SEARCH服务的信息
//        InstanceInfo info = eurekaClient.getNextServerFromEureka("SEARCH",false);
//        //2.获取到访问的地址
//        String url = info.getHomePageUrl();
//        System.out.println(url);
//        //3.通过restTemplate访问
//        String result = restTemplate.getForObject(url + "/search", String.class);
        /*
        //Ribbon时
        String result = restTemplate.getForObject("http://SEARCH/search",String.class);
        */

        String result = searchClient.search();
        //4.返回
        return result;
    }
}
