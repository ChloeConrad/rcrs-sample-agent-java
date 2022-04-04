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
import sample.AbstractSampleAgent;

public final class AntSearchB {
	private Map<EntityID, Set<EntityID>> graph;
	private Set<EntityID>                buildingSet;
	public AntSearchB( StandardWorldModel world) {
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
		        if ( next instanceof Building ) buildingSet.add( next.getID() );
		      }
		    }
		    graph= neighbours ;// TODO Auto-generated constructor stub
	}
	
	public List<EntityID> antSearch( EntityID start,
		      Collection<EntityID> goals ) {
		    List<EntityID> path = new LinkedList<EntityID>();
		    boolean foundGoal = false;
		    EntityID moi = start;
		    float presP=0;
		    //Piqué à chloé si jamais on est sur le goal on reste sur place
		    if(goals.contains(start)) {
				path.add(start);
				foundGoal= true;
				return path;
			}
		    do
		    {
		    	Set<EntityID> voisins = graph.get(moi);
		    	//On verifie d'abord que l'un de nos objectifs n'est pas un de nos voisins
		    	for (EntityID voisin : voisins) {
		    		if(goals.contains(voisin)) {
		    			path.add(voisin);
		    			foundGoal=true;
		    			return path;
		    		}
		    	}
		    	//On verifie la presence de pheromones parmis les voisins
		    	for (EntityID voisin : voisins) {
		    		presP=presP+voisin.getPhA();
		    	}
		    	//si il n'y a pas de pheromones presente on choisis un chemin au hasard sur lequel on est pas deja passé
		    	if (presP==0) {
		    		EntityID[] voisinsTAB = (EntityID[]) voisins.toArray();
		    		int i = (int)Math.random()*voisinsTAB.length;
		    		if (path.contains(voisinsTAB[i])) break;
		    		path.add(voisinsTAB[i]);
		    		moi=voisinsTAB[i];
		    	}
		    	//si il y a des phermonones presentes on parcours les possibilité et on prends celle qui a le plus de pheromones
		    	else if(presP>0) {
		    		EntityID[] voisinsTAB = (EntityID[]) voisins.toArray();
		    		EntityID next = voisinsTAB[0];
		    		for (int i=0;i<voisinsTAB.length;i++) {
		    			if(next.getPhA()<voisinsTAB[i].getPhA() && !path.contains(voisinsTAB[i])) next = voisinsTAB[i];
		    		}
		    		path.add(next);
		    		moi=next;
		    	}
		    }while(!foundGoal);

		    return path;
		  }
	
	 private boolean isGoal( EntityID e, Collection<EntityID> test ) {
		    return test.contains( e );
		  }

}
