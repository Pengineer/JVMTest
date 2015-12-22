package hust.memory.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟Java 堆溢出/泄露
 * 
 * 
 * @author 2015-12-22
 *
 */
public class HeapOOM {
	
	static class OOMObject {}
	
	/**
	 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError  (最后一个参数用于生成内存溢出的转储文件)
	 */
	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		
		while (true) {
			list.add(new OOMObject());
		}
	}
	
	/*
	 * 控制台输出：
	 * java.lang.OutOfMemoryError: Java heap space
	 * Dumping heap to java_pid4120.hprof ...
	 * Heap dump file created [32580628 bytes in 0.483 secs]
	 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	 */
	
}
