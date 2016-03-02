package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filepath)throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filepath));	//#p249
		Integer sum = 0;	//#p249
		String line = null;	//#p249
		while((line = br.readLine()) != null){	//#p249
			sum += Integer.valueOf(line);	//#p249
		}
		
		br.close();	//#p249
		
		return sum;	//#p249
	}
	
}
