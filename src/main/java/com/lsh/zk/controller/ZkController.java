package com.lsh.zk.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 14:57
 * @Desc:
 */
@Slf4j
@RestController
@RequestMapping("/zk")
public class ZkController {

    @Autowired
    CuratorFramework curatorClient;

    @GetMapping("/createZNode/{path}")
    public String createZNode(@PathVariable("path") String path) throws Exception {
        String result = "";
        result = curatorClient.create().forPath("/"+path);
        log.info("curator create node :{} successfully.", result);
//        try {
//            result = curatorClient.create().forPath(path);
//            log.info("curator create node :{} successfully.", result);
//        }catch (Exception e){
//            log.error("curator create node :{} fail.", result);
//        }
        return result;
    }
}
