package springbook.learningtest.template;

public interface LineCallback<T> {
	// p255 function for each lines
	//Integer doSomethingWithLine(String line, Integer value);
	
	//#p257 switch to generic 
	T doSomethingWithLine(String line, T value);
}
