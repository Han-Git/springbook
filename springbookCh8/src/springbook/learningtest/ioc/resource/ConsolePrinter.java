package springbook.learningtest.ioc.resource;

public class ConsolePrinter implements Printer{

	public void print(String message) {
		System.out.println(message);
	}
}
