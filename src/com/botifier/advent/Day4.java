package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Day4 {

	public Day4() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day4_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			int i = 0;
			int e = 0;
			while ((s = r.readLine()) != null) {
				String[] odd = s.split(",");

				String[] range = odd[0].split("-");
				int start1 = Integer.valueOf(range[0]);
				int end1 = Integer.valueOf(range[1]);
				
				String[] range2 = odd[1].split("-");
				int start2 = Integer.valueOf(range2[0]);
				int end2 = Integer.valueOf(range2[1]);
				
				if (overlap_full(start1, end1, start2, end2)) {
					i++;
				}
				if (overlap(start1, end1, start2, end2)) {
					e++;
				}
			}
			System.out.println(i);
			System.out.println(e);
		}
	}
	
	public static boolean overlap_full(int s1, int e1, int s2, int e2) {
		if ((s1 >= s2 && s1 <= e2) && (e1 >= s2 && e1 <= e2))
			return true;
		if ((s2 >= s1 && s2 <= e1) && (e2 >= s1 && e2 <= e1))
			return true;
		
		return false;
	}
	
	public static boolean overlap(int s1, int e1, int s2, int e2) {
		if ((s1 >= s2 && s1 <= e2) || (e1 >= s2 && e1 <= e2))
			return true;
		if ((s2 >= s1 && s2 <= e1) || (e2 >= s1 && e2 <= e1))
			return true;
		
		return false;
	}

}
