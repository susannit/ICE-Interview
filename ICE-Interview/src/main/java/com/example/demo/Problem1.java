package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/*
 *Assumptions for CUSIP data reading
 *Input file is of very large size (> GB)
 *		Process input by stream reader so that it holds less in memory.
 *	IF the file size is more it has to be divided into chunks and can be processed in tool like 
 * Apache kafa for parallel streaming and processing.
 * 
 * The below program reads input file from the patch specified by stream processing
 * and check every line for CUSIP code (alpha numeric value).
 * else process the value as price and store it in a TreeMap for each CUSIP code.
 * 
 * Treeset by default will sort the input values ascending.
 * Fetch the last value in the set to get the highest price for the CUSIP
 * 
 * The below code is compiled in Java 8 version.
 * */
public class Problem1 {

	public static void main(String args[]) {

		
		Map <String, TreeSet<Double>> cusipMap = new HashMap<>();
		String[] key = new String[1];
		try {
			Files.lines(Paths.get("C:\\CUSIP.txt")).
			forEach(l -> {
				if (isCUSIP(l)) {
					if (cusipMap.get(l) == null )
						cusipMap.put(l, new TreeSet<Double>());
					key[0] = l;
				} else {
					cusipMap.get(key[0]).add(Double.valueOf(l));
						
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//print the max price for each cusip.
		cusipMap.entrySet().stream().forEach(e -> System.out.println(e.getValue().last()));
		
	}

	/*
	 * Assuming all CUSIP is of fixed length 8 character alpha numeric strings
	 * iterate through each character in the string and check for CUSIP ALL
	 * characters in CUSIP will be of UPPER Case.
	 */
	public static Boolean isCUSIP(String s) {
		if (s.length() != 8)
			return false;

		int sum = 0;
		for (int i = 0; i < 7; i++) {
			char c = s.charAt(i);
			int v;
			if (c >= '0' && c <= '9') {
				v = c - 48; // for number 0 - 9
			} else if (c >= 'A' && c <= 'Z') {
				v = c - 64; // check for characters
			} else {
				return false;
			}

			if (i % 2 == 1)
				v *= 2;
			sum += v / 10 + v % 10;
		}
		return s.charAt(8) - 48 == (10 - (sum % 10)) % 10;
	}

}
