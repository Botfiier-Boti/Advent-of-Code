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

import javax.swing.event.ListSelectionEvent;
import javax.swing.text.Position.Bias;

public class Day10 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day10_input");
		
		
		
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			HashMap<String, Integer> commands = new HashMap<String, Integer>();
			commands.put("noop", 1);
			commands.put("addx", 2);
			
			int cycles = 0;
			int x = 1;
			
			
			ArrayList<String> com = new ArrayList<String>();
						
			while ((s = r.readLine()) != null) {
				com.add(s);
				cycles += commands.get(s.split(" ")[0]);
			}
			
			Sprite sp = new Sprite(1, 3);

			int div = cycles/6;
			ArrayList<Integer> spots = new ArrayList<Integer>(Arrays.asList(div*1, div*2, div*3, div*4, div*5, div*6));
			
			char[] hold = new char[div];
			
			int total = 0;
			int cycle = 0;
			int next = 0;
			int pos = 0;
			boolean skip = false;
			for (int i = 0; i < cycles; i++) {
				cycle++;
				
				if (spots.contains(cycle)) {
					total += (cycle * x);
					spots.remove((Integer)(cycle));

					System.out.println(hold);
					hold = new char[div];
				}
				
				
				if (skip) {
					skip = false;
					x += next;
					next = 0;

					sp.setX(x);
					char[] temp = sp.draw(div);
					if (temp[(cycle) % div] != 0) {
						hold[(cycle) % div] = '#';
					} else {
						hold[(cycle) % div] = '.';
					}
					continue;
				} else {
					sp.setX(x);
					char[] temp = sp.draw(div);
					if (temp[(cycle) % div] != 0) {
						hold[(cycle) % div] = '#';
					} else {
						hold[(cycle) % div] = '.';
					}
				}
				
				String s1 = com.get(pos++);
				String[] split = s1.split(" ");
				
				String command = split[0];
				
				switch (command) {
					case "noop":
						break;
					case "addx":
						next = Integer.valueOf(split[1]);
						skip = true;
						break;
				}
				
			}
			System.out.println(cycles);
			System.out.println(total);
		}
	}
	
	public static char[] merge(char[] c1, char[] c2, int length) {
		char[] c = new char[length];
		for (int i = 0; i < length; i++) {
			if (c1[i] != 0)
				c[i] = c1[i];
			if (c2[i] != 0)
				c[i] = c2[i];
		}
		return c;
	}
	
	public static class Sprite {
		int x;
		int width;
		
		public Sprite(int x, int width) {
			this.x = x;
			this.width = width;
		}
		
		public void move(int amount) {
			x += amount;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public int getX() {
			return x;
		}
		
		public char[] draw(int length) {
			char[] s = new char[length];
			for (int i = 0; i < length; i++) {
				if (i >= x-1 && i <= x+1)
					s[i] = '%';
			}
			return s;
		}
	}
	
	public static class Command<T> {
		int cycles;
		String name;
		
		public Command(String name, int cycles) {
			this.name = name;
			this.cycles = cycles;
		}
		
		public String getName() {
			return name;
		}
	}
	
}
