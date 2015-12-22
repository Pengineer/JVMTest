package hust.memory.stack;

/**
 * 模拟单线程栈空间设置过大导致虚拟机初始化失败（OOM）
 * 
 * 解决办法：
 * 调节-Xss参数，减小单线程栈内存大小。因为32位的windows限制单进程的内存大小为2GB，而留给所有线程的总栈大小=
 * 2GB - Xmx(最大堆容量) - MaxPermSize(最大方法区容量) - 程序计数器消耗容量(忽略不计)。
 * 
 * @author 2015-12-22
 *
 */
public class JavaVMStackOOM {

	
	public void stackLeak() {
		stackLeak();
	}
	
	/**
	 * VM Args: -Xms800m -Xmx800m -Xss3000m
	 */
	public static void main(String[] args) {
		JavaVMStackOOM jsf = new JavaVMStackOOM();
		try {
			jsf.stackLeak();
		} catch (Throwable e) { //***这里不能用Exception，因为内存溢出属于Error
			e.printStackTrace();
		}
	}
}

/*
 * 控制台输出：
 * Error occurred during initialization of VM
 * java.lang.OutOfMemoryError: unable to create new native thread
 * 	at java.lang.Thread.start0(Native Method)
 * 	at java.lang.Thread.start(Thread.java:597)
 * 	at java.lang.ref.Reference.<clinit>(Reference.java:145)
 */