package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	
	// p253 after applying Template/Callback
	public Integer calcSum(String filepath)throws IOException {
		/*	//p253
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
		*/
		/*
		BufferedReaderCallback sumCallback = 	//#p253
			new BufferedReaderCallback(){	//#p253
				public Integer doSomethingWithReader(BufferedReader br) throws IOException {	//#p253
					Integer sum = 0;	//#p253
					String line = null;	//#p253
					while((line = br.readLine()) != null){	//#p253
						sum += Integer.valueOf(line);	//#p253
					}	//#p253
					return sum;	//#p253
				}	//#p253
			};	//#p253
		return fileReadTemplate(filepath, sumCallback);	//#p253
		*/
		
		// p256 revised that using lineReadTemplate()
		LineCallback sumCallback = new LineCallback(){
			public Integer doSomethingWithLine(String line, Integer value){
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath,sumCallback, 0);
	}
	
	// p254 has the multiply function callback
	public Integer calcMultiply(String filepath)throws IOException{	//#p254
		/*
		BufferedReaderCallback multiplyCallback = new BufferedReaderCallback(){	//#p254
			public Integer doSomethingWithReader(BufferedReader br) throws IOException{	//#p254
				Integer multiply = 1;	//#p254
				String line = null;	//#p254
				while((line = br.readLine()) != null){	//#p254
					multiply *= Integer.valueOf(line);	//#p254
				}
				return multiply;	//#p254
			}
		};
			
		return fileReadTemplate(filepath, multiplyCallback);	//#p254
		*/
		LineCallback multiplyCallback = new LineCallback(){
			public Integer doSomethingWithLine(String line, Integer value){
				return value * Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, multiplyCallback, 1);
	}

	// p252 Template Method which uses BufferedReaderCallback
	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filepath));
			int ret = callback.doSomethingWithReader(br);
			return ret;
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally {
			if(br != null){
				try{
					br.close();
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	// p255 Template using LineCallback Template
	public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal)throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(filepath));
			Integer res = initVal;
			String line = null;
			while((line = br.readLine()) != null){
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally {
			if(br != null){
				try{
					br.close();
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
