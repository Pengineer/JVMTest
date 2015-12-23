package hust.reference;

/**
 * 此代码演示了两点：
 * 1，对象可以在被GC时，利用finalize方法的执行自我拯救。
 * 2，这种自救的机会只有一次，因为一个对象的finalize()方法最多只会被系统自动调用一次。
 * 
 * 关于finalize()方法：{@link http://blog.csdn.net/pi9nc/article/details/12374049}
 * finalize()的作用，执行过程，问题。（finalize在资源释放方面能做的try-finally等也可以做，而且做得更好。Java虚拟机作者建议大可忘掉这个方法的存在）
 * 
 * a.finalize()是Object的protected方法，子类可以覆盖该方法以实现资源清理工作，GC在回收对象之前调用该方法。
 * b.大致描述一下finalize流程：当对象变成(GC Roots)不可达时，GC会判断该对象是否覆盖了finalize方法，若未覆盖，则直接将其回收。否则，若对象未执行过finalize方法，将其放入F-Queue队列，由一低优先级线程执行该队列中对象的finalize方法。执行finalize方法完毕后，GC会再次判断该对象是否可达，若不可达，则进行回收，否则，对象“复活”。
 * c.Java语言规范并不保证finalize方法会被及时地执行、而且根本不会保证它们会被执行。（运行代价高，不确定性大，无法保证各个对象的调用次序）
 * 
 * @author 2015-12-23
 *
 */

public class FinalizeEscapeGC {

	public static FinalizeEscapeGC SAVE_HOOK = null;
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("finalize method executed.");
		SAVE_HOOK = this;
	}

	public static void main(String[] args) throws Throwable {
		SAVE_HOOK = new FinalizeEscapeGC();
		
		//对象第一次成功拯救自己
		SAVE_HOOK = null;
		System.gc();
		//因为finalize方法优先级很低，所以暂停0.5秒以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null) {
			System.out.println("yes, i am still alive.");
		} else {
			System.out.println("no, i am dead.");
		}
		
		//下面这段代码与上面的完全相同，但是这次自救失败了
		SAVE_HOOK = null;
		System.gc();
		//因为finalize方法优先级很低，所以暂停0.5秒以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null) {
			System.out.println("yes, i am still alive.");
		} else {
			System.out.println("no, i am dead.");
		}
	}

}
