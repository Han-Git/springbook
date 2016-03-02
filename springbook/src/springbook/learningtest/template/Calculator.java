package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filepath)throws IOException {
		BufferedReader br = null;	// #p250
		try{ 
			br = new BufferedReader(new FileReader(filepath));	//#p250
			Integer sum = 0;	//#p249
			String line = null;	//#p249
			while((line = br.readLine()) != null){	//#p249
				sum += Integer.valueOf(line);	//#p249
			}
			
			//br.close();	//#p249
			
			return sum;	//#p249
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally {
			if(br != null){
				try{br.close();}	//#p250
				catch(IOException e){System.out.println(e.getMessage());}
			}
		}
	}
	
}
