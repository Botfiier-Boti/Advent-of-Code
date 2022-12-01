package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
	
	public static void main(String[] args) throws IOException {
		File f = new File("day1_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			HashMap<Long, Long> elves = new HashMap<Long, Long>();
			
			long largestCal = 0;
			long largestElf = 0;
			
			long cal = 0;
			long elf = 0;
			while ((s = r.readLine()) != null) {
				if (s.isEmpty()) {
					if (cal >= largestCal) {
						largestCal = cal;
						largestElf = elf;
					}
					elves.put(elf, cal);
					cal = 0;
					elf++;
					continue;
				}
				cal += Long.valueOf(s);
			}
			if (cal >= largestCal) {
				largestCal = cal;
				largestElf = elf;
			}
			cal = 0;
			
			List<Long> largestElves = elves.values().stream().sorted(Collections.reverseOrder()).limit(3).collect(Collectors.toList());
			long calBig = largestElves.stream().mapToLong(Long::longValue).sum();
			System.out.println(calBig);
		}
	}
}
