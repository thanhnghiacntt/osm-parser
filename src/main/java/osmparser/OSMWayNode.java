package osmparser;

import java.util.*;

public class OSMWayNode {

    private final Map<Long, Node> graphNodes;
    private final Map<Long, Way> graphWays;

    public OSMWayNode() {
        this.graphNodes = new HashMap<>();
        this.graphWays = new HashMap<>();
    }

    public void addNode(Node node) {
        this.graphNodes.put(node.getId(), node);
    }
    
    public void addWay(Way way) {
    	this.graphWays.put(way.getId(), way);
    }
    
	public Map<Long, Way> getGraphWays() {
		return graphWays;
	}

	public Map<Long, Node> getGraphNodes() {
		return graphNodes;
	}
}
