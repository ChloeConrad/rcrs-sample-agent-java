package sampleagent;

import rescuecore2.worldmodel.EntityID;

public class Pheromone {
	private int poids;
	private EntityID node_1;
	private EntityID node_2;
	
	public Pheromone(int poids, EntityID node_1, EntityID node_2) {
		this.poids = poids;
		this.node_1 = node_1;
		this.node_2 = node_2;
	}
	
	public int getPoids() {
		return poids;
	}
	
	public void antPassage() {
		poids ++;
	}
}
