package com.lsh.watch;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 13:47
 * @Desc:
 */
@Slf4j
public class WatcherApi implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        log.info("【Watcher监听事件】={}",event.getState());
        log.info("【监听路径为】={}",event.getPath());
        log.info("【监听的类型为】={}",event.getType()); //  三种监听类型： 创建，删除，更新
    }
}

