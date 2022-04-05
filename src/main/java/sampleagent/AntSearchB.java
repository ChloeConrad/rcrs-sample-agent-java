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
		    			
		    			//si tu return à ce moment là tu sord de la méthode et donc pas de mise à jour des phéromone 
		    			return path;
		    		}
		    	}
		    	//On verifie la presence de pheromones parmis les voisins
		    	// t'es pas plutôt en train de compter la quantité totale de phéromones parmis les voisins là ?
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
		    	//si il y a des phermonones presentes on parcours les possibilité et on regarde les probabilité d'aller sur l'une qui contient des pheromones et on choisis en fonction de ça
		    	//Pas compris l'algo
		    	else if(presP>0) {
		    		EntityID[] voisinsTAB = (EntityID[]) voisins.toArray();
		    		EntityID next = voisinsTAB[0];
		    		float[] probV= new float[voisinsTAB.length];
		    		for(int i=0;i<probV.length;i++) {
		    			probV[i]=voisinsTAB[i].getPhA()/presP;
		    		}
		    		for(int x=1; x<probV.length; x++)
		    		    probV[x] += probV[x-1];
		    		double rand = Math.random();
		    		for(int x=0; x<probV.length; x++){
		    		    if(rand <= probV[x])
		    		        next = voisinsTAB[x];
		    		        break;
		    		    }
		    		if(path.contains(next)) {
		    			int i = (int)Math.random()*voisinsTAB.length;
			    		if (path.contains(voisinsTAB[i])) next = moi;
			    		else next= voisinsTAB[i];
			    		
		    		}
		    		path.add(next);
		    		moi=next;
		    	}
		    }while(!foundGoal);
		    for (EntityID inst : path){
		    	inst.setPhA(inst.getPhA()+10);
		    }
		    return path;
		  }
	
	 private boolean isGoal( EntityID e, Collection<EntityID> test ) {
		    return test.contains( e );
		  }

}
