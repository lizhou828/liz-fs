
# 文件系统 

## 说明
    
模块名称|用途   
|------|-----:|
fs-common-old|公共模块(过渡阶段：含老版本的部分代码，以后全都用FastDFS时，该模块将废弃)
fs-common|公共模块，可以单独对外提供依赖
fs-fastdfs-core|FastDFS核心模块，可以单独对外提供依赖
fs-fastdfs-webClient|其他Web应用调用FastDFS，可参考该模块


## 准备及注意事项
 * 预先在服务器安装及部署好 FastDFS 软件，并适当调整配置文件
 * track server默认的端口为 22122
