package com.lsh.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 11:35
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZookeeperUtil {

    private ZooKeeper zooKeeper;

    /**
     *判断当前节点是否存在
     */
    public Stat isExistZnode(String path) throws Exception{
        Stat stat = zooKeeper.exists(path,true);
        return stat;
    }
    /**
     * 创建节点
     */
    public String createZnode(String path, Object object, ArrayList<ACL> zooDefs, CreateMode createMode) throws Exception {
        return  zooKeeper.create(path,object.toString().getBytes(),zooDefs,createMode);
    }
    /**
     * 修改节点
     */
    public Stat updateZnode(String path, Object object, int version) throws Exception {
        return zooKeeper.setData(path,object.toString().getBytes(),version);
    }
    /**
     * 删除节点
     */
    public void deleteZnode(String path,int version) throws Exception {
        zooKeeper.delete(path,version);
    }


}
