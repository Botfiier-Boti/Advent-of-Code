package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.botifier.advent.Day14.Line;
import com.botifier.advent.Day14.Position;
import com.botifier.advent.Day14.Sand;

public class Day15 {

	static int maxX = 0;
	static int minX = Integer.MAX_VALUE;
	static int maxY = 0;
	static int minY = Integer.MAX_VALUE;
	static int y = 0;
	static int x = 0;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day15_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			
			Sensor hold = null;
			
			ArrayList<MapValue> sAndB = new ArrayList<MapValue>();
			
			char[][] screen;
			
			while ((s = r.readLine()) != null) {
				s = s.toLowerCase();
				s = s.replace("closest", "");
				s = s.replace("is", "");
				s = s.replace("at", "");
				s = s.replace("x=", "");
				s = s.replace("y=", "");
				
				
				
				String[] spl= s.split(":");
				for (String s2 : spl) {
					int use = -1;
					if (s2.contains("sensor")) {
						s2 = s2.replace("sensor", "");
						use = 0;
					}
					if (s2.contains("beacon")) {
						s2 = s2.replace("beacon", "");
						use = 1;
					}
					s2 = s2.replace(" ", "");
					
					String[] sp2 = s2.split(",");
					
					int x = Integer.valueOf(sp2[0]);
					int y = Integer.valueOf(sp2[1]);
					
					if (x > maxX)
						maxX = x;
					if (x < minX)
						minX = x;
					if (y > maxY)
						maxY = y;
					if (y < minY)
						minY = y;
					
					switch (use) {
						case 0:
							hold = new Sensor(x, y);
							sAndB.add(hold);
							break;
						case 1:
							Beacon b = new Beacon(x, y);
							hold.setClosest(b);
							sAndB.add(b);
							break;
					}
				}
			}
			
			//screen = new char[maxY-(minY)+1][maxX-(minX)+1];

			HashSet<Integer> toCheck = new HashSet<Integer>();
			HashSet<Position> toCheckB = new HashSet<Position>();
			HashSet<Position> toCheckBI = new HashSet<Position>();
			
			int count = 0;
			

			Set<Sensor> sens = sAndB.stream().filter((m) -> m instanceof Sensor).map(Sensor.class::cast).collect(Collectors.toSet());
			
			Set<Beacon> bea = sAndB.stream().filter((m) -> m instanceof Beacon).filter((m) -> m.p.x >= 0 && m.p.x <= 4000000 && m.p.y >= 0 && m.p.y <= 4000000).map(Beacon.class::cast).collect(Collectors.toSet());
			

			/*int c = 0;
			for (Sensor se : sens) {
				Position p = se.getPosition();
				for (int i = -se.dist; i < se.dist; i++) {
					if (se.validate(p.x+i, 2000000) == true) {
						if (toCheck.add(p.x+i)) {
						}
					}
				}
			}*/
			

			long minBX = Integer.MAX_VALUE;
			long maxBX = 0;
			long minBY = Integer.MAX_VALUE;
			long maxBY = 0;
			
			for (Sensor se : sens) {
				
				//Set<Position> valid = se.getValid();
				//System.out.println(valid);
				//toCheckBI.addAll(valid);
				
				long sxL = se.getPosition().x - se.dist;
				long sxG = se.getPosition().x + se.dist;
				long syL = se.getPosition().y - se.dist;
				long syG = se.getPosition().y + se.dist;
				
				if (sxL >= 0 && sxL < minBX) {
					//System.out.println(sxL);
					minBX = se.getPosition().x - se.dist;
				}
				if (sxG <= 4000000 && sxG > maxBX) {
					maxBX = se.getPosition().x + se.dist;
				}
				if (syL < minBY) {
					if (se.getPosition().y - se.dist >= 0) {
						System.out.println(se.getPosition().y);
						minBY = se.getPosition().x - se.dist;
					}
				}
				if (syG <= 4000000 && syG > maxBY) {
					maxBY = se.getPosition().y + se.dist;
				}
				toCheckBI.add(se.getPosition());
			}
			
			
			
			
			Position distress;
			long actions = 0;
			Set<Sensor> so = sens.stream()
					.filter((p) -> 4000000 >= p.getPosition().x+p.dist && 0 <= p.getPosition().x-p.dist)
					.filter((p) -> 4000000 >= p.getPosition().y+p.dist && 0 <= p.getPosition().y-p.dist)
					.collect(Collectors.toSet());
			// mSystem.out.println(minBY);
			for (y = (int) minBY; y < maxBY; y++) {
				
				//System.out.println(so.size());
				if (so.size() <= 0) {
					//System.out.println(y);
					continue;
				}
				//System.out.println(y);
				boolean br = false;
				for (x = 0; x < maxBX; x++) {
					if (toCheckBI.contains(new Position(x, y)))
						continue;
					//Set<Sensor> so2 = so.stream().filter((p) -> x <= p.getPosition().x+p.dist && x >= p.getPosition().x-p.dist).collect(Collectors.toSet());
					if (so.size() <= 0)
						continue;
					//System.out.println(x);
					for (Sensor soo : so) {
						if (soo.validate(x, y))
							toCheckB.add(new Position(x, y));
						else
							toCheckBI.add(new Position(x, y));

						actions++;
					}

					if (br)
						break;
				}
				System.out.println(y);
				if (br)
					break;
			}
			toCheckB.removeAll(toCheckBI);

			System.out.println(actions);
			System.out.println(toCheckB);
			
			for (Position se : toCheckB) {
				//System.out.println((se.x * 4000000) + se.y);
				
			}

			
			/*for (MapValue mv : sAndB) {
				//mv.getPosition().subtract(minX, minY);
				//screen = mv.draw(screen);
				//screen = mv.detect(screen);
				
				Position p = mv.getPosition();
				
				if (mv instanceof Sensor) {
					Sensor sen = (Sensor) mv;
					for (int i = -sen.dist; i < sen.dist; i++) {
						if (mv.validate(p.x+i, 2000000) == false)
							if (toCheck.add(p.x+i)) {
								//System.out.println((p.x+i)+","+10);
							}
					}
						
				}
				screen = mv.draw(screen);
				screen = mv.detect(screen);
				
			}
			int col = minY;
			int row = minX;
			for (int y = 0; y < screen.length; y++) {
				String cSt = (col++) + ":\t";
				if (y == 0) {
					for (int i = 0; i < cSt.length(); i++) {
						if (cSt.charAt(i) == '\t')
							for (int e = 0; e < 8-cSt.length(); e++)
								System.out.print(" ");
						System.out.print(" ");
					}
					for (int x = 0; x < screen[0].length; x++) {
						System.out.print(Math.abs(row++) % 10);
					}
					System.out.println();
				}
				System.out.print(cSt);
				for (int x = 0; x < screen[0].length; x++) {
					if (screen[y][x] != 0)
						System.out.print(screen[y][x]);
					else
						System.out.print(".");
				}
				System.out.println();
			}*/
			
			//System.out.println(toCheck.size()-1);
			
		}
	}
	
	public static class Sensor extends MapValue {
		Set<Position> valid = new HashSet<Position>();
		Beacon closest;
		int dist;
		
		public Sensor(int x, int y) {
			super('S', x, y);
		}
		
		public void setClosest(Beacon b) {
			closest = b;
			dist = getPosition().manhattanDist(closest.getPosition());
		}
		
		public Beacon getClosest() {
			return closest;
		}
		
		public Set<Position> getValid() {
			valid.clear();
			dist = getPosition().manhattanDist(closest.getPosition());
			valid.add(new Position(getPosition().x, getPosition().y-dist));
			valid.add(new Position(getPosition().x, getPosition().y+dist));
			valid.add(new Position(getPosition().x-dist, getPosition().y));
			valid.add(new Position(getPosition().x+dist, getPosition().y));
			return valid;
		}

		@Override
		public char[][] detect(char[][] in) {
			dist = getPosition().manhattanDist(closest.getPosition());
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					char c = in[y][x-minX];
					if (getPosition().manhattanDist(x, y) <= dist && c != 'S' && c != 'B')
						in[y - minY][x - minX] = '#';
				}
			}
			return in;
		}

		@Override
		public boolean validate(int x, int y) {
			dist = getPosition().manhattanDist(closest.getPosition());
			int dist1 = getPosition().manhattanDist(x, y);
			if (dist1 <= dist)
				return false;
			return true;
		}
		
		
	}
	
	public static class Beacon extends MapValue {
		
		
		public Beacon(int x, int y) {
			super ('B', x, y);
		}

		@Override
		public char[][] detect(char[][] in) {
			return in;
		}

		@Override
		public boolean validate(int x, int y) {
			return true;
		}
	}

	public static abstract class MapValue {
		Position p;
		char c;
		
		public MapValue(char c, int x, int y) {
			this.c = c;
			p = new Position(x, y);
		}
		
		public abstract char[][] detect(char[][] in);
		
		public abstract boolean validate(int x, int y);
		
		public boolean validate(Position p) {
			return validate(p.x, p.y);
		}
		
		public char[][] draw(char[][] in) {
			in[p.y - minY][p.x - minX] = c;
			return in;
		}
		
		public Position getPosition() {
			return p;
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
		
		public int manhattanDist(Position p) {
			return manhattanDist(p.x, p.y);
		}
		
		public int manhattanDist(int x2, int y2) {
			return Math.abs(x-x2)+Math.abs(y-y2);
		}
		
		public int distance(int x2, int y2) {
			int x1 = Math.min(x, x2);
			int x2h = Math.max(x, x2);
			
			int y1 = Math.min(y, y2);
			int y2h = Math.max(y, y2);
			return (int) Math.sqrt((x2h-x1)*(x2h-x1)+(y2h - y1)*(y2h - y1));
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Position))
				return false;
			Position p = (Position)o;
			if (p.x == x && p.y == y)
				return true;
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(x)+Integer.hashCode(y);
		}
		
		
		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
	}
}
