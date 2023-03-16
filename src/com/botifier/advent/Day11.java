package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Day11 {
	public static ArrayList<Monkey> monkies = new ArrayList<Monkey>();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day11_input");
		
		
		
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			String hold = "";
			while ((s = r.readLine()) != null) {
				if (s.isEmpty()) {
					Monkey m = Monkey.build(hold);
					monkies.add(m);
					hold = "";
					continue;
				}
				hold += s+"\n";
			}
			Monkey mo = Monkey.build(hold);
			monkies.add(mo);
			
			System.out.println("running...");
			for (int ro = 0; ro < 20; ro++) {
				System.out.println("round: "+(ro+1));
				
				for (Monkey m : monkies) {
					for (int i = m.items.size()-1  ; i >= 0; i--) {
						long e = m.items.get(i);

						m.inspect(e, ro + 1);
					}
					System.out.println(m.name+" "+m.inspections);
				}

			}

			//long last = System.currentTimeMillis();
			//System.out.println(System.currentTimeMillis()-last);
			
			for (Monkey m : monkies) {
				
				System.out.println(m.name+" "+m.inspections);
			}
			
			
			long is = monkies.stream().map(m -> m.inspections).sorted(Collections.reverseOrder()).limit(2).mapToLong(i -> i).reduce(1, Math::multiplyExact);
			System.out.println(is);
			System.out.println(5204 * 5192);
		}
	}
	
	public static boolean isLong(String s) {
		try {
			new BigInteger(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static char stringToOperation(String s) {
		String e = s.toLowerCase();
		/*switch (e) {
			case "divisible":
				return '%';
			case "multiple":
				return '/';
		}*/
		
		char c = e.charAt(0);
		return c;
	}
	
	public static long mathByChar(char c, long a, long b) {
			return c == '*' ? a * b : (c == '/') ? a / b :
				   c == '+' ? a + b : c == '-' ? a - b    :
				   c == '%' || c == 'd' ? a % b : 0;
	}
	public static long mathByChar(char ch, long a, long b, long c) {
		return ch == '*' ? (a == b ? (a << 1) % c : ((a % c) * (b % c)) / c) : ch == '/' ? (a / b) % c :
			   ch == '+' ? (a+b) % c : ch == '-' ? (a-b) % c    :
			   ch == '%' || ch == 'd' ? ((a%c) * (b%c))/c : 0;
	}
	
	public static long bigPowMod(long a, long b, long c) {
		String bin = Long.toBinaryString(b);
		
		long product = 0;
		long hold = 0;
		
		for (int i = bin.length()-1; i >= 0; i--) {
			if (i == 0)
				hold = a % c;
			else
				hold = (hold * hold) % c;
			
			char ch = bin.charAt(i);
			if (ch == '1') {
				if (product == 0)
					product = hold;
				else
					product *= hold;
				product %= c;
			}
		}
		return product;
	}
	
	public static long bigMod(char ch, long a, long b, long c, int round) {
		System.out.println(a % c);
		//System.out.println(Math.pow((b % c), round) % c);
		System.out.println(bigPowMod(b, round, c));
		//System.out.println(((a % c) + bigPowMod(b, round, c)) % c);
		return ch == '*' ? ((a % c) * (bigPowMod(b, round, c))) % c : 
			   ch == '/' ? (a / b) % c :
			   ch == '+' ? ((a % c) + bigPowMod(b, round, c)) % c : 
			   ch == '-' ? ((a % c) - bigPowMod(b, round, c)) % c    :
			   ch == '%' || 
			   ch == 'd' ? ((a%c) * (b%c)) / c : 0;
	}
	
	public static long bigModAdd(long a, long b, long c, int round) {
		return (a % c) + ((long)Math.pow(b, round) % c) / c;
	}
	
	public static long stringToMath(String s, long i1, long i2) {
		return mathByChar(s.charAt(0), i1, i2);
	}
	public static long stringToMath(String s, long i1, long i2, long i3) {
		return mathByChar(s.charAt(0), i1, i2, i3);
	}
	
	
	public static class Monkey {
		ArrayList<Long> items = new ArrayList<Long>();
		String name;
		String operation;
		long opMod;
		String test;
		long testMod;
		String targetTrue;
		String targetFalse;
		
		Monkey targetTrueM;
		Monkey targetFalseM;
		
		int inspections = 0;
		
		private Monkey(String name, String operation, long opMod, String test, long testMod, String targetTrue, String targetFalse, long... i) {
			for (int e = 0; e < i.length; e++) {
				items.add(i[e]);
			}
			this.name = name;
			this.targetFalse = targetFalse;
			this.targetTrue = targetTrue;
			this.operation = operation;
			this.test = test;
			this.opMod = opMod;
			this.testMod = testMod;
		}
		
		public void inspect(long item, int round) {
			if (!items.contains(item))
				return;
			inspections++;
			
			int index = items.indexOf(item);
			
			long test = 0;
			long opMod = this.opMod;
			
			if (opMod == 0) {
				opMod = item;
			}
			

			test = bigMod(operation.charAt(0), item, opMod, testMod, round);
			
			//items.add(index, value);
			//items.remove(index+1);
			System.out.println("("+item+" "+operation+" "+opMod+"^"+round+") % "+testMod+"="+test);
			
			boolean t = (test == 0); 
			//System.out.println(t);

			if (t == true) {
				if (targetTrueM == null)
					targetTrueM = monkies.stream().filter((mo) -> mo.name.equalsIgnoreCase(targetTrue)).findFirst().get();
				throwTo(targetTrueM, item);
			} else {
				if (targetFalseM == null)
					targetFalseM = monkies.stream().filter((mo) -> mo.name.equalsIgnoreCase(targetFalse)).findFirst().get();
				throwTo(targetFalseM, item);
			}

		}
		
		public void throwTo(Monkey m, Long item) {
			items.remove(item);
			m.items.add(item);
		}
		
		public ArrayList<Long> getItems() {
			return items;
		}
		
		public static Monkey build(String toUse) {
			String name = ""; 
			String operation = "";
			String test = "";
			String targetTrue = "";
			String targetFalse = "";
			
			long testMod = 0;
			long opMod = 0;
			
			ArrayList<Long> e = new ArrayList<Long>();
			ArrayList<String> lines = new ArrayList<String>();
			String line = "";
			for (int i = 0; i < toUse.length(); i++) {
				char c = toUse.charAt(i);
				if (c == '\n') {
					lines.add(line);
					line = "";
					continue;
				}
				line += c;
			}
			for (String s : lines) {
				String temp = s.replaceAll(":", "");
				temp = temp.replaceAll("\t", "");
				String[] split = temp.split(" ");
				
				int op = 0;
				for (int i = 0; i < split.length; i++) {
					String current = split[i];
					current = current.toLowerCase();
					boolean end = false;
					switch (op) {
						case 0:
							switch (current) {
								case "monkey":
									op = 1;
									break;
								case "starting":
									op = 2;
									break;
								case "operation":
									op = 3;
									break;
								case "test":
									op = 4;
									break;
								case "if":
									op = 5;
									break;
							}
							break;
						case 1:
							name = "Monkey "+current;
							end = true;
							break;
						case 2:
							if (current.equalsIgnoreCase("items"))
								continue;
							String[] ints = current.split(",");
							for (String st : ints) {
								Long h = Long.valueOf(st);
								e.add(h);
							}
							continue;
						case 3:
							if (current.equals("new"))
								continue;
							if (current.equals("="))
								continue;
							if (current.equals("old"))
								continue;
							if (isLong(current)) {
								opMod = Long.valueOf(current);
								continue;
							}
							operation += current;
							break;
						case 4:
							if (isLong(current)) {
								testMod = Long.valueOf(current);
								break;
							}
							if (current.equalsIgnoreCase("by"))
								break;
							test += current;
							break;
						case 5:
							if (current.equalsIgnoreCase("true"))
								op = 6;
							if (current.equalsIgnoreCase("false"))
								op = 7;
							break;
						case 6:
							if (!isLong(current))
								continue;
							targetTrue= "Monkey "+current;
							end = true;
							break;
						case 7:
							if (!isLong(current))
								continue;
							targetFalse = "Monkey "+current;
							end = true;
							break;
					}
					if (end)
						break;
				}
			}
			
			return new Monkey(name, operation, opMod, test, testMod, targetTrue, targetFalse, e.stream().mapToLong(Long::valueOf).toArray());
		}
	}
}
