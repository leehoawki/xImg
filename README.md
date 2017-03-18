# xImg
带有图片处理功能的文件管理系统。存入的时候会预先对图片进行处理并按照MD5分散放到目录下，通过id对文件、图片进行访问，支持简单的图片处理功能并且会将结果进行缓存。

## Build and Run
1. JDK8，MAVEN3。
2. 直接mvn package后即可扔到Web容器中运行。
3. 也可以通过本地运行 mvn spring-boot:run 在本地运行。

## API
##### 上传图片
    POST:/upload
    enctype="multipart/form-data"
    data:file=[file]
    
##### 图片下载
    GET:/view/[id]

##### 图片查询
    GET:/exists/[id]

## 管理页面
通过`admin.html`可以进行简单的管理操作（上传等）。