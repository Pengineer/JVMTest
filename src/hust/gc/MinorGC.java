package hust.gc;

/**
 * 测试新生代Minor GC
 * 
 * @author 2015-12-25
 *
 */
public class MinorGC {

	private static final int _1MB = 1024 * 1024;
	
	/**
	 * VM args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
	 * 说明：堆总大小20M，新生代10M，老年代10M。新生代中Eden：Survivor：Survivor=8:1:1（默认）。
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		byte[] allocation1 = new byte[2 * _1MB];
		byte[] allocation2 = new byte[2 * _1MB];
		byte[] allocation3 = new byte[2 * _1MB];
		byte[] allocation4 = new byte[4 * _1MB];  //出现一次Minor GC
	}
}

/*
[GC [DefNew: 6472K->140K(9216K), 0.0082223 secs] 6472K->6284K(19456K), 0.0082660 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
Heap
 def new generation   total 9216K, used 4400K [0x036e0000, 0x040e0000, 0x040e0000)
  eden space 8192K,  52% used [0x036e0000, 0x03b08fd8, 0x03ee0000)
  from space 1024K,  13% used [0x03fe0000, 0x04003328, 0x040e0000)
  to   space 1024K,   0% used [0x03ee0000, 0x03ee0000, 0x03fe0000)
 tenured generation   total 10240K, used 6144K [0x040e0000, 0x04ae0000, 0x04ae0000)
   the space 10240K,  60% used [0x040e0000, 0x046e0030, 0x046e0200, 0x04ae0000)
 compacting perm gen  total 12288K, used 2121K [0x04ae0000, 0x056e0000, 0x08ae0000)
   the space 12288K,  17% used [0x04ae0000, 0x04cf26b8, 0x04cf2800, 0x056e0000)
No shared spaces configured.

解释：
（1）6472K->140K(9216K):因为分配allocation4时新生代空间不足，触发GC，新生代占用量减少。
（2）6472K->6284K(19456K)：总堆占用量不变。因为allocation1~3对象依然存活，因此新生代剩余空间不足以分配给4，导致
	 Minor GC的触发，allocation1~3通过分配担保机制提前转移到老年代。（为什么说提前呢，因为在分代收集思想中，虚拟机给
	 每一个对象定义了对象年龄计数器，对象每经历一次Minor GC且存活，计数器加1，直到-XX:MaxTenuringThreshold参数设置的
	 阀值，然后转为老年代，当然这是一般情况。）
（3）def new generation total 9216K：可能会有疑惑，新生代不是10M吗，怎么成了9M。因为新生代采用的复制算法回收空间，
	有一个Survivor（1M）作为轮换备份来保存回收的存活对象，因此这里显示的可用新生代总空间。
（4）tenured generation total 10240K：老年代6144/10240。
*/
