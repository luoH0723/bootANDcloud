package com.lhstudy.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("SEARCH")  //指定服务名称
public interface SearchClient {

    //value->目标服务的请求路劲，method->映射请求方式
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    String search();

}
