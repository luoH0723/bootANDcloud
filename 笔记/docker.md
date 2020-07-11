# docker

## 安装

```
#1.下载docker的依赖环境
yum -y install yum-utils device-mapper-persistent-data lvm2

#2.设置一下下载docker的镜像源
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

#3.安装docker
yum makacache fast
yum -y install docker-ce

#4.启动，并设置为开机自动启动，测试
#启动docker服务
systemctl start docker
#设置开机自动启动
systemctl enable docker
#测试
docker run hello-world
```

### 中央仓库

```
1.docker官方的中央仓库：这个仓库是镜像最全的，但是下载速度较慢
2.国内的镜像：网易蜂巢，daoCloud
3.私服创建
```

#需要在/etc/docker/daemon.json

```
{
	"registry-mirrors": ["https://registry.docker-cn.com"]
	"insecure-registries": ["ip:port"]
}
#重启两个服务
systemctl daemon-reload
systemctl restart docker
```

### 镜像操作

```
#拉取镜像到本地
docker pull 镜像名称[:tag]
#例子
docker pull daocloud.io/library/tomcat:8.5.15-jre8

#查看本地的镜像
docker images

#删除本地镜像
docker rmi 镜像的唯一标识


#镜像的导入导出
#将本地的镜像导出
docker save -o 导出的路径 镜像id
#加载本地的镜像文件
docker load -i 镜像文件

#修改名称
docker tag 镜像id 名称:版本
```

### 容器操作

```
#1.运行容器
#简单
docker run 镜像的标识|镜像名称[:tag]
#常用参数
docker run -d -p 宿主机端口：容器端口 --name 容器名称 镜像的标识|镜像名称[:tag]
#-d: 后台运行容器
#-p 宿主机端口：容器端口 ： 映射当前linux的端口和容器的端口
#--name 容器名称 镜像的标识|镜像名称[:tag]   指定容器的名称
```

```
#查看正在运行的容器
docker ps [-qa]
#-a:查看全部的容器，包括没有运行
#-q：只查看容器的到标识
```

```
#3查看容器的日志
docker logs -f 容器id
#-f: 可以滚动查看日志的最后几行
```

```
#4进入容器内部
docker exec -it 容器id bash
```

```
#5删除容器
#停止指定容器
docker stop 容器id
#停止全部容器
docker stop $(docker ps -qa)
#删除指定容器
docker rm 镜像id
#删除全部容器
docker rm $(docker ps -qa)
```

```
#6启动容器
docker start 容器id
```

