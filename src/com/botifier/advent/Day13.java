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

public class Day13 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day13_input");
		
		
		
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			ArrayList<NestedArrayObjectAndClone<Integer>> a = new ArrayList<NestedArrayObjectAndClone<Integer>>();
			while ((s = r.readLine()) != null) {
				if (s.isEmpty())
					continue;
				System.out.println("NEW");
				NestedArrayObjectAndClone<Integer> hold = seperateArrays(s, s, 0);
				a.add(hold);
				System.out.println("hold: "+hold);
			}

			int count = 0;
			int pos = 1;
			for (int i = 0; i < a.size()-1; i+=2) {
				System.out.println();
				NestedArrayObjectAndClone<Integer> left = a.get(i);
				NestedArrayObjectAndClone<Integer> right = a.get(i+1);
				System.out.println("left:"+left);
				System.out.println("right:"+right);
				System.out.println();
				//int compare = left.compare(right);
				if (compare(left,right,0)) {
					count+= pos;
					System.out.println(true);
				}
				pos++;
			}
			System.out.println(count);
		}
	}
	
	public static boolean compare(NestedArrayObjectAndClone<Integer> left, NestedArrayObjectAndClone<Integer> right, int it) {

		System.out.println(left);
		System.out.println(right);
		System.out.println("it:"+it);
		for (int e = 0; e < left.size(); e++) {
			System.out.println(left.getObject(e));
			for (int p = (it > 0 ? 0 : e); p < right.size(); p++) {
				if (e < p)
					continue;
				System.out.println("pos:"+p+", "+e);
				if (left.isPlain(e) && right.isPlain(p)) {
					System.out.println("both plain");
					System.out.println(left.getObject(e).intValue()+"<="+right.getObject(p).intValue()+"?");
					
					if (it > 0 && right.size() < left.size()) {
						System.out.println("o");
						for (int i = 0; i < left.size(); i++) {
							int op = right.getObject(p);
							
							if (left.isPlain(i)) {
								int lp = left.getObject(i);
								
								System.out.println(lp+"<"+op+"?");
								if (lp > op) {
									System.out.println("left bigger plain: "+lp+">"+op);
									return false;
								}
							} else if (left.isClone(i)) {
								int o = right.getObject(p).intValue();
								NestedArrayObjectAndClone<Integer> lI = left.getSimilar(i);
								NestedArrayObjectAndClone<Integer> rI = new NestedArrayObjectAndClone<Integer>().add(o);
								
								boolean recurse = compare(lI, rI, it+1);
								if (recurse == false)
									return false;
							}
							
						}
					} else {
						if (left.getObject(e).intValue() > right.getObject(p).intValue()) {
							System.out.println("left bigger plain: "+left.getObject(e).intValue()+">"+right.getObject(p).intValue());
							return false;
						}
					}
				}else if (left.isArray(e) && right.isArray(p)) {
					Integer[] i1 = left.getArray(e);
					Integer[] i2 = right.getArray(p);

					System.out.println("both array");
					if (i2.length < i1.length)
						return false;
					for (int i = 0; i < i1.length; i++) {
						int o = i1[i];
						int w = i2[i];

						System.out.println(o+"<="+w);
						if (o > w) {
							System.out.println(o +" > "+w);
							return false;
						}
					}
				} else if (left.isClone(e) && right.isClone(p)) {
					NestedArrayObjectAndClone<Integer> lI = left.getSimilar(e);
					NestedArrayObjectAndClone<Integer> rI = right.getSimilar(p);
					System.out.println(right);

					System.out.println("both nested");
					System.out.println("lI:"+lI);
					System.out.println("rI:"+rI);
					System.out.println("recurring");
					boolean recurse = compare(lI, rI, it +1);
					if (recurse == false)
						return false;
				}
				/*
				else if (left.isPlain(e) && right.isArray(e)) {
					System.out.println("left plain right array");
					int o = left.getObject(e).intValue();
					Integer[] i2 = right.getArray(e);
					for (int i = 0; i < i2.length; i++) {
						int w = i2[i];
						System.out.println(o+"<="+w);
						if (o > w) {
							//return false;
						}
					}
				} else if (right.isPlain(e) && left.isArray(e)) {
					int o = right.getObject(e).intValue();
					Integer[] i2 = left.getArray(e);
					for (int i = 0; i < i2.length; i++) {
						int w = i2[i];

						System.out.println(o+"<="+w);
						if (o > w) {
							//return false;
						}
					}
				} 
				*/
				else if (left.isPlain(e) && right.isClone(p)) {
					System.out.println("left plain right nested");
					int o = left.getObject(e).intValue();
					NestedArrayObjectAndClone<Integer> lI = new NestedArrayObjectAndClone<Integer>().add(o);
					NestedArrayObjectAndClone<Integer> rI = right.getSimilar(p);
					//NestedArrayObjectAndClone<Integer> w = rI.getSimilar(i);
					
					boolean recurse = compare(lI, rI, it+1);
					if (recurse == false)
						return false;
				} else if (left.isClone(e) && right.isPlain(p)) {
					System.out.println("left nested right plain");
					int o = right.getObject(p).intValue();
					NestedArrayObjectAndClone<Integer> lI = left.getSimilar(e);
					NestedArrayObjectAndClone<Integer> rI = new NestedArrayObjectAndClone<Integer>().add(o);
					//NestedArrayObjectAndClone<Integer> w = rI.getSimilar(i);
					
					System.out.println("lI:"+lI);
					System.out.println("rI:"+rI);
					System.out.println("o:"+o);
					
					if (lI.size() == 0) {
						continue;
					}
					
					boolean recurse = compare(lI, rI, it+1);
					if (recurse == false)
						return false;
					else e++;
					/*int o = right.getObject(e).intValue();
					NestedArrayObjectAndClone<Integer> lI = left.getSimilar(e);
					for (int i = 0; i < lI.size(); i++) {
						if (lI.isPlain(i)) {
							int w = lI.getObject(i);
							System.out.println(lI);
							System.out.println(o);
							System.out.println(w+"<="+o+"??");
							if (w < o) {
								System.out.println(i+":"+w+">"+o);
								System.out.println(lI.getObject(i));
								return true;
							}
						} else if (lI.isArray(i)) {

							int w = lI.getObject(i);
							
							if (w > o) {
								System.out.println(o+">"+w);
								return false;
							}
						} else if (lI.isClone(i)) {
							NestedArrayObjectAndClone<Integer> w = lI.getSimilar(i);
							int compare = w.compareInt(o);
							System.out.println("compare: "+compare);
							if (compare < 0)
								return false;
						}
					} */
				}
			}
			
		}
		if (left != null && right != null && left.size() > right.size() && (right.size() == 0 || it == 0)) {
			System.out.println("right too small");
			return false;
		}
		System.out.println(left);
		System.out.println(right);
		System.out.println("gone through full, valid");
		return true;
	}
	
	public static NestedArrayObjectAndClone<Integer> seperateArrays(String s, String original, int ite) {
		 NestedArrayObjectAndClone<Integer> hold = new  NestedArrayObjectAndClone<Integer>();
		 //int test = 0;
		//System.out.println(s);
		int completedFirst = 0;

		//System.out.println(it);
		//System.out.println("String: "+s);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int start = 0;
			int end = s.length()-1;
			NestedArrayObjectAndClone<Integer> n = null;

			//System.out.println(i);
			//System.out.println(c);

			completedFirst++;
			if (c == '[') {
				if (s.length() == 1)
					return hold;
				int beginCount = 1;
				int counter = 0;
				start = i;
				int e = i + 1;
				while (e < s.length() && (counter < beginCount)) {
					
					char ch = s.charAt(e);
					e++;
					if (ch == '[') {
						start = e-1;

						int beginCount2 = 1;
						//System.out.println("start:"+start);
						//System.out.println("s:"+s);
						while (e < s.length()) {
							ch = s.charAt(e++);
							if (ch == '[') {
								beginCount2++;
							}
							if (ch == ']') {
								counter++;
								//System.out.println(counter);
							}
							if (counter >= beginCount2) {
								//System.out.println("break: "+e+", "+ch);
								end = e;
								break;
							}
						}
						//System.out.println("ns:"+s);
						//System.out.println("end:"+end);
						String nested = s.substring(start, end);
						//System.out.println("n:"+nested);
						n = seperateArrays(nested, original, ite+1);

						//System.out.println("added: "+n);
						hold.add(n);
						String h = s.substring(0, 1);
						
						if (end < s.length()-1)
							h += s.substring(end+1, s.length());
						if (!h.endsWith("]"))
							h += ']';
						//System.out.println(ite+" s1:"+h);
						s = h;
						i = -1;
						break;
					}else if (isInteger(ch + "")) {
						int st = e-1;
						int en = e+1;
						String temp = ch +"";
						int e2 = i;
						//System.out.println(ch);
						while (e2 < s.length()) {
							char ch2 = s.charAt(e++);
							if (isInteger(ch2+"")) {
								temp += ch2;
								 en = e2+1;
							}
							else
								break;
						}
						int val = Integer.valueOf(temp);
						hold.add(val);
					}
				}
			}
			if (n != null && ite == 0 && !s.isEmpty()) {
				/*
				System.out.println("N:"+n);
				System.out.println("rs");
				System.out.println("o:"+completedFirst);
				System.out.println("hold: "+hold);
				System.out.println("current:"+s);
				System.out.println("original:"+original);

				System.out.println("start:"+start);
				*/
				//if (s.length() <= 1)
					//return hold;
				String sub = "";
			
				if (completedFirst < 1) {
					completedFirst = 1;
					//System.out.println("replace: "+hold.toString());
					String toReplace = hold.toString().substring(1, hold.toString().length()-1);
					toReplace.replace("[", "\\[");
					toReplace.replace("]", "]");
					//System.out.println(toReplace);
					s = "["+original.replace(toReplace, "").substring(2);
					//System.out.println("replace: "+s);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				continue;
				/*sub = original.substring(completedFirst-1,start);
				completedFirst = -1;
				
				String sub2 = "";
				if (end != original.length())
					sub2 = original.substring(end+1, original.length());

				s = sub + sub2;
				System.out.println("ns:"+s);
				if (s.isEmpty())
					return hold;

				if (!s.startsWith("["))
					s = "["+s;
				i = -1;
				//test = 1;
				continue;*/
			}
		}
		return hold;
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static class NestedArrayObjectAndClone<T extends Comparable<T>>{
		
		HashMap<Integer, T> hold = new HashMap<Integer, T>();
		HashMap<Integer, T[]> hold2 = new HashMap<Integer, T[]>();
		HashMap<Integer, NestedArrayObjectAndClone<T>> hold3 = new HashMap<Integer, NestedArrayObjectAndClone<T>>();
		
		private int next = 0;
		
		public NestedArrayObjectAndClone<T> add(T h) {
			hold.put(next++, h);
			return this;
		}
		
		public NestedArrayObjectAndClone<T> add(T[] h) {
			hold2.put(next++, h);
			return this;
		}
		
		public NestedArrayObjectAndClone<T> add(NestedArrayObjectAndClone<T> h) {
			hold3.put(next++, h);
			return this;
		}
		
		public boolean isPlain(int pos) {
			return hold.containsKey(pos);
		}
		
		public boolean isArray(int pos) {
			return hold2.containsKey(pos);
		}
		
		public boolean isClone(int pos) {
			return hold3.containsKey(pos);
		}
		
		public T getObject(int pos) {
			return hold.get(pos);
		}
		
		public T[] getArray(int pos) {
			return hold2.get(pos);
		}
		
		public NestedArrayObjectAndClone<T> getSimilar(int pos) {
			return hold3.get(pos);
		}
		
		public int size() {
			return next;
		}
		
		public Stream<NestedArrayObjectAndClone<T>> toStream() {
			Builder<NestedArrayObjectAndClone<T>> builder = Stream.<NestedArrayObjectAndClone<T>>builder();
			
			for (int i = 0; i < next; i++) {
				if (isClone(i)) {
					builder.add(new NestedArrayObjectAndClone<T>().add(getSimilar(i)));
				} else if (isArray(i)) {
					builder.add(new NestedArrayObjectAndClone<T>().add(getArray(i)));
				} else
					builder.add(new NestedArrayObjectAndClone<T>().add(getObject(i)));
			}
			return builder.build();
		}
		
		public void forEach(Consumer<? super T> action) {
			for (int i = 0; i < size(); i++) {
				if (isClone(i)) {
					getSimilar(i).forEach(action);
				}
				else if (isArray(i)) {
					for (T t : getArray(i))
						action.accept(t);
				} else
					action.accept(getObject(i));
			}
		}
		
		@Override
		public String toString() {
			String s = "[";
			for (int i = 0; i < next; i++) {
				if (isClone(i)) {
					s += getSimilar(i);
				}
				else if (isArray(i)) {
					s += "[";
					for (T t : getArray(i)) {
						s += t;
					}
					s += "]";
				} else
					s += getObject(i);
				if (i != next-1)
					s += ",";
			}
			s += "]";
			return s;
		}
		
		public boolean isBigger(NestedArrayObjectAndClone<Integer> o) {
			for (int i = 0; i < size(); i++) {
				
			}
			return false;
		}
		
		public int sum() {
			int sum = 0;
			for (int i = 0; i < size(); i++) {
				if (isClone(i)) {
					sum += getSimilar(i).sum();
				} else if (isArray(i)) {
					for (int e = 0; e < getArray(i).length; i++) {
						sum += (Integer)getArray(i)[e];
					}
				} else if (isPlain(i)) {
					sum += (Integer)getObject(i);
				}
			}
			return sum;
		}
		
		public int compareInt(int c) {
			int comp = 0;
			for (int i = 0; i < size(); i++) {
				if (isClone(i)) {
					System.out.println("nested recurring");
					NestedArrayObjectAndClone<Integer> op = (NestedArrayObjectAndClone<Integer>) getSimilar(i);
					if (op.compareInt(c) > 0) {
						return -1;
					}
				}
				else if (isArray(i)) {
					for (int o = 0; o < getArray(i).length; o++) {
						System.out.println(c+","+((Integer[])getArray(i))[o]);
						comp += c - ((Integer[])getArray(i))[o];
					}
				} else {
					System.out.println(c+"-"+((Integer)getObject(i)));
					if (c - ((Integer)getObject(i)) > 0) {
						return -1;
					}
					comp += c - ((Integer)getObject(i));
				}
			}
			System.out.println(c+"," + comp);
			return comp;
		}
		
		public int compareIntArr(NestedArrayObjectAndClone<Integer> o, int pos, int inner) {
			if (isArray(pos)) {
				if (o.isArray(pos)) {
					Integer[] in = ((Integer[])getArray(pos));
					Integer[] in2 = o.getArray(pos);
					return  in[inner].intValue() - in2[inner].intValue();
				}
				if (o.isPlain(pos)) {
					Integer[] in = ((Integer[])getArray(pos));
					int in2 = o.getObject(pos).intValue();
					return in[inner].intValue() - in2;
				}
			}
			return 0;
		}
		
	}
	
}
