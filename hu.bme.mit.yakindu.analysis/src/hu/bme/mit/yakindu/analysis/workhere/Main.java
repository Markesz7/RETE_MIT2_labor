package hu.bme.mit.yakindu.analysis.workhere;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() throws IOException {
		main(new String[0]);
	}
	
	static Random random = new Random();
	
	public static String randomNameGenerator(Statechart s)
	{
		while(true)
		{
			String name = "NoNameState" + random.nextInt(Integer.MAX_VALUE);
			TreeIterator<EObject> iterator = s.eAllContents();
			while (iterator.hasNext()) {
				EObject content = iterator.next();
				if(content instanceof State) {
					State state = (State) content;
					if(state.getName() == name)
						break;
					
				}
				return name;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Task 2.3:\n");
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		List<State> trappedStates = new ArrayList<State>();
		List<State> noNameStates = new ArrayList<State>();
		List<VariableDefinition> variables = new ArrayList<VariableDefinition>();
		List<EventDefinition> events = new ArrayList<EventDefinition>();
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition)
			{
				VariableDefinition var = (VariableDefinition) content;
				variables.add(var);
			}
			else if(content instanceof EventDefinition)
			{
				EventDefinition event = (EventDefinition) content;
				events.add(event);
			}
			else if(content instanceof State) {
				State state = (State) content;
				if(state.getName() == null)
				{				
					state.setName(randomNameGenerator(s));
					noNameStates.add(state);
				}
				System.out.println(state.getName());
				if(state.getOutgoingTransitions().isEmpty())
					trappedStates.add(state);					
			}
			else if(content instanceof Transition)
			{
				Transition tr = (Transition) content;
				if(tr.getTarget().getName() == null)
				{				
					tr.getTarget().setName(randomNameGenerator(s));
					noNameStates.add((State) tr.getTarget());
				}
				System.out.println(tr.getSource().getName() + " -> " + tr.getTarget().getName());
			}
		}
		
		System.out.println("\nTask 2.5:\n");
		System.out.print("Suggested names: ");
		for(int i = 0; i < noNameStates.size(); i++)
			System.out.print(noNameStates.get(i).getName() + " ");
		System.out.println();
		
		System.out.println("\nTask 2.4:\n");
		System.out.print("Trapped state names: ");
		for(int i = 0; i < trappedStates.size(); i++)
			System.out.print(trappedStates.get(i).getName() + " ");
		System.out.println();
		
		System.out.println("\nTask 4.3:\n");
		System.out.print("Variable names: ");
		for(int i = 0; i < variables.size(); i++)
			System.out.print(variables.get(i).getName() + " ");
		System.out.println();
		
		System.out.print("Event names: ");
		for(int i = 0; i < events.size(); i++)
			System.out.print(events.get(i).getName() + " ");
		System.out.println();		
		System.out.println("\nTask 4.4 and 4.5:\n");
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.BufferedReader;\r\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		boolean exit = false;\r\n" + 
				"		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"		while(!exit)\r\n" + 
				"		{\r\n" + 
				"			switch(br.readLine())\r\n" + 
				"			{");
		
		for(int i = 0; i < events.size(); i++)
		{
			System.out.println("			case \"" + events.get(i).getName() + "\":\r\n" + 
							   "				s.raise" + (events.get(i).getName().substring(0, 1).toUpperCase() + events.get(i).getName().substring(1)) +"();\r\n" + 
							   "				break;\r\n");
		}

		System.out.println("			case \"exit\":\r\n" + 
						   "				exit = true;\r\n" + 
						   "				break;\r\n" + 
						   "				\r\n" + 
						   "			default:\r\n" + 
						   "				System.out.println(\"Unknown command!\");\r\n" + 
						   "				break;\r\n" + 
						   "			}\r\n" + 
						   "			if(!exit)\r\n" + 
						   "			{\r\n" + 
						   "				s.runCycle();\r\n" + 
						   "				print(s);	\r\n" + 
						   "			}\r\n" + 
						   "		}\r\n" + 
						   "		br.close();\r\n" + 						   
						   "		System.exit(0);\r\n" + 
						   "	}\r\n");		
		
		System.out.println("\tpublic static void print(IExampleStatemachine s) {");
		for(int i = 0; i < variables.size(); i++)
		{
			System.out.println("\t\tSystem.out.println(\"" 
							  + variables.get(i).getName().substring(0, 1).toUpperCase() 
							  + " = \" + s.getSCInterface().get"
							  + (variables.get(i).getName().substring(0, 1).toUpperCase() + variables.get(i).getName().substring(1))
							  +"());");
		}
		System.out.println("\t}\r\n}");
		
		System.out.println("\nTask 4.8:\n");
		System.out.println("Generating code...");

		FileWriter myWriter = new FileWriter("NewGeneratedCode.java");
		myWriter.write("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.BufferedReader;\r\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class NewGeneratedCode {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		boolean exit = false;\r\n" + 
				"		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"		while(!exit)\r\n" + 
				"		{\r\n" + 
				"			switch(br.readLine())\r\n" + 
				"			{\r\n");
		
		for(int i = 0; i < events.size(); i++)
		{
			myWriter.write("			case \"" + events.get(i).getName() + "\":\r\n" + 
						   "				s.raise" + (events.get(i).getName().substring(0, 1).toUpperCase() + events.get(i).getName().substring(1)) +"();\r\n" + 
						   "				break;\r\n\n");
		}

		myWriter.write    ("			case \"exit\":\r\n" + 
						   "				exit = true;\r\n" + 
						   "				break;\r\n" + 
						   "				\r\n" + 
						   "			default:\r\n" + 
						   "				System.out.println(\"Unknown command!\");\r\n" + 
						   "				break;\r\n" + 
						   "			}\r\n" + 
						   "			if(!exit)\r\n" + 
						   "			{\r\n" + 
						   "				s.runCycle();\r\n" + 
						   "				print(s);	\r\n" + 
						   "			}\r\n" + 
						   "		}\r\n" + 
						   "		br.close();\r\n" + 								   
						   "		System.exit(0);\r\n" + 
						   "	}\r\n");		
		
		myWriter.write("\tpublic static void print(IExampleStatemachine s) {\r\n");
		for(int i = 0; i < variables.size(); i++)
		{
			myWriter.write   ("\t\tSystem.out.println(\"" 
							  + variables.get(i).getName().substring(0, 1).toUpperCase() 
							  + " = \" + s.getSCInterface().get"
							  + (variables.get(i).getName().substring(0, 1).toUpperCase() + variables.get(i).getName().substring(1))
							  +"());\r\n");
		}
		myWriter.write("\t}\r\n}");
		System.out.println("Code successfully generated!");
		myWriter.close();
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
}
