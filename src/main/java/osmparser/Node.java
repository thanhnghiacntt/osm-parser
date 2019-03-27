package osmparser;

import java.util.HashMap;

public class Node implements Comparable<Object>{

    private transient long id;  // id
    private double la;          // latitude
    private double lo;          // longitude
	private HashMap<String, String> tags;
    public Node(long id, double la, double lo, HashMap<String, String> tags) {
        this.id = id;
        this.la = la;
        this.lo = lo;
        this.setTags(tags);
    }

   
    public long getId() {
        return id;
    }

    public double getLa() {
        return la;
    }

    public double getLo() {
        return lo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Node node = (Node) o;
        if (id != node.id){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public int compareTo(Object o) {
        if(o == null || o.getClass() != this.getClass()){
            return 0;
        }
        Node n = (Node) o;

        return (int)(this.getId() - n.getId());
    }

	/**
	 * @return the tags
	 */
	public HashMap<String, String> getTags() {
		return tags;
	}


	/**
	 * @param tags the tags to set
	 */
	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}


	@Override
    public String toString() {
        return "id: " + id;
    }
}
