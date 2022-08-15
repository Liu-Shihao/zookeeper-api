package com.lsh.zk;

import com.lsh.zk.utils.CuratorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/15 15:19
 * @Desc:
 */
@SpringBootTest(classes = ZkApplication.class)
@RunWith(SpringRunner.class)
public class CuratorUtilTest {
    @Autowired
    CuratorUtil curatorUtil;

    @Test
    public void test() throws Exception {
        String haha = curatorUtil.createNode("/testy", "haha");
        System.out.println(haha);
        String nodeData = curatorUtil.getNodeData("/testy");
        System.out.println(nodeData);


    }
}
