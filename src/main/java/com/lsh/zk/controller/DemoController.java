package com.lsh.zk.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/18 17:47
 * @Desc:
 * 1.创建Group组
 * 2.组内添加User
 * 3.配置Group组的策略
 * 4.配置User的Role角色
 * 5.
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class DemoController {

    @Autowired
    ZooKeeper zkClient;

    public static final String GROUP_POLICY = "/policy";

    public static final String GROUP_USERS = "/user";

    /**
     * 创建组
     * Group内有User
     * Group需要配置Policy
     * 创建节点表示一个组  /group
     * 这个节点下创建两个子节点：/policy 、/user 分别表示Policy 和 User
     * @param groupName
     * @return
     * @throws Exception
     */
    @GetMapping("/create/{groupName}")
    public String createGroup(@PathVariable("groupName") String groupName) throws Exception {
        String path = "/"+groupName;
        //节点的path为组名
        //节点的值表示配置的policy path
        zkClient.create(path,groupName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zkClient.create(path+GROUP_POLICY,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zkClient.create(path+GROUP_USERS,"[\"SL75741\"]".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        return "success";
    }

    /**
     * TODO
     * 组内添加成员
     * @param userID
     * @return
     * @throws Exception
     */
    @GetMapping("/addUser/{groupName}/{userID}")
    public String addUser(@PathVariable("groupName") String groupName,@PathVariable("userID") String userID) throws Exception {
        String path = "/"+groupName;
        Stat stat = new Stat();
        byte[] data = zkClient.getData(path+GROUP_USERS, true,stat);
        String s = new String(data);
        System.out.println(s);
        String[] split = s.split(",");
        List<String> users = Arrays.asList(split);
        users.add(userID);
        zkClient.setData(path,users.toString().getBytes(),stat.getVersion());
        return "success";
    }
    @GetMapping("/addPolicy/{policy}")
    public String addPolicy(@PathVariable("policy") String policy) throws Exception {
        return null;
    }


}
