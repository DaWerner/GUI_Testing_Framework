package Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;


public class DOMCompare{
	private Document ShouldBe;
	private List<Integer> Matches;
	private List<Integer> Diffs;
	
	public DOMCompare(Document Stored) {
		this.ShouldBe = Stored;
		this.Matches = new ArrayList<>();
		this.Diffs = new ArrayList<>();
	}
	
	public int calcSimilarity(Document ToTest) {
		if(ToTest.equals(ShouldBe)) return 100000;
		double Match = 0;
		double Diff = 0;
		LinkedList<Element> ElsShould = unnest(ShouldBe.getAllElements());
		LinkedList<Element> ElsIs = unnest(ToTest.getAllElements());

		
		for(Element El : ElsIs) {
			if(ElsShould.contains(El)) Match ++;
			else Diff ++;
		}
		System.out.println("Matches: " + Match);
		System.out.println("Differences: " + Diff);
		System.out.println("Score: " + Math.floor(((Diff/(Diff+Match))*100)) + "%");
		return (int) Math.floor(((Diff/(Diff+Match))*100));
	}

	private LinkedList<Element> unnest(List<Element> Els){
		LinkedList<Element> Unnested = new LinkedList();
		for(Element E : Els) {
			Unnested.add(E);
		}
		Boolean StillHasChildren = false;
		Boolean hasAdded = false;
		for(Element El : Els) {
			if(El.children().size() > 0) {
				for(Element C : El.children()) {
					if(C.children().size() > 0) StillHasChildren = true;
					Unnested.add(C);
					if(!hasAdded) {
						Unnested.add(C.parent());
						hasAdded = true;
					}
				}
				hasAdded = false;
				Unnested.remove(El);
			}
		}
		if(StillHasChildren && Unnested.size()<100000) {
			Unnested = unnest(Unnested);
		}
		return Unnested;
	}

	

}

