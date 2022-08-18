package com.lsh.zk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/14 10:37
 * @Desc:
 */
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
@Data
public class ZookeeperProperties {

    public String address;

    public int sessionTimeOut;

}
