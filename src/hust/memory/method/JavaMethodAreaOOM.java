package hust.memory.method;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 借助CGLib使方法区溢出
 * 
 * 方法区用于存放Class相关的信息，如类名、访问修饰符、常量池、字段描述、方法描述等，对于这个区的测试，基本思路就是
 * 运行产生大量的类去填满方法区。
 * 
 * 当前很多主流框架，如Spring，hibernate，在对类进行增强时，都会使用到CGLib这类字节码技术，增强的类越多，就需要
 * 越大的方法区来保证动态生成的Class可以加载入内存。
 * 
 * 此类场景除了上面提到的程序使用了CGLib字节码增强和动态语言之外，常见的还有：大量JSP或动态产生JSP文件的应用（JSP第一次
 * 编译时需要编译为Java类）、基于OSGi的应用（同一个类，不同的加载器加载也视为不同的类）。
 * 
 * @author 2015-12-23
 *
 */
public class JavaMethodAreaOOM {

	/**
	 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
	 */
	public static void main(String[] args) {
		while(true) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(OOMObject.class);
			enhancer.setUseCache(false);
			enhancer.setCallback(new MethodInterceptor() {
				public Object intercept(Object obj, Method method,
						Object[] args, MethodProxy proxy) throws Throwable {
					return proxy.invokeSuper(obj, args);
				}
			});
			enhancer.create();
		}
	}
	
	static class OOMObject {}
}

/*
 * Caused by: java.lang.OutOfMemoryError: PermGen space
	at java.lang.ClassLoader.defineClass1(Native Method)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:621)
	... 8 more
*/
