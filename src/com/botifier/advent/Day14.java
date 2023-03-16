package com.botifier.advent;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.swing.event.ListSelectionEvent;
import javax.swing.text.Position.Bias;

public class Day14 {


	static int maxX = 0;
	static int minX = Integer.MAX_VALUE;
	static int maxY = 0;
	static int minY = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day14_input");
		
		
		
		
		char[][] rock;
		
		Position spawn = new Position(500, 0);
		
		ArrayList<Line> store = new ArrayList<Line>();
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			
			while ((s = r.readLine()) != null) {
				String[] spl= s.split(" -> ");
				Line l = new Line();
				
				for (String o : spl) {
					//System.out.println(o);
					String[] pos = o.split(",");
					
					int x = Integer.valueOf(pos[0]);
					int y = Integer.valueOf(pos[1]);
					
					if (x > maxX)
						maxX = x;
					if (x < minX)
						minX = x;
					if (y > maxY)
						maxY = y;
					
					Position p = new Position(x, y);
					
					l.addPosition(p);
				}
				store.add(l);
			}
			
			maxY += 1;
			minX -= 300;
			maxX += 300;
			
			rock = new char[maxY+1][maxX-minX+1];
			

			spawn.subtract(minX, 0);
			for (Line li : store) {
				li.subtractAll(minX, 0);
				rock = li.drawLine(rock);
			}
			
			Sand sand = new Sand(spawn.x, 0, rock);
			int counter = 0;
			do {
				sand = new Sand(spawn.x, 0, rock);
				while (!sand.rest) {
					rock = sand.fall();
					/*for (int y = 0; y < rock.length; y++) {
						for (int x = 0; x < rock[0].length; x++) {
							if  (rock[y][x] != 0)
								System.out.print(rock[y][x]);
							else
								System.out.print('.');
						}
						System.out.println();
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
				if (sand.rest)
					counter++;
			} while ((sand.getPosition().x != spawn.x || sand.getPosition().y != 0));
			for (int y = 0; y < rock.length; y++) {
				for (int x = 0; x < rock[0].length; x++) {
					if  (rock[y][x] != 0)
						System.out.print(rock[y][x]);
					else
						System.out.print('.');
				}
				System.out.println();
			}
			System.out.println(counter);
			
			
			
		}
	}
	
	public static class Sand {
		Position p;
		char[][] toUse;
		boolean rest = false;
		boolean fellOff = false;
		
		public Sand(int x, int y, char[][] toUse) {
			p = new Position(x, y);
			this.toUse = toUse;
		}
		
		public char[][] fall() {
			if (rest)
				return toUse;
			if (p.y+1 < toUse.length && toUse[p.y+1][p.x] == 0) {
				toUse[p.y][p.x] = 0;
				p.add(0, 1);
				toUse[p.y][p.x] = 'o';
			} else if (p.y+1 < toUse.length && p.x-1 >= 0 && toUse[p.y+1][p.x-1] == 0){
				toUse[p.y][p.x] = 0;
				p.add(-1, 1);
				toUse[p.y][p.x] = 'o';
			} else if (p.y+1 < toUse.length && p.x+1 < toUse[0].length && toUse[p.y+1][p.x+1] == 0){
				toUse[p.y][p.x] = 0;
				p.add(1, 1);
				toUse[p.y][p.x] = 'o';
			} else if (p.y+1 > toUse.length-1) {
				//System.out.println("byebye");
				rest = true;
			} else {
				rest = true;
			}
			return toUse;
		}
		
		public Position getPosition() {
			return p;
		}
	}
	
	public static class Line {
		ArrayList<Position> v = new ArrayList<Position>();
		
		public char[][] drawLine(char[][] array) {
			char[][] copy = new char[array.length][array[0].length];
			for (int y = 0; y < array.length; y++) {
				for (int x = 0; x < array[0].length; x++) {
					copy[y][x] = array[y][x];
				}
			}
			
			for (int i = 0; i < v.size()-1; i++) {
				Position v = this.v.get(i);
				Position v2 = this.v.get(i+1);
				

				copy[v.y][v.x] = '#';
				copy[v2.y][v2.x] = '#';
				
				
				if (v.y < v2.y) {
					for (int y = v.y; y <= v2.y; y++) {
						//System.out.println((v.y+y)+","+(v.x));
						copy[y][v.x] = '#';
					}
				} else {
					for (int y = v.y; y >= v2.y; y--) {
						//System.out.println((v.y+y)+","+(v.x));
						copy[y][v.x] = '#';
					}
				}
				if (v.x < v2.x) {
					for (int x = v.x; x <= v2.x; x++) {
						//System.out.println((v.y)+","+(v.x+x));
						copy[v.y][x] = '#';
					}
				} else {
					for (int x = v.x; x >= v2.x; x--) {
						//System.out.println((v.y)+","+(v.x+x));
						copy[v.y][x] = '#';
					}
				}
			}
			return copy;
		}
		
		public void subtractAll(int x, int y) {
			for (Position p : v) {
				p.subtract(x, y);
				//System.out.println(p);
			}
		}
		
		public void addPosition(Position v) {
			this.v.add(v);
		}
		
		public ArrayList<Position> getVectors() {
			return v;
		}
	}
	
	public static class Position {
		int x;
		int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void add(int x, int y) {
			this.x += x;
			this.y += y;
		}
		
		public void subtract(int x, int y) {
			this.x -= x;
			this.y -= y;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int distance(int x2, int y2) {
			int x1 = Math.min(x, x2);
			int x2h = Math.max(x, x2);
			
			int y1 = Math.min(y, y2);
			int y2h = Math.max(y, y2);
			return (int) Math.sqrt((x2h-x1)*(x2h-x1)+(y2h - y1)*(y2h - y1));
		}
		
		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
	}
	
}
