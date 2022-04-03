package sampleagent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import sample.SampleSearch;


public class AntSearch {
	
	/**
	 * Map associant à chaque entités un set d'entités voisins 
	 */
	protected Map<EntityID, Set<EntityID>> graph;
	private Set<EntityID>                buildingSet;

	
	public AntSearch(StandardWorldModel world ) {
		  Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {

		      @Override
		      public Set<EntityID> createValue() {
		        return new HashSet<EntityID>();
		      }
		    };
		    buildingSet = new HashSet<EntityID>();
		    for ( Entity next : world ) {
		      if ( next instanceof Area ) {
		        Collection<EntityID> areaNeighbours = ( (Area) next ).getNeighbours();
		        neighbours.get( next.getID() ).addAll( areaNeighbours );
		        if ( next instanceof Building ) 
		        	buildingSet.add( next.getID() );
		      }
		    }
		    
		    this.graph = neighbours;
	}
	
	public List<EntityID> seachBySwarm( EntityID start,
		      Collection<EntityID> goals ){
		List<EntityID> path = new ArrayList<EntityID>();
		EntityID currentStart = start;
		boolean findGoals = false;
		
		//Test si je suis sur un de mes objectifs 
		if(goals.contains(start)) {
			path.add(start);
			start.pheromoneUpdate();
			return path;
		}
		
		
		do {
			Set<EntityID> possibilities = graph.get(currentStart);
			EntityID nextStart = currentStart;
			float maxProba = Float.MIN_VALUE;
			for(EntityID voisin : possibilities) {
				if(goals.contains(voisin)) {
					findGoals = true;
					nextStart = voisin;
					break;
				}
				float proba = voisin.getPheromone()/pheromoneSomme(possibilities);
				if(proba>maxProba) {
					maxProba = proba;
					nextStart = voisin;
				}
			}
			
			//Si aucun voisin n'a de phéromone, choisi un chemin random
			if(maxProba==0)
				nextStart = randomWay(possibilities);
			path.add(nextStart);
			
		}while(!findGoals);
		
		//mise à jour des phéromones 
		for(EntityID step : path) 
			step.pheromoneUpdate();
		
		return path;
		 
	 }
	
	private EntityID randomWay(Set<EntityID> possibilities) {
		EntityID[] possibilitiesTAb = (EntityID[]) possibilities.toArray();
		int random = (int)Math.random()*possibilitiesTAb.length;
		return possibilitiesTAb[random];
	}
	 
	private float pheromoneSomme(Set<EntityID> entities) {
		float res = 0;
		for(EntityID entity : entities) 
			res += entity.getPheromone();
		return res;
			
	}
	
	
}
