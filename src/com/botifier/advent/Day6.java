package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day6 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		File f = new File("day6_input");
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = r.readLine();
			
			String chars = "";
			final int length = 14;

			//I overthought this originally took way too long
			for (int i = 0; i < s.length(); i++) {
				chars = s.charAt(i) + chars;
				if (chars.length() < length)
					continue;
				if (chars.length() > length) {
					chars = chars.substring(0, length);
				}
				if (eachUnique(chars)) {
					System.out.println(i+1);
					break;
				}
			}
		}
	}
	
	public static boolean eachUnique(String s) {
		for (int i = 0; i < s.length(); i++) {
			for (int e = 0; e < s.length(); e++) {
				if (e == i)
					continue;
				if (s.charAt(e) == s.charAt(i)) {
					return false;
				}
			}
		}
		return true;
	}

}
