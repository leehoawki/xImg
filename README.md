# xImg
带有图片处理功能的文件管理系统。存入的时候会预先对图片进行处理并按照MD5分散放到目录下，通过id对文件、图片进行访问，支持简单的图片处理功能并且会将结果进行缓存。

## Build and Run
1. JDK8，MAVEN3。
2. 直接mvn package后即可扔到Web容器中运行。
3. 也可以通过本地运行 mvn spring-boot:run 在本地运行。

## API
##### 上传文件
    POST:/upload
    enctype="multipart/form-data"
    data:file=[file]
    
##### 文件下载
    GET:/view/[id]

##### 文件删除
    GET:/delete/[id]

## 管理页面
通过`admin.html`和`upload.html`可以进行简单的管理操作（上传、查询和删除等）。