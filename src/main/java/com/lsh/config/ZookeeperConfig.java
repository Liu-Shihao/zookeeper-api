package com.lsh.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/11 14:35
 * @Desc:
 */
@Slf4j
@Configurable
@Component
public class ZookeeperConfig {

    private static final String host = "192.168.153.128:2181,192.168.153.129:2181,192.168.153.130:2181";

    private static final int timeout = 60000;


    @Bean("zkClient")
    public ZooKeeper zooKeeper() throws IOException {
        ZooKeeper zooKeeper = null;
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(host,timeout , new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    log.info("receive event:" + watchedEvent.toString());
                    if(Event.KeeperState.SyncConnected==watchedEvent.getState()){
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            log.info("zooKeeper init state:" + zooKeeper.getState());
        }catch (Exception e){
            log.error("zooKeeper init throw exception:" + e.getMessage());
        }
        return zooKeeper;
    }

    public ZookeeperConfig() {
        log.info("loading ZookeeperConfig...");
    }
}
