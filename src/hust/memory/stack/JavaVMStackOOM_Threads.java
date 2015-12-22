package hust.memory.stack;

/**
 * 模拟运行期间创建线程过多导致的栈内存溢出（OOM）
 * 
 * 解决：
 * 调节-Xss参数，减小单线程栈内存大小。因为32位的windows限制单进程的内存大小为2GB，而留给所有线程的总栈大小=
 * 2GB - Xmx(最大堆容量) - MaxPermSize(最大方法区容量) - 程序计数器消耗容量(忽略不计)。因此-Xss越小，理论上
 * 可创建的线程数越多，但是太小又会出现SOF溢出问题（@see JavaVMStackSOF）。
 * 
 * 注意：请先保存当前的所有工作，再运行本程序，因为此代码可能导致操作系统假死。
 * 
 * @author 2015-12-22
 *
 */
public class JavaVMStackOOM_Threads {
	
	private void dontstop() {
		while(true) {
		}
	}
	
	public void stackLeakByThread() {
		while(true) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					dontstop();
				}
			});
			thread.start();
		}
	}
	
	/**
	 * VM Args: -Xss2m
	 * @param args
	 */
	public static void main(String[] args) {
		JavaVMStackOOM_Threads oom = new JavaVMStackOOM_Threads();
		oom.stackLeakByThread();
	}

}
/*
 * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
 * 	at java.lang.Thread.start0(Native Method)
 * 	at java.lang.Thread.start(Thread.java:597)
 * 	at hust.memory.stack.JavaVMStackOOM_Threads.stackLeakByThread(JavaVMStackOOM_Threads.java:28)
 * 	at hust.memory.stack.JavaVMStackOOM_Threads.main(JavaVMStackOOM_Threads.java:38)
 */
