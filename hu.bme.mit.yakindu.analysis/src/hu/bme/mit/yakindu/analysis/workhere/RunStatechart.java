package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		boolean exit = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(!exit)
		{
			switch(br.readLine())
			{
			case "start":
				s.raiseStart();
				break;
				
			case "white":
				s.raiseWhite();
				break;
				
			case "black":
				s.raiseBlack();
				break;
				
			case "exit":
				exit = true;
				break;
				
			default:
				System.out.println("Unknown command!");
				break;
			}
			if(!exit)
			{
				s.runCycle();
				print(s);	
			}
		}
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
