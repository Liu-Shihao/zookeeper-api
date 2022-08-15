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

## 内容

<p>
zk
目录树结构
主从  
选举机制
速度快
持久节点 和 临时节点（client和server连接时产生session会话）

序列节点

netstat -natp | egrep '(2888|3888)'
3888  选举投票用
2888  leader 正常接受请求

常用命令
create
delete
exists
getdata
setdata
get children
sync

java  jps
zk使用java开发

scp -r ./data/  node02:`pwd`

scp -r /etc/profile  node02:/etc

create /hello  ""  创建节点

create -e /hello  ""  创建临时节点 客户端退出，session会话结束，节点删除
create -s /hello  ""  序列创建，并发时可以规避覆盖
create /hello/zk  ""

get /hello  获取数据

set /hello "world"    node节点能保存1m数据   字节数组形式

hello world
cZxid = 0x100000103    c表示create
ctime = Mon Aug 15 03:44:30 GMT 2022
mZxid = 0x100000108
mtime = Mon Aug 15 03:49:16 GMT 2022
pZxid = 0x100000106
cversion = 1
dataVersion = 2
aclVersion = 0
ephemeralOwner = 0x0    表示持久节点    ephemeralOwner = 0x2000153b7aa001d表示临时节点 为session ID
dataLength = 11
numChildren = 1


server 如果挂掉，client 可以连接其他server， session 不会丢失

1.统一配置管理  --》   1M数组
2.分组管理     --》   path结构
3.统一命名     --》    序列
4.同步         --》    临时节点


应用场景：
1.分布式锁  利用临时节点  session结束后，节点自动删除 ，锁依托一个父节点，且具备-s,表示父节点下可以有多把锁，队列时 事务锁， （客户端代码实现）
2.HA 选主


paxos site:douban.com

Paxos 分布式 算法

ZAB  原子广播协议  队列+2PC
原子：要么成功，要么失败，没有中间状态
广播：通知所有节点（节点不一定收到）超过半数
队列：FIFO 先进先出 顺序性
zk的数据状态再内存，用日志保存在磁盘

1.client 写数据到server follower节点
2.follower节点 通知leader节点
3.leader 节点广播到所有节点
4.超过半数节点回复，leader进行广播，通知所有节点写操作


zk集群的选举机制：  两种场景： 1第一次启动集群的时候  2leader节点宕机的时候
每个节点有代表自己的myid 已经执行到的事务ID  Zxid
新的leader Zxid越高的表示数据越全
推选制：先比较Zxid，如果Zxid相同，再比较myid（都是选举值大的）
集群中的所有节点的Zxid一般都是相同，所以比较的一般都是myid，谁先超出半数，谁当选leader

如果已经没有超过半数的节点存活，则zk集群变为不可用状态，无法对外提供服务

watch


server 宕机后，client会连接其他server，并且session ID 保持不变








</p>
