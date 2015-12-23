package hust.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Java 四类引用测试
 * 
 * 强引用：对象不会被GC
 * 软引用：内存溢出之前，对这些对象进行第二次回收
 * 弱引用：下一次垃圾回收时对象被回收
 * 虚引用：不能通过虚引用获取对象，为一个对象设置虚引用的唯一目的就是能在这个对象被收集器回收时收到一个系统通知
 * 
 * @author 2015-12-23
 *
 */
public class RefereanceTest {

	public static void main(String[] args) throws Exception {
//		softTest_1();
		
//		weakTest_1();
		
		testReference_1();
	}
	
	//软引用测试
	public static void softTest_1() {
		RefereanceTest t1 = new RefereanceTest();
		ReferenceQueue<RefereanceTest> queue1 = new ReferenceQueue<RefereanceTest>();
		SoftReference<RefereanceTest> sr = new SoftReference<RefereanceTest>(t1, queue1);
		t1 = null; //去除强引用
		sr.get().method("SoftReference");// ok
		System.gc();
		sr.get().method("SoftReference");// ok
		
		sr.clear(); //清除软引用对象（sr所引用的对象），clear方法清除的对象不进入队列
		System.out.println(queue1.poll());// null
		System.out.println(sr.toString());// ok
		sr.get().method("SoftReference");// not ok
	}
	
	//弱引用测试
	public static void weakTest_1() throws Exception {
		RefereanceTest t2 = new RefereanceTest();
		ReferenceQueue<RefereanceTest> queue2 = new ReferenceQueue<RefereanceTest>();
		WeakReference<RefereanceTest> wr = new WeakReference<RefereanceTest>(t2, queue2);
		t2 = null;
		wr.get().method("WeakReference");
		System.gc();
		System.out.println(queue2.poll());// gc后此时队列里面有一个对象，但是对象不可用（？）
		System.out.println(wr);// ok
		queue2.remove(10);// 去掉队列中的一个元素（参数表示时间）
		System.out.println(wr);// ok
		wr.get().method("WeakReference");// not ok
	}
	
	/*
	 * http://www.iteye.com/problems/69204
	 * 原因：
	 * 首先，大致描述一下finalize流程：当对象变成(GC Roots)不可达时，GC会判断该对象是否覆盖了finalize方法，若未覆盖，
	 * 则直接将其回收。否则，若对象未执行过finalize方法，将其放入F-Queue队列，由一低优先级线程执行该队列中对象的
	 * finalize方法。执行finalize方法完毕后，GC会再次判断该对象是否可达，若不可达，则进行回收，否则，对象“复活”。
	 */
	public static void testReference_1() throws Exception {  
		Object object = new RefereanceTest(); 
		ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();  
		Reference<Object> reference = new PhantomReference<Object>(object, referenceQueue);  
		System.out.println(reference);  
		System.out.println(reference.isEnqueued());  
		object = null;
		System.gc();  
		Thread.sleep(0);//切换CPU执行权，由一低优先级Finalizer线程执行该队列中对象的finalize方法
		System.gc();  
		System.out.println(reference.isEnqueued());  
		try {  
			System.out.println(referenceQueue.remove(2000));  
		} catch (IllegalArgumentException e) {  
			e.printStackTrace();  
		} catch (InterruptedException e) {  
			e.printStackTrace();  
		}  
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
	public void method(String str) {
		System.out.println("Reference test: " + str);
	}
}
