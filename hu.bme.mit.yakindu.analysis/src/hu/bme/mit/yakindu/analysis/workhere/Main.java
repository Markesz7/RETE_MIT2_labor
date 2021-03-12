package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
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
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		List<State> trappedStates = new ArrayList<State>();
		List<State> noNameStates = new ArrayList<State>();
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
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
		
		System.out.print("Suggested names: ");
		for(int i = 0; i < noNameStates.size(); i++)
			System.out.print(noNameStates.get(i).getName() + " ");
		System.out.println();
		
		System.out.print("Trapped state names: ");
		for(int i = 0; i < trappedStates.size(); i++)
			System.out.print(trappedStates.get(i).getName() + " ");
		System.out.println();
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
