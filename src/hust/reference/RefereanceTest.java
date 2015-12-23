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
 * 虚引用：
 * 
 * @author 2015-12-23
 *
 */
public class RefereanceTest {

	public static void main(String[] args) throws Exception {
//		softtest_1();
		
//		weaktest_1();
		
		testReference_1();
	}
	
	//软引用测试
	public static void softtest_1() {
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
	public static void weaktest_1() throws Exception {
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
	
	//http://www.iteye.com/problems/69204
	public static void testReference_1() {  
        Object object = new Object(); 
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();  
        Reference<Object> reference = new PhantomReference<Object>(object, referenceQueue);  
        System.out.println(reference);  
        System.out.println(reference.isEnqueued());  
        object = null;
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
	
	public void method(String str) {
		System.out.println("Reference test: " + str);
	}
}
