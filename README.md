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
    
    这里会返回图片的MD5值作为id，后续查询和访问可以用到。当然很明显客户端也可以通过预算MD5的做法确认图片是否已经上传来实现"秒传"功能。
   
##### 图片下载
    GET:/view/[id]?width=[width]&height=[height]
    可选参数width和height，不输入则维持原状。

##### 图片查询
    GET:/exists/[id]

## 管理页面
通过`admin.html`可以进行简单的管理操作（上传等）。
