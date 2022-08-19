package com.lsh.zk.controller;

import com.lsh.zk.dto.EditPolicyDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    CuratorFramework curatorClient;
//
//    public static final String GROUP_POLICY = "/policy";
//
//    public static final String GROUP_USERS = "/user";

    /**
     * 创建组  1.组内添加用户  2.配置组内策略
     * @param groupName
     * @return
     * @throws Exception
     */
    @GetMapping("/create/{groupName}")
    public String createGroup(@PathVariable("groupName") String groupName) throws Exception {
        String path = "/"+groupName;
        //节点的path为组名
        //节点的值表示配置的policy path
        zkClient.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        return "success";
    }

    /**
     * 组内添加成员
     * @param userID
     * @return
     * @throws Exception
     */
    @GetMapping("/addUser/{groupName}/{userID}")
    public String addUser(@PathVariable("groupName") String groupName,@PathVariable("userID") String userID) throws Exception {
        String path = "/"+groupName+"/"+userID;
        zkClient.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        return "success";
    }

    /**
     * 获取组内所有User
     * @param groupName
     * @return
     * @throws Exception
     */
    @GetMapping("/getGroupUsers/{groupName}")
    public String getGroupUsers(@PathVariable("groupName") String groupName) throws Exception {
        String path = "/"+groupName;
        List<String> children = zkClient.getChildren(path, true);
        return children.toString();
    }

    /**
     * 删除组内User
     * @param groupName
     * @return
     * @throws Exception
     */
    @GetMapping("/getGroupUsers/{groupName}/{userID}")
    public String deleteGroupUsers(@PathVariable("groupName") String groupName,@PathVariable("userID") String userID) throws Exception {
        String path = "/"+groupName+"/"+userID;
        zkClient.delete(path,-1);
        return "success";
    }

    /**
     * 获取组内策略
     * @param groupName
     * @return
     * @throws Exception
     */
    @GetMapping("/getPolicy/{groupName}")
    public String getPolicy(@PathVariable("groupName") String groupName) throws Exception {
        String path = "/"+groupName;
        byte[] data = zkClient.getData(path, true, new Stat());
        String ans = new String(data);
        System.out.println(ans);
        return ans;
    }


    /**
     * 配置组内策略
     * @param groupName
     * @param policyID
     * @return
     * @throws Exception
     */
    @GetMapping("/setPolicy/{groupName}/{policyID}")
    public String setGroupPolicy(@PathVariable("groupName") String groupName,@PathVariable("policyID") String policyID) throws Exception {
        String path = "/"+groupName;
        //节点存储数据最大1M
        zkClient.setData(path,policyID.getBytes(),-1);
        return "success";
    }


    /**
     * 创建策略
     * @param policyID
     * @return
     * @throws Exception
     */
    @GetMapping("/createPolicy/{policyID}")
    public String editGroupPolicy(@PathVariable("policyID") String policyID) throws Exception {
        String path = "/"+policyID;
        zkClient.create(path,"".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        return "success";
    }

    /**
     * 查看策略
     * @return
     * @throws Exception
     */
    @GetMapping("/selectPolocy/{policyID}")
    public String selectPolocy(@PathVariable("policyID") String policyID) throws Exception {
        String path = "/"+policyID;
        List<String> children = zkClient.getChildren(path, true);
        System.out.println("zkClient:"+children.toString());

        List<String> strings = curatorClient.getChildren().forPath(path);
        System.out.println("curatorClient:"+strings.toString());

        return children.toString();
    }

    /**
     * 配置策略
     * API:
     * /select/order
     * /create/order
     * /update/order
     * /delete/order
     * @param editPolicyDto
     * @return
     * @throws Exception
     */
    @PostMapping("/editPolicy")
    public String editPolicy(@RequestBody EditPolicyDto editPolicyDto)  {
        String path = "/"+editPolicyDto.getPolicyID();
        editPolicyDto.getPermissions().forEach((x) ->{
            System.out.println("====permission:"+x);///delete/order
            try {
                //使用curatorClient创建多级节点
                curatorClient.create().creatingParentsIfNeeded().forPath(path+x,"".getBytes());
//                curatorClient.create().creatingParentContainersIfNeeded()

            } catch (Exception e) {
                log.error(path+x+"   "+e.getMessage());
            }
        });
        return "success";
    }
    @DeleteMapping("/delPolicy")
    public String delPolicy(@RequestBody EditPolicyDto editPolicyDto) throws Exception {
        String path = "/"+editPolicyDto.getPolicyID()+editPolicyDto.getPermission();
        curatorClient.delete().forPath(path);
        return "success";
    }


    /**
     * TODO 2022年08月19日11:55:36
     * @param editPolicyDto
     * @return
     */
    @PostMapping("/checkPath")
    public String checkPath(@RequestBody EditPolicyDto editPolicyDto) throws Exception {

        String path = "/"+editPolicyDto.getPolicyID()+editPolicyDto.getPermission();

        Stat stat1 = curatorClient.checkExists().forPath(path);

        if (stat1 == null){
            System.out.println(path + " 不存在");
            return "false";
        }else {
            System.out.println(path + " 存在");
            return "true";
        }
    }



}
