package osmparser;

import java.util.ArrayList;
import java.util.HashMap;

public class Way {
	private transient long id;  // id
	private ArrayList<Long> wayNodesIds;
	private HashMap<String, String> wayTags;
	public Way(long id, ArrayList<Long> wayNodesIds, HashMap<String, String> wayTags) {
		this.setId(id);
		this.wayNodesIds = wayNodesIds;
		this.wayTags = wayTags;
	}
	public ArrayList<Long> getWayNodesIds() {
		return wayNodesIds;
	}
	public void setWayNodesIds(ArrayList<Long> wayNodesIds) {
		this.wayNodesIds = wayNodesIds;
	}
	public HashMap<String, String> getWayTags() {
		return wayTags;
	}
	public void setWayTags(HashMap<String, String> wayTags) {
		this.wayTags = wayTags;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
