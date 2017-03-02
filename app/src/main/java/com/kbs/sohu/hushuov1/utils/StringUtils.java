package com.kbs.sohu.hushuov1.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {
	/**
	 * 把一个输入流中的数据读取成字符串
	 * @return
	 * @throws IOException 
	 */
	public static String isToString(InputStream is) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line=reader.readLine()) != null){
			sb.append(line+"\n");
		}
		return sb.toString();
	}
	
	
}
