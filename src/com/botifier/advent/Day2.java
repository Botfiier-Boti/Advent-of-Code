package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Day2 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day2_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			int pscore = 0;
			int escore = 0;
			while ((s = r.readLine()) != null) {
				String[] actions = s.split("\\s");
				int tescore = score(actions[0]);
				int tpscore = score(actions[1]);
				
				pscore += match(tescore, tpscore);
			}
			
			System.out.println(pscore);
		}
	}

	
	public static int match(int enemy, int toMatch) {
		int score = 0;
		switch (toMatch) {
			case 1:
				score = prev(enemy);
				break;
			case 2:
				score = enemy;
				break;
			case 3:
				score = next(enemy);
				break;
		}
		score += win(score, enemy);
		return score;
	}
	
	public static int next(int in) {
		return in < 3 ? in+1 : 1;
	}
	public static int prev(int in) {
		return in > 1 ? in-1 : 3;
	}
	
	public static int score(String input) {
		return input.charAt(0) == 'A' || input.charAt(0) == 'X'  ? 1 : input.charAt(0) == 'B' || input.charAt(0) == 'Y' ? 2 : 3;
	}
	
	public static int win(int p, int e) {
		if (p == e) {

			System.out.println("tie");
			return 3;
		}
		if ((p == 1 && e == 3) || (p == 3 && e == 2) || (p == 2 && e == 1)) {
			System.out.println("win");
			return 6;
		}

		System.out.println("lose");
		return 0;
	}
}
