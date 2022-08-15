# spring boot + zk

##版本信息
springboot 2.2.2.RELEASE
org.apache.zookeeper 3.4.13
curator   4.0.1

zookeeper镜像  3.4.13


#zk环境
使用Docker部署zk集群,需要进入容器内部启动zk:
```shell
docker ps |grep zookeeper
docker exec -it 容器ID /bin/bash

zkServer.sh start
```
<table>
<tr>
<td>
node01
</td>
<td>
192.168.153.128
</td>
</tr>

<tr>
<td>
node02
</td>
<td>
192.168.153.129
</td>
</tr>

<tr>
<td>
node03
</td>
<td>
192.168.153.130
</td>
</tr>

</table>