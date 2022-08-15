package com.lsh.zk.configcenter;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 23:18
 * @Desc: 重要
 * TestConfig 、WatchCallBack
 */
public class MainTest {
    ZooKeeper zk;

    @Before
    public void conn(){
        zk = ZkUtil.getZkClient();
    }

    @After
    public void close(){
        ZkUtil.closeZK();
    }


//    @Test
//    public void testConf(){
//        zk.exists("/TestConfig", new Watcher() {
//            @Override
//            public void process(WatchedEvent event) {
//                System.out.println("WatchedEvent 回调："+event.getState());
//
//            }
//        }, new AsyncCallback.StatCallback() {
//            @Override
//            public void processResult(int rc, String path, Object ctx, Stat stat) {
//
//                if(stat != null){
//                    System.out.println("节点存在");
//                }
//            }
//        },"aaa");
//    }
    //以上代码经过经过优化之后，直接使用自定义 WatchCallBack 代替
    @Test
    public void testConf() throws InterruptedException {
        //创建WatchCallBack
        WatchCallBack watchCallBack = new WatchCallBack();

        //创建MyConf，set到watchCallBack，中去，如果获得了数据，则会把data数据set到MyConf中
        MyConf myConf = new MyConf();

        watchCallBack.setMyConf(myConf);

        //1.节点存在，获取数据
        //2.节点不存在，阻塞，等待节点被创建之后获取数据
        watchCallBack.aWait();

        while (true){
            if ("".equals(myConf.conf)){
                //如果conf为空，说明节点可能被删除，重新调用aWait()获取数据（阻塞），等待节点被创建之后获取数据
                watchCallBack.aWait();
            }else {
                System.out.println(myConf.conf);
                Thread.sleep(2000);
            }
        }
    }
}
