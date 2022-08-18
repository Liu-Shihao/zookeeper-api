package com.lsh.zk.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 23:33
 * @Desc: ZkUtil 获得getZkClient
 */
@Slf4j
public class ZkUtil {

    private static ZooKeeper zooKeeper;

    private static final String host = "192.168.153.128:2181,192.168.153.129:2181,192.168.153.130:2181";

    private static final int timeout = 5000;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static DefaultWatch defaultWatch = new DefaultWatch();

    /**
     * 获得zkClient
     * 注意：new 出的ZooKeeper不能立即返回，还需要等待和zk server端建立连接后才能返回
     * 需要注册一个watch：此处自定义了DefaultWatch
     * @return
     */
    public static ZooKeeper getZkClient(){
        try {
            defaultWatch.setCountDownLatch(countDownLatch);
            zooKeeper = new ZooKeeper(host, timeout, defaultWatch);
            countDownLatch.await();//阻塞中，等待DefaultWatch连接成功
        }catch (Exception e){
            log.error("getZkClient error:{}",e.getMessage());
        }
        return zooKeeper;
    }

    /**
     * 关闭zk客户端连接
     */
    public static void closeZK(){
        if(zooKeeper != null){
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
