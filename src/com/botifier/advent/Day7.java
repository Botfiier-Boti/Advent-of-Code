package com.botifier.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class Day7 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		java.io.File f = new java.io.File("day7_input");
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			
			String s = "";
			
			Folder cDir = new Folder(null, "/");
			Folder bDir = cDir;
			while ((s = r.readLine()) != null) {
				if (!s.startsWith("$"))
					continue;

				System.out.println(s);
				s = s.substring(2);
				if (s.startsWith("ls")) {
					//System.out.println(s);
					while (((s = r.readLine()) != null)) {
						if (s.startsWith("$"))
							break;
						String[] split = s.split(" ");
						if (split[0].equals("dir")) {
							cDir.getFolders().put(split[1], new Folder(cDir, split[1]));
						} else {
							cDir.getFiles().put(split[1], new File(cDir, split[1], Long.valueOf(split[0])));
						}
					}
					if (s != null)
						s = s.substring(2);
					System.out.println("went through ls");
				}
				if (s != null && s.startsWith("cd")) {
					System.out.println(s);
					s = s.substring(3);
					switch (s) {
						case "..":
							System.out.println("..");
							if (cDir.getOwner() != null) {
								cDir = cDir.getOwner();
							} else
								System.out.println("Failed to go back");
							break;
						case "/":
							cDir = bDir;
							break;
						default:
							System.out.println(s);
							File temp = cDir.findFolder(s);
							if (temp != null && temp instanceof Folder)
								cDir = (Folder)temp;
							else 
								System.out.println("Failed to find folder");
							break;
					}
					System.out.println("Current Directory: "+cDir.getFullName());
				}
				
			}
			System.out.println("end");

			System.out.println(bDir);
			System.out.println(bDir.sumLessThan(100000));
			long t = 30000000-(70000000 - bDir.getSize());
			HashMap<String, Long> oo = bDir.allMoreThan(t, new HashMap<String, Long>());
			
			System.out.println(oo.values().stream().min(Long::compare).get());
			
			
		}
	}
	
	
	public static class File {
		Folder owner;
		String name; 
		long size;
		
		public File(Folder owner, String name, long size) {
			this.owner = owner;
			this.name = name;
			this.size = size;
		}
		
		public Folder getOwner() {
			return owner;
		}
		
		public String getName() {
			return name;
		}
		
		public long getSize() {
			return size;
		}
	}
	
	public static class Folder extends File{
		
		HashMap<String, File> files;
		HashMap<String, Folder> folders;
		
		public Folder(Folder owner, String name, File... files) {
			super(owner, name, 0);
			this.files = new HashMap<String, File>();
			this.folders = new HashMap<String, Folder>();
			for (File f : files) {
				this.files.put(f.name, f);
			}
		}
		
		public File find(String name) {
			return files.get(name);
		}
		
		public File findFolder(String name) {
			return folders.get(name);
		}

		
		public HashMap<String, Folder> getFolders() {
			return folders;
		}
		
		public HashMap<String, File> getFiles() {
			return files;
		}
		
		public String getFullName() {
			String n = name;
			Folder o = this;
			while ((o = o.getOwner()) != null) {
				n = o.getName() + "/"+n;
			}
			return n;
		}
		
		
		public HashMap<String, Long> allLessThan(long size, HashMap<String, Long> l) {
			for (Folder f : folders.values()) {
				long inS = f.getSize();
				if (inS <= size) {
					l.put(f.getFullName(), inS);
				}
				if (!f.folders.isEmpty()) {
					f.allLessThan(size, l);
				}
			}
			return l;
		}
		
		public HashMap<String, Long> allMoreThan(long size, HashMap<String, Long> l) {
			for (Folder f : folders.values()) {
				long inS = f.getSize();
				if (inS >= size) {
					l.put(f.getFullName(), inS);
				}
				if (!f.folders.isEmpty()) {
					f.allMoreThan(size, l);
				}
			}
			return l;
		}
		
		public long sumLessThan(long size) {
			HashMap<String, Long> l = allLessThan(size, new HashMap<String, Long>());
			long sum = 0;
			for (Long ll : l.values()) {
				sum += ll;
			}
			return sum;
		}
		
		public String allToString(int depth) {
			System.out.println(depth);
			String s = name+",(Owner="+(getOwner() != null ? getOwner().name : "null")+",Size:"+getSize()+"):\n";
			for (Folder f : folders.values()) {
				for (int i = 0; i < depth+1; i++)
					s += "\t";
				s += f.allToString(depth+ 1)+"\n";
			}
			for (File f : files.values()) {
				for (int i = 0; i < depth+1; i++)
					s += "\t";
				s += f.getOwner().getName()+"/"+f.getName() +", size: "+f.getSize()+"\n";
			}
			return s;
		}
		
		@Override
		public String toString() {
			return allToString(1);
		}
		
		@Override
		public long getSize() {
			size = 0;
			for (File f : files.values()) {
				size += f.getSize();
			}
			for (Folder f : folders.values()) {
				size += f.getSize();
			}
			return size;
		}
	}
}
