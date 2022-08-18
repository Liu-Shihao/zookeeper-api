package com.lsh.zk.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
//    public void testGetData(){
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
    public void testGetData() throws InterruptedException {
        //创建WatchCallBack，传入zk对象
        WatchCallBack watchCallBack = new WatchCallBack(zk,"/test");

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
                System.out.println("myConf.conf："+myConf.getConf());
                Thread.sleep(5000);
            }
        }
    }

    /**
     * CreateMode.PERSISTENT 持久节点
     * CreateMode.EPHEMERAL 临时节点
     *
     * @throws InterruptedException
     */
    @Test
    public void testChildren() throws Exception {
        String path = "/test1";
//        WatchCallBack watchCallBack = new WatchCallBack(zk,path);
//        zk.create(path, "admin".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        List<String> children = zk.getChildren(path, true);
        children.forEach((x)-> {

            try {
                byte[] data = zk.getData(path+"/" + x, true, new Stat());
                System.out.println(x+":"+new String(data));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }


}
