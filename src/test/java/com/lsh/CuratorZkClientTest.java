package com.lsh;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 11:40
 * @Desc:
 */
@SpringBootTest(classes = com.lsh.ZkApplication.class)
@RunWith(SpringRunner.class)
public class CuratorZkClientTest {

    @Autowired
    CuratorFramework curatorClient;

    /**
     * 创建节点
     *
     * @throws Exception
     */
    @Test
    public void createNode() throws Exception {
        // 添加持久节点
        String path = curatorClient.create().forPath("/curator-node-test");
        System.out.println(String.format("curator create node :%s successfully.", path));

        // 添加临时序号节点,并赋值数据
        String path1 = curatorClient.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/curator-node-test", "some-data".getBytes());
        System.out.println(String.format("curator create node :%s successfully.", path1));

        // System.in.read()目的是阻塞客户端关闭，我们可以在这期间查看zk的临时序号节点
        // 当程序结束时候也就是客户端关闭的时候，临时序号节点会消失
        System.in.read();
    }

    /**
     * 获取节点
     *
     * @throws Exception
     */
    @Test
    public void testGetData() throws Exception {
        // 在上面的方法执行后，创建了curator-node节点，但是我们并没有显示的去赋值
        // 通过这个方法去获取节点的值会发现，当我们通过Java客户端创建节点不赋值的话默认就是存储的创建节点的ip
        byte[] bytes = curatorClient.getData().forPath("/curator-node-test");
        System.out.println(new String(bytes));
    }

    /**
     * 修改节点数据
     *
     * @throws Exception
     */
    @Test
    public void testSetData() throws Exception {
        curatorClient.setData().forPath("/curator-node-test", "hello zk data!".getBytes());
        byte[] bytes = curatorClient.getData().forPath("/curator-node-test");
        System.out.println(new String(bytes));
    }
    /**
     * 获取节点数据
     *
     * @throws Exception
     */
    @Test
    public void getData() throws Exception {
//        curatorClient.setData().forPath("/curator-node-test", "changed!".getBytes());
        byte[] bytes = curatorClient.getData().forPath("/curator-node-test");
        System.out.println(new String(bytes));
    }

    /**
     * 创建节点同时创建⽗节点
     *
     * @throws Exception
     */
    @Test
    public void testCreateWithParent() throws Exception {
        String pathWithParent = "/node-parent/sub-node-1";
        String path = curatorClient.create().creatingParentsIfNeeded().forPath(pathWithParent);
        System.out.println(String.format("curator create node :%s successfully.", path));
    }

    /**
     * 删除节点(包含子节点)
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        String pathWithParent = "/node-parent";
        curatorClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
    }

}
