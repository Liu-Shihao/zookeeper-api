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
@Component
@Configuration
@ConfigurationProperties(prefix = "zookeeper.curator")
@Data
public class CuratorProperties {
    /**
     * 集群地址
     */
    private String ip;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeoutMs;
    /**
     * 会话超时时间
     */
    private Integer sessionTimeOut;

    /**
     * 重试机制时间参数
     */
    private Integer sleepMsBetweenRetry;

    /**
     * 重试机制重试次数
     */
    private Integer maxRetries;

    /**
     * 命名空间(父节点名称)
     */
    private String namespace;
}
