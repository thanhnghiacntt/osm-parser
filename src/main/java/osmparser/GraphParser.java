package osmparser;

import java.io.File;

public interface GraphParser {
    void parseXml(File file, OSMWayNode outputGraph);
}
