package com.botifier.advent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class Day16 {


	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("day16_input");
		
		
		LinkedList<Valve> valves = new LinkedList<Valve>();
		ArrayList<Valve> open = new ArrayList<Valve>();
		ArrayList<Valve> visited = new ArrayList<Valve>();
		
		
		
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			String s = null;
			
			while ((s = r.readLine()) != null) {
				//Makes it easy to read input
				//Not very efficient
				s = s.replace("Valve", "").replace("has", "").replace("flow", "").replace("rate", "")
					 .replace("tunnels", "").replace("tunnel", "").replace("leads", "").replace("lead", "")
					 .replace("to", "").replace("valves", "").replace("valve", "").replace(" ", "");
				String[] arg = s.split(";");
				
				String[] val = arg[0].split("=");
				String valve = val[0];
				int flow = Integer.valueOf(val[1]);
				
				String[] flowTo = arg[1].split(",");
				
				valves.add(new Valve(valve, flow, flowTo));
			}
			
			for (Valve v : valves) {
				v.updateValves(valves);
			}
			
			int pos = 0;

			int pressure = 0;
			//This is neat
			Valve last = null;
			Valve current = valves.get(0);
			boolean moved = false;
			for (int i = 0; i < 30; i++) {
				System.out.println("-=Minute "+(i+1)+"=-");
				System.out.println("Current location: "+current.getName());
				
				if (open.size() > 0) {
					String out = "Valve";
					//Less comparisons
					boolean doit = open.size() > 1;
					if (doit)
						out += "s ";
					else
						out += " ";
					int it = 0;
					int add = 0;
					for (Valve va : open) {
						it++;
						add += va.getFlow();
						out += va.getName();
						if (doit) {
							if (it < open.size()-1) {
								out += ",";
							} else if (it == open.size() -1) {
								out += " and";
							}
						}
						out += " ";
					}
					if (doit) 
						out += "are open, ";
					else
						out += "is open, ";
					out += "Releasing "+add+" pressure.";
					
					System.out.println(out);
					pressure += add;
				} else {
					System.out.println("No valves are open.");
				}
				

				if (current.getFlow() > 0 && !open.contains(current) && current.valvesTo.get(0).flowRate < current.getFlow()) {
					open.add(current);
					System.out.println("You open valve "+current.getName()+".");
					moved = false;
				}else {
					Valve hold = last;
					last = current;
					if (current.valvesTo.isEmpty())
						continue;
					current = current.valvesTo.get(0);
					int max = current.flowRate;
					if ((last != null 
						&& current.flowRate <= last.flowRate
						&& visited.contains(current))
						|| open.contains(current)) {
						
						Valve hol = current;

						max = Integer.MIN_VALUE;
						for (Valve v : valves) {
							if (v.equals(last))
								continue;
							if (!last.valvesTo.contains(v))
								continue;
							if (visited.contains(v))
								continue;
							
							if (v.getFlow() > max) {
								System.out.println(current.name+" !: "+v.name);
								hol = v;
								max = v.getFlow();
							}
						}
						current = hol;
					}
					//last.valvesTo.remove(current);
					System.out.println("You move to valve "+current.getName()+".");
					moved = true;
					visited.add(current);
					continue;
				}
			}
			System.out.println(pressure);
		}
	}
	
	public static class Valve {
		String name = "";
		int flowRate = 0;
		int state = 0;
		
		LinkedList<String> flowsTo = new LinkedList<String>();
		LinkedList<Valve> valvesTo = new LinkedList<Valve>();
		
		public Valve(String name, int flowRate, String[] flowsTo) {
			this.name = name;
			this.flowRate = flowRate;
			for (String s : flowsTo) {
				this.flowsTo.add(s);
			}
		}
		
		//Not a good idea
		public void updateValves(LinkedList<Valve> toUse) {
			for (String s : flowsTo) {
				for (Valve v : toUse) {

					if (v.name.equals(s)) {
						valvesTo.add(v);
					}
					
				}
			}
			
		}
		
		public void setState(int state) {
			this.state = state;
		}
		
		public void addFlow(int flow) {
			flowRate += flow;
		}
		
		public LinkedList<String> getTo() {
			return flowsTo;
		}
		
		public int getFlow() {
			return flowRate;
		}
		
		public int getState() {
			return state;
		}
		
		public String getName() {
			return name;
		}
	}
}
