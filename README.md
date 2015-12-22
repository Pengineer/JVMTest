# Java 虚拟机测试补充说明

##### 1、参数 `-XX:+HeapDumpOnOutOfMemoryError` 用于生成转储快照文件。离线分析该文件通常有两种方式：
（1）使用JDK提供的分析工具 `jhat`
> 命令格式：jhat dumpfile(jmap生成的文件)

```Java
E:\myeclipse10.0>cd JVM

E:\myeclipse10.0\JVM>jhat java_pid4120.hprof
Reading from java_pid4120.hprof...
Dump file created Tue Dec 22 10:04:58 CST 2015
Snapshot read, resolving...
Resolving 1514243 objects...
Chasing references, expect 302 dots....................................
.......................................................................
.....................................
Eliminating duplicate references.......................................
.......................................................................
..................................
Snapshot resolved.
Started HTTP server on port 7000
Server is ready.
```

执行成功后，访问 [http://localhost:7000](http://localhost:7000) 即可查看内存信息。

（2）使用eclipse的内存映像分析工具`MAT`</br>
　　官网：[http://www.eclipse.org/mat/](http://www.eclipse.org/mat/)</br>
　　这是eclipse的一个插件，安装后可以打开xxx.hprof文件，进行分析，比jhat更方便使用，有些时候由于线上xxx.hprof文件过大，直接使用jhat进行初步分析了，可以的话拷贝到本地分析效果更佳。
