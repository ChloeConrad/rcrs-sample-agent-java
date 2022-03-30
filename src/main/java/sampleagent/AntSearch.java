package sampleagent;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;
import sample.SampleSearch;


public class AntSearch extends SampleSearch {
	
	 public AntSearch(StandardWorldModel world) {
		super(world);
	}

	public List<EntityID> breadthFirstSearchForBlokade( EntityID start,
		      Collection<EntityID> goals ){
		 List<EntityID> open = new LinkedList<EntityID>();
		    Map<EntityID, EntityID> ancestors = new HashMap<EntityID, EntityID>();
		    open.add( start );
		    EntityID next = null;
		    boolean found = false;
		    ancestors.put( start, start );
		    do {
		      next = open.remove( 0 );
		      if ( isGoal( next, goals ) ) {
		        found = true;
		        break;
		      }
		      Collection<EntityID> neighbours = get( next );
		      if ( neighbours.isEmpty() ) {
		        continue;
		      }
		      for ( EntityID neighbour : neighbours ) {
		        if ( isGoal( neighbour, goals ) ) {
		          ancestors.put( neighbour, next );
		          next = neighbour;
		          found = true;
		          break;
		        } else {
		          if ( !ancestors.containsKey( neighbour ) ) {
		            open.add( neighbour );
		            ancestors.put( neighbour, next );
		          }
		        }
		      }
		    } while ( !found && !open.isEmpty() );
		    if ( !found ) {
		      // No path
		      return null;
		    }
		    // Walk back from goal to start
		    EntityID current = next;
		    List<EntityID> path = new LinkedList<EntityID>();
		    do {
		      path.add( 0, current );
		      current = ancestors.get( current );
		      if ( current == null ) {
		        throw new RuntimeException(
		            "Found a node with no ancestor! Something is broken." );
		      }
		    } while ( current != start );
		    return path;
		 
	 }
	 
	 public List<EntityID> breadthFirstSearchForCivilian( EntityID start,
		      Collection<EntityID> goals ){
		 return null;
		 
	 }
}
