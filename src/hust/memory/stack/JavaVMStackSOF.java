package hust.memory.stack;

/**
 * 模拟虚拟机栈和本地方法栈SOF
 * 
 * 解决办法：
 * 调节-Xss参数，增大单线程栈内存大小
 * 
 * tips：
 * 关于栈，Java虚拟机规范描述了两种异常：
 * 		（1）如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError
 * 		（2）如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError
 * 
 * 补充：
 * 1，栈内存是线程隔离的，因此对栈的设置都是针对单线程的。
 * 2，对于HotSpot来说，虚拟机栈和本地方法栈合二为一了，因此虽然-Xoss参数（设置每个线程的本地方法栈大小）存在，
 * 但是实际上是无效的，栈容量只由-Xss参数（设置每个线程的虚拟机栈大小）设置。
 * 
 * @author 2015-12-22
 *
 */
public class JavaVMStackSOF {

	private int stackLength = 1;
	
	public void stackLeak() {
		stackLength++;
		stackLeak();
	}
	
	/**
	 * VM Args: -Xss128k
	 * -Xssn： 设置单个线程栈的大小。JDK5.0以后默认值为1M，以前为256K。
	 */
	public static void main(String[] args) {
		JavaVMStackSOF jsf = new JavaVMStackSOF();
		try {
			jsf.stackLeak();
		} catch (Throwable e) { //***这里不能用Exception，因为内存溢出属于Error
			System.out.println("stack length: " + jsf.stackLength);
			e.printStackTrace();
		}
	}
}

/*
 * 控制台输出：
 * stack length: 2401
 * java.lang.StackOverflowError
 * 	at hust.memory.stack.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:19)
 * 	at hust.memory.stack.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:20)
 */