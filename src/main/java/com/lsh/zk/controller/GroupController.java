package com.lsh.zk.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 14:57
 * @Desc:
 *
 * 1.创建Group组
 * 2.组内添加User
 * 3.配置Group组的策略
 * 4.配置User的Role角色
 * 5.
 */
@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    ZooKeeper zkClient;





}
