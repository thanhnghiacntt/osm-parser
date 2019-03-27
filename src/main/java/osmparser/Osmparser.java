package osmparser;

import java.io.File;
import java.io.IOException;

public class Osmparser {

    private final String mapFile;
    private final GraphParser parser;
    private OSMWayNode graph;

    public Osmparser(String mapFile, GraphParser graphParser) {
        this.mapFile = mapFile;
        this.parser = graphParser;
    }

    public void start() throws IOException {
    	setGraph(parseAll());
        
    }

    private OSMWayNode parseAll() throws IOException {
        OSMWayNode graph = new OSMWayNode();
        File file = new File(mapFile);
        parser.parseXml(file, graph);
        return graph;
    }

	/**
	 * @return the graph
	 */
	public OSMWayNode getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(OSMWayNode graph) {
		this.graph = graph;
	}
}
