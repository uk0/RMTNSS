## RMTNSS
基于Finalspeed 修改版本，支持端口转发。


### quick start 


```bash
mvn clean package # 编译jar
java -jar RMT-NSS.jar server | client # 任意一种模式
```
 
 配置文件的路径为:
    当前运行jar路径`/conf/`
    推荐用Docker运行服务端。

* for Docker 

    ```bash
    # 相应文件在 `releases` 下,已经编译好Jar 直接用即可。
    docker-compose -f docker-compose-server.yml  up -d 
  
    docker-compose -f docker-compose-client.yml  up -d 
    ```
* config 

> server 部分配置兼容SS

```json

{
  "password": "password",
  "localPort": 1088,
  "method": "chacha20",
  "remotePort": 1085,
  "remoteIpAddress": "127.0.0.1",
  "server_mode": true,
  "auth":true,
  "timeout": 300
}


```


> client 配置


```json

{
  "recent_address_list":["10.105.220.49"],
  "upload_speed":5600000,  // 上传速度
  "download_speed":5600000,// 下载速度
  "server_port":150, // fs 端口
  "server_address":"10.105.220.49", //fs 地址
  "protocal":"udp", // tcp | udp 
  "auto_start":true,

  "dst":[ // 远程 代理 注意:(默认需要配置一个SS)
    {
      "listen_port":1087,
      "name":"SS",
      "dst_port":1085,
      "dst_address":"10.105.220.49"
    },
    {
      "listen_port":9999,
      "name":"web",
      "dst_port":80,
      "dst_address":"10.162.42.180"
    }],
    //以下的配置为SS
  "password": "Passw0rd",
  "localPort": 10890, //本地Socket5端口
  "method": "chacha20", //加密方式
  "remotePort": 1087, // ss监听的端口为fs加速过的端口。
  "remoteIpAddress": "127.0.0.1", //ss 监听本地地址
  "server_mode": false,
  "auth":true, 
  "timeout": 300
}

```


### method list

Supported encrypt method:

    1. aes-128-cfb/ofb
    2. aes-192-cfb/ofb
    3. aes-256-cfb/ofb
    4. chacha20/chacha20-ietf

### install win-pcap
 1.windows 上需要安装一个pcap。
 
### TODO

 1.添加TLS代理,HTTP模拟流量.
 2.添加模拟视频流量的方式代理 m3u8文件方式。
 3.构建Golang版本的NSS。
 4.欢迎大家提出问题。
