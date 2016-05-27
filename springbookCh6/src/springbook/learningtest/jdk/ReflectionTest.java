package springbook.learningtest.jdk;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ReflectionTest {
	@Test
	public void invokeMethod()throws Exception{
		String name = "Spring";
		
		// length()
		assertThat(name.length(),is(6));
		
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name),is(6));
		
		// charAt()
		assertThat(name.charAt(0),is("S"));
		
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name,0),is("S"));
	}
	
	@Test
	public void simpleProxy(){
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("toby"), is("Hello toby"));
		assertThat(hello.sayHi("toby"), is("Hi toby"));
		assertThat(hello.sayThankYou("toby"), is("ThankYou toby"));
		
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		assertThat(proxiedHello.sayHello("toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("toby"), is("THANKYOU TOBY"));
		
		Hello proxiedHello2 = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[]{Hello.class},	// proxy interface. it can be more than 1. so we are using array.
				new UppercaseHandler(new HelloTarget())
				);
		assertThat(proxiedHello2.sayHello("toby"), is("HELLO TOBY"));
		assertThat(proxiedHello2.sayHi("toby"), is("HI TOBY"));
		assertThat(proxiedHello2.sayThankYou("toby"), is("THANKYOU TOBY"));
		
	}
}
