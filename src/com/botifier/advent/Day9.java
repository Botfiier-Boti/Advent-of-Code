package com.botifier.advent;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.Position.Bias;

public class Day9 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day9_input");
		
		
		char[][] posi = new char[5000][5000];
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			Body head = new Body('H', 250, 250);
			Body mid1 = new Body('1', head.getX(), head.getY());
			Body mid2 = new Body('2', head.getX(), head.getY());
			Body mid3 = new Body('3', head.getX(), head.getY());
			Body mid4 = new Body('4', head.getX(), head.getY());
			Body mid5 = new Body('5', head.getX(), head.getY());
			Body mid6 = new Body('6', head.getX(), head.getY());
			Body mid7 = new Body('7', head.getX(), head.getY());
			Body mid8 = new Body('8', head.getX(), head.getY());
			Body tail = new Body('9', head.getX(), head.getY());
			head.moved = true;
			head.addFollower(mid1);
			head.addFollower(mid2);
			head.addFollower(mid3);
			head.addFollower(mid4);
			head.addFollower(mid5);
			head.addFollower(mid6);
			head.addFollower(mid7);
			head.addFollower(mid8);
			head.addFollower(tail);

			//mid1.addFollower(mid2);
			//mid2.addFollower(mid3);
			//mid3.addFollower(mid4);
			//mid4.addFollower(mid5);
			//mid5.addFollower(mid6);
			//mid6.addFollower(mid7);
			//mid7.addFollower(mid8);
			//mid8.addFollower(tail);
			
			ArrayList<Body> lazy = new ArrayList<Body>();
			lazy.add(head);
			lazy.add(mid1);
			lazy.add(mid2);
			lazy.add(mid3);
			lazy.add(mid4);
			lazy.add(mid5);
			lazy.add(mid6);
			lazy.add(mid7);
			lazy.add(mid8);
			lazy.add(tail);
			
			while ((s = r.readLine()) != null) {
				String[] split = s.split(" ");
				char dir = split[0].charAt(0);
				int amount = Integer.valueOf(split[1]);

				//posi[tail.getY()][tail.getX()] = tail.symbol;
				//posi[head.getY()][head.getX()] = head.symbol;
				//System.out.println(dir+", "+amount);

				//printArray(posi);
				for (int i = 0; i < amount; i++) {
					

					for (int o = lazy.size()-1; o >= 0; o--) {
						Body b = lazy.get(o);
						if (posi[b.getY()][b.getX()] == b.symbol)
							posi[b.getY()][b.getX()] = (char) (0);
					}
					head.step(dir);	
					for (int o = lazy.size()-1; o >= 0; o--) {
						Body b = lazy.get(o);
						posi[b.getY()][b.getX()] = b.symbol;
					}

					//printArray(posi);
				}
			}
			//printArray(posi);
			System.out.println(tail.visited.size());
			
		}
	}
	
	public static void printArray(char[][] posi) {
		
		for (int y = posi.length-1; y >= 0 ; y--)  {
			for (int x = 0; x < posi.length; x++) {
				if (posi[y][x] == 0)
					System.out.print('-');
				else
					System.out.print(posi[y][x]);
			}
			System.out.print('\n');
		}
		System.out.println();
	}
	
	
	public static class Position implements Cloneable {
		public int x;
		public int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "["+x+","+y+"]";
		}
		
		
		public boolean equals(Position p) {
			if (p.x == x && p.y == y)
				return true;
			return false;
		}
		
	}
	
	public static class Body {
		ArrayList<Position> visited = new ArrayList<Position>(); 
		ArrayList<Body> followers = new ArrayList<Body>();
		Position pos;
		char symbol;
		boolean moved = false;
		
		public Body(char symbol, int x, int y) {
			this.symbol = symbol;
			this.pos = new Position(x,y);
		}
		
		public void step(char dir) {
			switch (dir) {
			case 'U':
				pos.y++;
				break;
			case 'D':
				pos.y--;
				break;
			case 'L':
				pos.x--;
				break;
			case 'R':
				pos.x++;
				break;
		}
		if (!followers.isEmpty()) {
			for (int i = 0; i < followers.size(); i++) {
				
				Body b = followers.get(i);
				if (i == 0)
					b.follow(this);
				else {
					b.follow(followers.get(i-1));
				}
			}
				
		}
		}
		
		private void follow(Body b) {
			int x1 = pos.x;
			int y1 = pos.y;
			int x2 = b.pos.x;
			int y2 = b.pos.y;

			int sub1 = (y2-y1);
			int sub2 = (x2-x1);
			
			if (sub2 > 1) {
				if (x1 != x2 && y1 != y2) {
					if (y1 > y2)
						step('D');
					if (y2 > y1)
						step('U');
				}
				step('R');
			} else if (sub2 < -1) {
				if (x1 != x2 && y1 != y2) {
					if (y1 > y2)
						step('D');
					if (y2 > y1)
						step('U');
				}
				step('L');
			} else if (sub1 > 1) {
				if (x1 != x2 && y1 != y2) {
					if (x1 > x2)
						step('L');
					if (x2 > x1)
						step('R');
				}

				step('U');
			} else if (sub1 < -1) {
				if (x1 != x2 && y1 != y2) {
					if (x1 > x2)
						step('L');
					if (x2 > x1)
						step('R');
				}
				step('D');
			}
			
			
			
			
			
			if (!contains(visited, pos)) {
				visited.add(new Position(pos.x, pos.y));
			}
		}
		
		public int getX() {
			return pos.x;
		}
		
		public int getY() {
			return pos.y;
		}
		
		public void addFollower(Body b) {
			followers.add(b);
		}
		
		public ArrayList<Body> getFollowers() {
			return followers;
		}
		
		@Override
		public String toString() {
			return symbol+"";
		}
	}
	
	public static boolean contains(ArrayList<Position> a, Position p2) {
		for (Position p : a) {
			if (p.equals(p2))
				return true;
		}
		return false;
	}
}
