package com.lsh.zk;

import com.lsh.zk.utils.ZkApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 11:38
 * @Desc:
 */
@SpringBootTest(classes = ZkApplication.class)
@RunWith(SpringRunner.class)
public class ZkClientTest {

    @Autowired
    ZkApi zkApi;

    @Test
    public void createNode(){
        System.out.println("createNode："+zkApi.createNode("/test", "hello zk"));
        //节点创建成功返回true，失败返回false

    }
    @Test
    public void exists(){
        System.out.println("exists："+zkApi.exists("/test", true));
        //节点不存在，返回null
        //节点存在返回4294967372,4294967372,1660521761329,1660521761329,0,0,0,0,8,0,4294967372
    }
    @Test
    public void getData(){
        System.out.println("getData："+zkApi.getData("/test", null));
        //hello zk
        //如果节点已删除或不存在会抛出KeeperErrorCode = NoNode for /test异常 ，返回null
    }
    @Test
    public void deleteNode(){
        System.out.println("删除节点："+zkApi.deleteNode("/test"));
        System.out.println("exists？："+zkApi.exists("/test", true));
        //删除节点：true
        //exists？：null
    }
}
