本地测试方法
===
1 打开Zookeeper，并且配置application.properties里的ZK链接；<br/>
2 拷贝一份工程，拷贝出来的工程做如下修改:<br/>
* GlobalConstants.DEFAULT_HRPC_PORT = 62710
* DemoInterfaceImpl解开注释
* application.properties 修改 server.port=8080
* application.properties 修改 logging.path=/home/admin/logs2
* application-rpc-server-conf.properties 里的测试配置删掉

[我的博客](http://hquery.cn "悬停显示")