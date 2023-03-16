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

public class Day5 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<ArrayList<Character>> crates = new ArrayList<ArrayList<Character>>();
		
		crates.add(new ArrayList<Character>(Arrays.asList('C','F','B','L','D','P','Z','S')));
		crates.add(new ArrayList<Character>(Arrays.asList('B','W','H','P','G','V','N')));
		crates.add(new ArrayList<Character>(Arrays.asList('G','J','B','W','F')));
		crates.add(new ArrayList<Character>(Arrays.asList('S','C','W','L','F','N','J','G')));
		crates.add(new ArrayList<Character>(Arrays.asList('H','S','M','P','T','L','J','W')));
		crates.add(new ArrayList<Character>(Arrays.asList('S','F','G','W','C','B')));
		crates.add(new ArrayList<Character>(Arrays.asList('W','B','Q','M','P','T','H')));
		crates.add(new ArrayList<Character>(Arrays.asList('T','W','S','F')));
		crates.add(new ArrayList<Character>(Arrays.asList('R','C','N')));
		File f = new File("day5_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			while ((s = r.readLine()) != null) {
				s = s.replaceAll("move ", "").replaceAll("from ", "").replaceAll("to ", "");
				if (s.isEmpty())
					continue;
				
				String[] sep = s.split(" ");
				int from = Integer.valueOf(sep[1])-1;
				int use = Integer.valueOf(sep[0]);
				int to = Integer.valueOf(sep[2])-1;
				
				ArrayList<Character> crateFrom = crates.get(from);
				ArrayList<Character> crateTo = crates.get(to);
				
				int it = Math.min(crateFrom.size(), use);

				ArrayList<Character> hold = new ArrayList<Character>();
				for (int i = it-1; i >= 0; i--) {
					hold.add(crateFrom.get(0));
					crateFrom.remove(0);
				}
				crateTo.addAll(0,hold);
				System.out.println();
				for (int i = 0; i < crates.size(); i++) {


					System.out.print(i+":");
					for (int e = 0; e < crates.get(i).size(); e++) {
						System.out.print(crates.get(i).get(e)+" ");
					}
					System.out.print("("+crates.get(i).size()+")");
					System.out.println();
				}
				
			}
			
			System.out.println();
			
			for (int i = 0; i < crates.size(); i++) {


				System.out.print(crates.get(i).get(0));
			}
		}
	}

}
