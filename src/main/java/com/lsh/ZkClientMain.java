package com.lsh;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 22:12
 * @Desc: zk 原生API
 * zk是有session概念的，没有连接池的概念
 */
@Slf4j
public class ZkClientMain {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        /**
         * 三个参数：
         * 1. zk集群地址
         * 2.session会话超时时间
         * 3.watch
         */
        ZooKeeper zooKeeper = new ZooKeeper("192.168.153.128:2181,192.168.153.129:2181,192.168.153.130:2181",
                3000,
                new Watcher() {
                    //Watcher 回调方法
                    //watch 的注册只发生在读类型调用：get exites
                    @Override
                    public void process(WatchedEvent event) {
                        //回调方法
                        Event.EventType type = event.getType();
                        log.info("-----事件类型：{}", type);
                        log.info("-----节点目录：{}", event.getPath());
                        Event.KeeperState state = event.getState();
                        switch (state) {
                            case Unknown:
                                break;
                            case Disconnected:
                                break;
                            case NoSyncConnected:
                                break;
                            case SyncConnected:
                                //连接成功
                                System.out.println("Connected.....");
                                countDownLatch.countDown();
                                break;
                            case AuthFailed:
                                break;
                            case ConnectedReadOnly:
                                break;
                            case SaslAuthenticated:
                                break;
                            case Expired:
                                break;
                        }
                        switch (type) {
                            case None:
                                break;
                            case NodeCreated:
                                break;
                            case NodeDeleted:
                                break;
                            case NodeDataChanged:
                                break;
                            case NodeChildrenChanged:
                                break;
                        }

                    }
                });
        countDownLatch.await();
        ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("CONNECTING.....");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("CONNECTED.....");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }


        //1.创建节点
        /**
         * String path, byte[] data, List<ACL> acl, CreateMode createMode
         * 四个参数：
         * 1.path  必须以/开头
         * 2.data  数据  字节数组
         * 3.ACL   访问控制
         * 4.CreateMode  节点类型，一共有四种类型：持久、临时、持久序列、临时序列
         * 5.第五个参数：StringCallback  React模型，掉完方法不阻塞，成功以后调用回调方法
         */
        //创建一个临时节点（EPHEMERAL）
        String pathName = zooKeeper.create("/zktest", "hahha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建节点返回："+pathName);

        //2 获取数据 数据有两种：1 节点数据 2 元数据
        //watch是一次性的
        //在取数据的时候注册了一个watch，未来如果这个节点有变化，会回调这个方法process（）
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/zktest", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("=======getDate Watch:"+watchedEvent.toString());
            }
        }, stat);
        System.out.println("获取数据："+new String(data ));

        //3 修改数据

        Stat stat1 = zooKeeper.setData("/zktest", "newData1".getBytes(), 0); //触发上边watch的回调
        Stat stat2 = zooKeeper.setData("/zktest", "newData2".getBytes(), stat1.getVersion());//第二次更新数据，则不会触发上边的回调


        System.out.println("--------Async start----------");
        //4 异步获得数据 ，new AsyncCallback.DataCallback() 为回调方法
        zooKeeper.getData("/zktest",false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                // rc 表示状态码  ，path 表示目录 ，ctx表示上下文，data 表示获得的数据，stat表示元数据
                System.out.println("--------Async Callback----------");
                System.out.println("date:"+new String(data));
                System.out.println("ctx:"+ctx.toString());
            }
        },"aaa");

        System.out.println("--------over----------");

        System.in.read();


    }
}
