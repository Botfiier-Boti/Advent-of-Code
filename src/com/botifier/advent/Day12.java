package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Day12 {
	static Node[][] tiles;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day12_input");
		
		
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			ArrayList<Player> players = new ArrayList<Player>();
			int tX = 0;
			int tY = 0;
			int y = 0;
			
			String[] st = r.lines().toArray(String[]::new);
			
			int length = st[0].length();
			tiles = new Node[st.length][length];
			for (int e = 0; e < st.length; e++)  {
				s = st[e];
				//System.out.println(s);
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					tiles[y][i] = new Node(c,i,e,null);
					if (c == 'S' || c == 'a') {
						players.add(new Player(i, e));
						tiles[y][i].c = 'a';
					}
					if (c == 'E') {
						tX = i;
						tY = e;
						tiles[y][i].c = 'z';
					}
				}
				y++;
			}
			System.out.println(players.size());
			int lowest = Integer.MAX_VALUE;
			for (Player p : players) {
				p.followPath(tX, tY);
				if (p.actions < lowest)
					lowest = p.actions;
				System.out.println(lowest);
				
			}
				
			

			
		}
	}
	
	public static double distance(int x, int y, int x2, int y2) {
		int x1 = Math.min(x, x2);
		int x2h = Math.max(x, x2);
		
		int y1 = Math.min(y, y2);
		int y2h = Math.max(y, y2);
		return Math.sqrt((x2h-x1)*(x2h-x1)+(y2h - y1)*(y2h - y1));
	}
	
	public static class Node implements Comparable<Node>{
		long dist = Integer.MAX_VALUE;
		char c;
		int x;
		int y;
		Node parent;
		
		public Node(char c, int x, int y, Node parent) {
			this.c = c;
			this.parent = parent;
			this.x  = x;
			this.y = y;
		}

		@Override
		public int compareTo(Node n) {
			return (int) (dist-(n.dist));
		}
		
		
	}
	
	public static class Player {
		Node[][] visited;
		char[][] path;
		
		int x, y;
		int actions = 0;
		
		
		public Player(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node findPath(int x2, int y2) {
			visited = new Node[tiles.length][tiles[0].length];
			
			for (int i = 0; i < visited.length; i++) {
				for (int e = 0; e < visited[0].length; e++) {
					visited[i][e] = tiles[i][e];
				}
			}
			
			visited[y][x] = tiles[y][x];
			visited[y][x].dist = 0;
			
			ArrayList<Node> unexplored = new ArrayList<Node>();
			for (int i = 0; i < visited.length; i++) {
				for (int e = 0; e < visited[0].length; e++)
					unexplored.add(visited[i][e]);
			}
			Node ret = null;
			while (!unexplored.isEmpty()) {
				Node current = unexplored.stream().min(Node::compareTo).get();
				unexplored.remove(current);
				
				if (current.x == x2 && current.y == y2) {
					return current;
				}
				
				Node u = null;
				Node d = null;
				Node l = null;
				Node r =null;
				
				if (current.y > 0)
					u = visited[current.y-1][current.x];
				if (current.y < visited.length-1)
					d = visited[current.y+1][current.x];
				if (current.x < visited[0].length-1)
					r = visited[current.y][current.x+1];
				if (current.x > 0)
					l = visited[current.y][current.x-1];
				
				
				if (u != null) {
					long nDist = (long) (current.dist + distance(current.x, current.y, u.x, u.y));
					if (u.c-current.c > 1 && current.c != 'S')
						nDist = Integer.MAX_VALUE;
					if (nDist < u.dist) {
						u.dist = nDist;
						u.parent = current;
					}
				}
				if (d != null) {
					long nDist = (long) (current.dist + distance(current.x, current.y, d.x, d.y));
					if (d.c-current.c > 1 && current.c != 'S')
						nDist = Integer.MAX_VALUE;
					if (nDist < d.dist) {
						d.dist = nDist;
						d.parent = current;
					}
				}
				
				if (l != null) {
					long nDist = (long) (current.dist + distance(current.x, current.y, l.x, l.y));
					if (l.c-current.c > 1 && current.c != 'S')
						nDist = Integer.MAX_VALUE;
					if (nDist < l.dist) {
						l.dist = nDist;
						l.parent = current;
					}
				}
				
				if (r != null) {
					long nDist = (long) (current.dist + distance(current.x, current.y, r.x, r.y));
					if (r.c-current.c > 1 && current.c != 'S') {
						nDist = Integer.MAX_VALUE;
					}
					if (nDist < r.dist) {
						r.dist = nDist;
						r.parent = current;
					}
				}
			}
			
			return ret;
			
		}
		
		public void followPath(int x2, int y2) {
			System.out.println("start: "+x+", "+y);
			path = new char[tiles.length][tiles[0].length];
			Node current = findPath(x2, y2);
			
			ArrayList<Node> toFollow = new ArrayList<Node>();
			while (current != null) {
				toFollow.add(0, current);

				if (current.x == x && current.y == y)
					break;
				current = current.parent;
			}
			
			for (Node n : toFollow) {
				if (n.x == x && n.y == y)
					continue;
				System.out.println(n.c+", "+n.x+", "+n.y);
				
				if (n.y > y) {
					path[y][x] = 'V';
					y++;
				}else if (n.y < y) {
					path[y][x] = '^';
					y--;
				}else if (n.x < x) {
					path[y][x] = '<';
					x--;
				}else if (n.x > x) {
					path[y][x] = '>';
					x++;
				}
				
				actions++;
				
			}
			
			
			
			//System.out.println(actions);
		}
	}
	
}
