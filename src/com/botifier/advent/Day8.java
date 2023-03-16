package com.botifier.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Day8 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		java.io.File f = new java.io.File("day8_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			
			String s = "";
			ArrayList<Tree[]> trees = new ArrayList<Tree[]>();
			
			int count = 0;
			int cY = 0;
			
			while ((s = r.readLine()) != null) {
				Tree[] temp = new Tree[s.length()];
			
				for (int i = 0; i < s.length(); i++) {
					int size = Integer.valueOf(s.charAt(i)+"");
					temp[i] = new Tree(size, i);
				}
				
				trees.add(temp);
				cY++;
			}
			
			int maxScore = 0;
			for (int i = 0; i < trees.size(); i++) {
				Tree[] t = trees.get(i);

				System.out.print("[");
				for (int e = 0; e < t.length; e++) {
					Tree tr = t[e];
					int score = tr.score(trees, i);
					if (score > maxScore)
						maxScore = score;
					if (tr.visible(trees, i)) {

						System.out.print(tr+"V ");
						count++;
					} else {
						System.out.print(tr+"  ");
					}
					
				}

				System.out.print("]\n");
			}
			System.out.println("visible: "+count);
			
			
			
			System.out.println("max score: "+maxScore);
		}
	}
	
	public static class Tree implements Comparable<Tree> {

		boolean visible = false;
		
		int height;
		int x;
		
		public Tree(int height, int x) {
			this.height = height;
			this.x = x;
		}
		
		public boolean isVisible() {
			return visible;
		}
		
		public int getHeight() {
			return height;
		}

		public int getX() {
			return x;
		}
		
		public int score(ArrayList<Tree[]> trees, int pos) {
			
			Tree[] on = trees.get(pos);
			
			int scoreLeft = 0;
			int scoreRight = 0;
			int scoreUp = 0;
			int scoreDown = 0;
			
			for (int i = x-1; i >= 0; i--) {
				Tree t = on[i];
				scoreLeft++;
				if (t.getHeight() >= getHeight()) {
					break;
				} 
			}
			for (int i = x+1; i < trees.size(); i++) {
				Tree t = on[i];

				scoreRight++;
				if (t.getHeight() >= getHeight()) {
					break;
				} 
			}
			
			int ab = pos-1;
			while (ab >= 0) {
				Tree[] above = trees.get(ab--);

				scoreUp++;
				if (above[x].getHeight() >= getHeight()) {
					break;
				} 
			}
			
			int bl = pos+1;
			while (bl < trees.size()) {
				Tree[] below = trees.get(bl++);

				scoreDown++;
				if (below[x].getHeight() >= getHeight()) {
					break;
				}
			}

			System.out.print(scoreUp+",");
			return scoreUp * scoreDown * scoreLeft * scoreRight;
		}
		
		public boolean visible(ArrayList<Tree[]> trees, int pos) {
			if (pos <= 0 || pos >= trees.size()-1)
				return true;
			if (x == 0 || x == trees.get(pos).length-1)
				return true;
			boolean holdLeft = true;
			boolean holdRight = true;
			boolean holdAbove = true;
			boolean holdBelow = true;
			
			Tree[] on = trees.get(pos);
			for (int i = 0; i < x; i++) {
				Tree t = on[i];
				if (t.getHeight() >= getHeight()) {
					holdLeft = false;
				}
			}
			for (int i = on.length-1; i > x; i--) {
				Tree t = on[i];
				if (t.getHeight() >= getHeight()) {
					holdRight = false;
				}
			}
			
			int ab = pos-1;
			while (ab >= 0) {
				Tree[] above = trees.get(ab--);

				if (above[x].getHeight() >= getHeight()) {
					holdAbove = false;
					break;
				}
			}
			
			int bl = pos+1;
			while (bl < trees.size()) {
				Tree[] below = trees.get(bl++);

				if (below[x].getHeight() >= getHeight()) {
					holdBelow = false;
					break;
				}
			}

			
			return visible = (holdLeft || holdRight || holdAbove || holdBelow);
		}
		
		@Override
		public String toString() {
			return height+"";
		}
		
		@Override
		public int compareTo(Tree t) {
			return x < t.x ? -1 : x > t.x ? 1 : 0;
		}
	}
}
