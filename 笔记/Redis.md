# Redis

## 一、Redis介绍

### 1.1引言

```
1.由于用户量增大，请求数量也随之增大，数据压力过大。
2.多台服务企之间，数据不同步。
3.多台服务器之间的锁，已经不存在互斥性
```

![image-20200707170231156](C:\Users\lh\AppData\Roaming\Typora\typora-user-images\image-20200707170231156.png)

### 1.2NoSQL

```
Redis就是一款NoSQL
NoSQL->非关系型数据库->Not Only SQL
	1.Key-Value:Redis..
	2.文档型：ElasticSearch,Solr,MongoDB..
	3.面向列：Hbase,Cassandra..
	4.图形化：Neo4j..
	
除了关系型数据库都是非关系型数据库。（表和表之间无关系）

NoSQL只是一种概念，泛指菲关系型数据库，和关系型数据库做了一个区分。
```

### 1.3Redis介绍

```
Redis（Remote Dictonary Server）即远程字典服务，Redis是由C语言去编写，Redis是一款基于Key-Value的NoSQL，而且Redis是基于内存存储数据的，Redis还提供了多种持久化机制，性能可达到110000/s读取数据以及81000/s写入数据，Redis还提供了主从，哨兵以及集群的搭建方式，可以更方便的横向扩展以及垂直扩展。
```

## 二.Redis安装

2.1安装Redis

```
version: '3.1'
services:
	redis:
		image: daocloud.io/library/redis:5.0.7
		restart: always
		container_name: redis
		environment:
			- TZ=Asia/shanghai
		ports:
			- 6379:6379
```

