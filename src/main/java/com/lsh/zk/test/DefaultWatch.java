package com.lsh.zk.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 23:33
 * @Desc: 构造默认Watch，在ZkUtil中使用
 */
@Slf4j
public class DefaultWatch  implements Watcher {

    CountDownLatch countDownLatch;

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected==event.getState()){
            //如果收到了服务端的响应事件,连接成功
            countDownLatch.countDown();
            log.info("Zookeeper Connected Successful!");
        }
    }
}
