package springbook.learningtest.spring.ioc;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

public class ApplicationContextTest {
	
	@Test
	public void test1(){
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		Hello hello1 = ac.getBean("hello1", Hello.class);
		assertThat(hello1, is(notNullValue()));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		//�� ��Ÿ������ ���� ������Ʈ�� �����. ��Ŭ������ Hello�� �����Ѵ�. <bean class="springbook.leanringtest..Hello"/>�� �ش��ϴ� �����̴�.
			
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		// ���� name ������Ƽ�� �� ���� �����Ѵ�.<property name="name" value="Spring"/> �� �ش��Ѵ�. 
		
		ac.registerBeanDefinition("hello2", helloDef);
		// �տ��� ������ ���Ÿ������ hello2��� �̸��� ���� ������ �ؼ� ����Ѵ�. <bean id="hello2" ... /> �� �ش��Ѵ�.
		
		Hello hello2 = ac.getBean("hello2", Hello.class);
		assertThat(hello2.sayHello(), is("Hello Spring"));
		// BeanDefinition���� ��ϵ� ���� �����̳ʿ� ���� ��������� ������Ƽ ������ �ƴ��� Ȯ���Ѵ�.
		
		assertThat(hello1, is(not(hello2)));
		// ó������� ��� �ι�° ����� ���� ��� ������ Hello Ŭ���� ���� ������ ������Ʈ�� �����ƴ�.
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
	}
	
	@Test
	public void registerBeanWithDependency(){
		StaticApplicationContext ac = new StaticApplicationContext();	// IoC �����̳� ����. ������ ���ÿ� �����̳ʷ� �����Ѵ�.
		
		// StringPrinter Ŭ���� Ÿ���̸� printer ��� �̸��� ���� ���� ����Ѵ�.
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
		
		// ���Ÿ������ ���� ������Ʈ�� �����. ��Ŭ������ Hello�� �����Ѵ�.
		// <bean class="springbook.learningtest..Hello"/> �� �ش��ϴ� ��Ÿ������.
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		
		// ���� name ������Ƽ�� �� ���� �����Ѵ�. <property name="name" value="Spring"/> �� �ش��Ѵ�.
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		
		// ���̵� printer�� �� ���� ���۷����� ������Ƽ�� ���
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		ac.registerBeanDefinition("hello", helloDef);
		
		Hello hello = ac.getBean("hello",Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericApplicationContext(){
		GenericApplicationContext ac = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
		
		// XmlBeanDefinitionReader�� �⺻������ Ŭ�����н��� ���ǵȸ��ҽ��� ���� ������ �д´�.
		reader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		ac.refresh();  //��� ��Ÿ������ ����� �Ϸ������� ���ø����̼� �����̳ʸ� �ʱ�ȭ�϶�� ����̴�.
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericXmlApplicationContext() {
		GenericApplicationContext ac = new GenericXmlApplicationContext(
				"springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		// application context ������ ���ÿ� xml ������ �о���� �ʱ�ȭ���� �����Ѵ�.
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		assertThat( ac.getBean("printer").toString() , is("Hello Spring"));
	}
}
