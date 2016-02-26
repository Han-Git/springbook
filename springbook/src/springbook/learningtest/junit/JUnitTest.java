package springbook.learningtest.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class JUnitTest {
	//static JUnitTest testObject;	//#p201
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();	//#202
	
	@Test
	public void test1(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
	}
	
	@Test
	public void test2(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
	}
	
	@Test
	public void test3(){
//		assertThat(this, is(not(sameInstance(testObject))));	//#p201
//		testObject = this;	//#p201
		assertThat(testObjects, not(hasItem(this)));	//#202
		testObjects.add(this);	//#202
		System.out.println("testObjects.size()=="+testObjects.size());	//#202
	}
}
