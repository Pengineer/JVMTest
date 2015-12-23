package hust.memory.method;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟运行时常量池溢出（OOM）
 * 
 * 运行时常量池是方法去的一部分，由于从JDK1.7开始对方法区逐步的实施“去永久代”，因此本程序在不同的JDK下运行的
 * 结果会不一样。
 * 
 * 补充：
 * intern()是一个Native方法，它的作用是：如果字符串常量池中已经包含一个等于此String对象的字符串，则返回代表
 * 池中这个字符串的String对象；否则，将此String对象包含的字符串添加到常量池中，并且返回次String对象的引用。
 * 
 * @author 2015-12-23
 *
 */
public class RuntimeConstantPoolOOM {

	/**
	 * VM Args: -XX:PermSize=5m -XX:MaxPermSize=5m
	 */
	public static void main(String[] args) {
		//使用List保持着常量引用，避免Full GC回收常量池行为
		List<String> list = new ArrayList<String>();
		//5MB的PermSize在integer范围内足够产生OOM了
		int i=0;
		while(true) {
			list.add(String.valueOf(i++).intern()); //
		}
		
	}
}

/*
 * JDK1.6输出结果
 * Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
	at java.lang.String.intern(Native Method)
	at hust.memory.method.RuntimeConstantPoolOOM.main(RuntimeConstantPoolOOM.java:30)
 */

/*
 * JDK1.7运行结果是while循环一直在进行，无异常。
 */