package sampleagent;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

public class Pheromone {
	private int poids;
	private EntityID road;
	
	public Pheromone(int poids, EntityID road) {
		this.poids = poids;
		this.road = road;
	}
	
	public int getPoids() {
		return poids;
	}
	
	public void antPassage() {
		poids ++;
	}
}
