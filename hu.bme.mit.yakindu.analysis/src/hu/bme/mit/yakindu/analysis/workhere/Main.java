package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
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
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		List<String> trappedStateNames = new ArrayList<String>();
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
				if(state.getOutgoingTransitions().isEmpty())
					trappedStateNames.add(state.getName());
					
			}
			else if(content instanceof Transition)
			{
				Transition tr = (Transition) content;
				System.out.println(tr.getSource().getName() + " -> " + tr.getTarget().getName());
			}
		}
		
		System.out.print("Trapped state names: ");
		for(int i = 0; i < trappedStateNames.size(); i++)
			System.out.print(trappedStateNames.get(i) + " ");
		System.out.println();
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
