package com.schoolquiz.utils;

import java.util.Random;

public class SessionGenerator {
	
	private static int sessionLength = 20;
	private static char[] charSymbols  = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','R','S','T','U','V','W','Q','X','Y','Z'};
	
	public static String getNewSessionValue(){
		Random rand = new Random();
		StringBuilder sessionVal = new StringBuilder();
		int i;
		for(i=0;i<sessionLength;i++){
			int randLetterInt = rand.nextInt(2);
			if(randLetterInt==0){
				sessionVal.append(rand.nextInt(10));
			}
			if(randLetterInt==1){
				sessionVal.append(charSymbols[rand.nextInt(charSymbols.length)]);
			}
			
		}
		return sessionVal.toString();
	}

}
