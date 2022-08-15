package com.lsh.zk.configcenter;

import lombok.Data;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 23:50
 * @Desc:  实现三个接口
 * Watcher 注册节点监控回调
 * StatCallback exists 节点是否存在异步回调
 * DataCallback getData 获取数据异步回调
 */
@Data
public class WatchCallBack  implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    //注意，getData数据时需要使用zk对象，需要手动set
    ZooKeeper zooKeeper;

    MyConf myConf;

    CountDownLatch countDownLatch = new CountDownLatch(1);


    public void aWait()  {
        try {
            zooKeeper.exists("/TestConfig", this, this,"aaa");
            countDownLatch.await();//等待MyConf数据取完
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    /**
     * getData 异步回调
     * @param rc
     * @param path
     * @param ctx
     * @param data
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

        if (data != null){
            System.out.println("getData 回调："+new String(data));
            myConf.setConf(new String(data));
            countDownLatch.countDown();
        }else {
            System.out.println("getData 回调 :没有数据");
        }

    }

    /**
     * exists 判断节点是否存在 异步回调
     * @param rc
     * @param path
     * @param ctx
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("======exists 回调======");
        if (stat != null){
            System.out.println("======"+path+" 节点存在======");
            zooKeeper.getData(path,this,this,"abc");
        }else {
            System.out.println("======"+path+" 节点不存在======");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("WatchedEvent 回调："+event.getState());
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                //节点刚被创建出来
                //整个流程：首先在TestConfig类中判断节点是否存在，此时节点还未创建，则被阻塞在aWait()方法，等到节点被创建出，发生watch回调，即此处，然后继续获得数据，出发getData回调方法，从而完成aWait方法（）
                zooKeeper.getData(event.getPath(),this,this,"abc");
                break;
            case NodeDeleted:
                //容忍性问题
                //如果节点被删除，数据要怎么处理？
                //清空myConf
                myConf.setConf("");
                //并且countDownLatch重新赋值
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                //注意：如果数据被变更，则需要重新获取数据
                zooKeeper.getData(event.getPath(),this,this,"abc");
                break;
            case NodeChildrenChanged:
                break;
        }

    }


}
