package springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.either;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)	//#p203
@ContextConfiguration	//#p203
public class JUnitTest {
	@Autowired	//#p203
	ApplicationContext context;	//#p203
	
	//static JUnitTest testObject;	//#p201
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();	//#202
	static ApplicationContext contextObject = null;	//#p203	
	
	@Test
	public void test1(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
		
		assertThat(contextObject == null || contextObject == this.context, is(true));	//#p203	
		contextObject = this.context;	//#p203	
	}
	
	@Test
	public void test2(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
		
		assertTrue(contextObject == null || contextObject == this.context);	//#p203	
		contextObject = this.context;	//#p203	
	}
	
	@Test
	public void test3(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
		
		assertThat(contextObject, either(is(nullValue())).or(is(this.context)));	//#p203	
		contextObject = this.context;	//#p203	
	}
}
