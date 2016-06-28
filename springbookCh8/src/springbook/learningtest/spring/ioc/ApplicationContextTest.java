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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

public class ApplicationContextTest {
	
	private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
	// 현재 클래스의 패키지 정보를 클래스패스 형식으로 만들어서 미리 저장해둔 것이다.
	
	@Test
	public void test1(){
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerSingleton("hello1", Hello.class);
		
		Hello hello1 = ac.getBean("hello1", Hello.class);
		assertThat(hello1, is(notNullValue()));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		//빈 메타정보를 담은 오브젝트를 만든다. 빈클래스는 Hello로 지정한다. <bean class="springbook.leanringtest..Hello"/>에 해당하는 정보이다.
			
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		// 빈의 name 프로퍼티에 들어갈 값을 지정한다.<property name="name" value="Spring"/> 에 해당한다. 
		
		ac.registerBeanDefinition("hello2", helloDef);
		// 앞에서 생성한 빈메타정보를 hello2라는 이름을 가진 빈으로 해서 등록한다. <bean id="hello2" ... /> 에 해당한다.
		
		Hello hello2 = ac.getBean("hello2", Hello.class);
		assertThat(hello2.sayHello(), is("Hello Spring"));
		// BeanDefinition으로 등록된 빈이 컨테이너에 의해 만들어지고 프로퍼티 설정이 됐는지 확인한다.
		
		assertThat(hello1, is(not(hello2)));
		// 처음등록한 빈과 두번째 등록한 빈이 모두 동일한 Hello 클래스 지만 별개의 오브젝트로 생성됐다.
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
	}
	
	@Test
	public void registerBeanWithDependency(){
		StaticApplicationContext ac = new StaticApplicationContext();	// IoC 컨테이너 생성. 생성과 동시에 컨테이너로 동작한다.
		
		// StringPrinter 클래스 타입이며 printer 라는 이름을 가진 빈을 등록한다.
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
		
		// 빈메타정보를 담은 오브젝트를 만든다. 빈클래스는 Hello로 지정한다.
		// <bean class="springbook.learningtest..Hello"/> 에 해당하는 메타정보다.
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		
		// 빈의 name 프로퍼티에 들어갈 값을 지정한다. <property name="name" value="Spring"/> 에 해당한다.
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		
		// 아이디가 printer인 빈에 대한 레퍼런스를 프로퍼티로 등록
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
		
		// XmlBeanDefinitionReader는 기본적으로 클래스패스로 정의된리소스로 부터 파일을 읽는다.
		reader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		ac.refresh();  //모든 메타정보가 등록이 완료했으니 애플리케이션 컨테이너를 초기화하라는 명령이다.
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
	}
	
	@Test
	public void genericXmlApplicationContext() {
		GenericApplicationContext ac = new GenericXmlApplicationContext(
				"springbook/learningtest/spring/ioc/genericApplicationContext.xml");
		// application context 생성과 동시에 xml 파일을 읽어오고 초기화까지 수행한다.
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		assertThat( ac.getBean("printer").toString() , is("Hello Spring"));
	}
	
	@Test
	public void contextHierachy(){
		// 계층구조 applicationContext test
		System.out.println("basePath==="+basePath);
		ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
		GenericApplicationContext child = new GenericApplicationContext(parent);
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
		reader.loadBeanDefinitions(basePath+"childContext.xml");
		child.refresh(); // 리더를 사용해서 설정을 읽은 경우에는 반드시 refresh() 를 통해 초기화해야 한다.
		
		Printer printer  = child.getBean("printer", Printer.class);
		assertThat(printer, is(notNullValue()));
		
		Hello hello = child.getBean("hello", Hello.class);
		assertThat(hello, is(notNullValue()));
		
		hello.print();
		assertThat(printer.toString(), is("Hello Child"));	// getBean()으로 가져온 hello빈은 자식 컨택스트에 존재하는 것임을 확인할 수 있다.
	}
}
