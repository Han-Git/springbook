package springbook.learningtest.template;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	Calculator calculator;	//#p253
	String numFilepath;	//#p253
	
	@Before
	public void setUp(){	//#p253
		this.calculator = new Calculator();	//#p253
		this.numFilepath = getClass().getResource("numbers.txt").getPath();	//#p253
	}
	
	@Test
	public void sumOfNumbers() throws IOException{
		//Calculator calculator = new Calculator();	//#p249
		//int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());	//#p249
		//assertThat(sum, is(10));	//#p249
		assertThat(calculator.calcSum(this.numFilepath), is(10));	//#p253
	}
	
	// p253 added a new test method
	@Test
	public void multiplyOfNumbers() throws IOException{
		assertThat(calculator.calcMultiply(this.numFilepath),is(24));
	}
}
