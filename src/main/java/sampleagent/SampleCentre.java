package sampleagent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import rescuecore2.messages.Command;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.components.StandardAgent;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import sample.AbstractSampleAgent;

/**
 * A sample centre agent.
 */
public class SampleCentre extends StandardAgent<Building> {
  private static final int evaporationRate = 10;
  private static final Logger LOG = Logger.getLogger(SampleCentre.class);
  private ArrayList<Pheromone> pheromones_fb_AT = new ArrayList<Pheromone>();
  private ArrayList<Pheromone> pheromones_pf = new ArrayList<Pheromone>();
	private Map<EntityID, Set<EntityID>> graph;
	private Set<EntityID>                buildingSet;
  

  @Override
  public String toString() {
    return "Sample centre";
  }


  @Override
  protected void think(int time, ChangeSet changed, Collection<Command> heard) {
    this.evaporationPh();
	  if (time == config
        .getIntValue(kernel.KernelConstants.IGNORE_AGENT_COMMANDS_KEY)) {
      // Subscribe to channels 1 and 2
      sendSubscribe(time, 1, 2);
    }
    for (Command next : heard) {
      LOG.debug("Heard " + next);
    }
    sendRest(time);
  }


  @Override
  protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
    return EnumSet.of(StandardEntityURN.FIRE_STATION,
        StandardEntityURN.AMBULANCE_CENTRE, StandardEntityURN.POLICE_OFFICE);
  }
  /*
   * Getter of the arraylist of pheromones fb AT
   */
  public  ArrayList<Pheromone> getPhermones_fb_AT(){
	  return this.pheromones_fb_AT;
  }
  /**
   * Getter of the arraylist of pheromoes_pf
   * @return pheromones_pf
   */
  public  ArrayList<Pheromone> getPheromones_pf(){
	   return this.pheromones_pf;
	  
  }
  
  @SuppressWarnings("rawtypes")
public void updatePheromone(AbstractSampleAgent agent) {
	  if(agent instanceof SampleFireBrigade && agent instanceof SampleAmbulanceTeam) {
		  
	  }
	  else if (agent instanceof SamplePoliceForce) {
		  
	  }
		  
	  }
  /**
   * methode d'evaporation des pheromones
   */
 public void evaporationPh() {
	Collection<StandardEntity> tout=this.model.getAllEntities();
	
	For (StandardEntity moi : tout){
		if (moi.getID().getPhA()<1) moi.getID().setPhA(0);
		else {
			float perte=moi.getID().getPhA()/evaporationRate;
			moi.getID().setPhA(moi.getID().getPhA()-perte);
		}
	}
 }
  
}