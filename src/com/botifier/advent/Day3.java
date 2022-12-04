package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day3_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			int psum = 0;
			String[][] buffer = new String[3][2];
			int bufferPos = 0;
			while ((s = r.readLine()) != null) {
				buffer[bufferPos][0] = s.substring(0, (s.length()/2));
				buffer[bufferPos][1] = s.substring((s.length()/2), s.length());
				bufferPos++;
				if (bufferPos == 3) {
					bufferPos = 0;
					char last = 0;
					String[] o = new String[buffer.length];
					int oit = 0;
					for (int i = 0; i < buffer.length; i++) {
						String p1 = buffer[i][0];
						String p2 = buffer[i][1];
						
						o[oit++] = p1+p2;
					}
					last = findMatch(o);
					System.out.println(last);
					psum += calcPriority(last);
				}
			}
			System.out.println(psum);
		}
	}

	public static char findMatch(String... s) {
		ArrayList<Character> chars = new ArrayList<Character>(); 
		for (int i = 0; i < s.length; i++) {
			String s1 = s[i];
			Collection<Character> cc = new HashSet<Character>();
			for (int c : s1.chars().toArray()) {
				cc.add((char)c);
			}
			chars.addAll(cc);
		}
		Optional<Character> ch = chars.stream().filter(c -> Collections.frequency(chars, c) >= s.length).findFirst();
		if (ch.isPresent())
			return ch.get();
		return 0;
	}
	
	public static byte calcPriority(char c) {
		if (!Character.isAlphabetic(c))
			return 0;
		if (Character.isUpperCase(c))
			return (byte) ((c-64) + 26);
		return (byte)(c - 96);
	}
}
