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
	
	/**
	 * Méthode permettant de définir un chemin entre l'entité start et une des entités goal en utilisant un algorithme d'essaim 
	 * @param start
	 * @param goals
	 * @return le chemin choisi sous forme d'une liste d'EntityID
	 */
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
			
			//Si aucun voisin n'a de phéromone, choisi un chemin random + test si chemin n'a pas déja été emprunté 
			if(maxProba==0)
				do {
					nextStart = randomWay(possibilities);
				}while(path.contains(nextStart));
			path.add(nextStart);
			
		}while(!findGoals);
		
		//mise à jour des phéromones 
		for(EntityID step : path) 
			step.pheromoneUpdate();
		
		return path;
		 
	 }
	
	/**
	 * Fonction permettant de choisir un chemin de manière aléatoire parmis un set de chemin possible
	 * @param possibilities
	 * @return l'entité choisi pour la prochaine étape du chemin sous forme d'entityID
	 */
	
	private EntityID randomWay(Set<EntityID> possibilities) {
		EntityID[] possibilitiesTAb = (EntityID[]) possibilities.toArray();
		int random = (int)Math.random()*possibilitiesTAb.length;
		return possibilitiesTAb[random];
	}
	 
	/**
	 * FOnction permettant de calculer la somme des phéromones contenues dans l'ensemble des entités du set passer en paramètre 
	 * @param entities
	 * @return la valeur de la somme sous forme de float
	 */
	private float pheromoneSomme(Set<EntityID> entities) {
		float res = 0;
		for(EntityID entity : entities) 
			res += entity.getPheromone();
		return res;
			
	}
	
	
}
