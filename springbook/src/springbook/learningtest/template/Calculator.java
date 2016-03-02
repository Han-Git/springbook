package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

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
	}
	
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
}
