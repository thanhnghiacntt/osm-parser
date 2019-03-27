package osmparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import com.fasterxml.aalto.stax.InputFactoryImpl;

public class StreamingXmlGraphParser implements GraphParser {

	private final XMLInputFactory inputFactory;
	private OSMWayNode graph;

	public StreamingXmlGraphParser() {
		this.inputFactory = InputFactoryImpl.newInstance();
	}

	@Override
	public void parseXml(File file, OSMWayNode outputGraph) {
		this.graph = outputGraph;

		XMLStreamReader reader = null;
		try (InputStream stream = new FileInputStream(file)) {
			reader = inputFactory.createXMLStreamReader(stream);
			readGraph(reader);
		} catch (IOException | XMLStreamException ex) {
			throw new RuntimeException(ex);
		} finally {
			safeClose(reader);
		}
	}

	private void readGraph(XMLStreamReader reader) throws XMLStreamException {
		while (reader.hasNext()) {
			int eventType = reader.next();
			if (eventType != XMLEvent.START_ELEMENT) {
				continue;
			}

			String elementName = reader.getLocalName();
			if (elementName.equals("node")) {
				readNodeElement(reader);
			} else if (elementName.equals("way")) {
				readWayElement(reader);
			}
		}
	}

	private void readNodeElement(XMLStreamReader reader) throws XMLStreamException {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		double lat = Double.parseDouble(reader.getAttributeValue(null, "lat"));
		double lon = Double.parseDouble(reader.getAttributeValue(null, "lon"));
		HashMap<String, String> wayTags = new HashMap<>();
		readTags(reader, wayTags);
		Node node = new Node(id, lat, lon, wayTags);
		this.graph.addNode(node);
	}

	private void readWayElement(XMLStreamReader reader) throws XMLStreamException {
		long id = Long.parseLong(reader.getAttributeValue(null, "id"));
		ArrayList<Long> wayNodesIds = new ArrayList<>();
		HashMap<String, String> wayTags = new HashMap<>();
		readIdsAndTags(reader, wayNodesIds, wayTags);
		Way way = new Way(id, wayNodesIds, wayTags);
		this.graph.addWay(way);
	}

	private static void readIdsAndTags(XMLStreamReader reader, ArrayList<Long> nodesOut,
			HashMap<String, String> tagsOut) throws XMLStreamException {
		// when we're here, reader cursor is currently at START_ELEMENT, that is, <way>
		int nodeChildDepth = 1;

		while (reader.hasNext()) {
			int eventType = reader.next();

			if (eventType == XMLEvent.START_ELEMENT) {
				nodeChildDepth++;

				String elementName = reader.getLocalName();
				if ("nd".equals(elementName)) {
					String ndRefAttribute = reader.getAttributeValue(null, "ref");
					Long id = Long.parseLong(ndRefAttribute);
					nodesOut.add(id);
				} else if ("tag".equals(elementName)) {
					// according to the schema, "k" and "v" are required
					String tagKeyAttribute = reader.getAttributeValue(null, "k");
					String tagKeyValue = reader.getAttributeValue(null, "v");
					tagsOut.put(tagKeyAttribute, tagKeyValue);
				}
			} else if (eventType == XMLEvent.END_ELEMENT) {
				nodeChildDepth--;

				String elementName = reader.getLocalName();
				if (nodeChildDepth == 0 && "way".equals(elementName)) {
					return;
				}
				if (nodeChildDepth <= 0) {
					// Found the end of an XML tag but it was not </way> even though it should
					// have been at this level, e.g. <way><nd /></notWayTag>
					Location location = reader.getLocation();
					String msg = String.format(
							"Found unbalanced XML tags at line %d col %d, file character pos %d! Expecting </way> but was </%s>",
							location.getLineNumber(), location.getColumnNumber(), location.getCharacterOffset(),
							elementName);
					throw new RuntimeException(msg);
				}
			}
		}

		throw new RuntimeException("Reached end of XMLStream inside readIdsAndTags! Expected </way>!");
	}

	private static void readTags(XMLStreamReader reader, HashMap<String, String> tagsOut) throws XMLStreamException {
// when we're here, reader cursor is currently at START_ELEMENT, that is, <way>
		int nodeChildDepth = 1;

		while (reader.hasNext()) {
			int eventType = reader.next();

			if (eventType == XMLEvent.START_ELEMENT) {
				nodeChildDepth++;

				String elementName = reader.getLocalName();
				if ("tag".equals(elementName)) {
					String tagKeyAttribute = reader.getAttributeValue(null, "k");
					String tagKeyValue = reader.getAttributeValue(null, "v");
					tagsOut.put(tagKeyAttribute, tagKeyValue);
				}
			} else if (eventType == XMLEvent.END_ELEMENT) {
				nodeChildDepth--;

				String elementName = reader.getLocalName();
				if (nodeChildDepth == 0 && "node".equals(elementName)) {
					return;
				}
				if (nodeChildDepth <= 0) {
					Location location = reader.getLocation();
					String msg = String.format(
							"Found unbalanced XML tags at line %d col %d, file character pos %d! Expecting </way> but was </%s>",
							location.getLineNumber(), location.getColumnNumber(), location.getCharacterOffset(),
							elementName);
					throw new RuntimeException(msg);
				}
			}
		}

		throw new RuntimeException("Reached end of XMLStream inside readIdsAndTags! Expected </way>!");
	}

	private static void safeClose(XMLStreamReader reader) {
		if (reader == null)
			return;

		try {
			reader.close();
		} catch (Exception ex) {
		}
	}
}
