package com.lsh.zk.configcenter;

import com.lsh.zk.config.ZookeeperConfig;
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
