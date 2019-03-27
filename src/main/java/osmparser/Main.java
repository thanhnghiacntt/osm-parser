package osmparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
    private static String PROGRAM_NAME = "osmparser";
    private static int EXIT_FAIL = 1;

    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            error(ex.getMessage());
            new HelpFormatter().printHelp(PROGRAM_NAME, options);
            System.exit(EXIT_FAIL);
        }

        boolean notQuiet = cmd.hasOption("quiet");

        String file = cmd.getOptionValue("file");
        if (notQuiet) {
            info("Parsing files: " + file);
        }


        String output = cmd.getOptionValue("output");

        if(notQuiet && output != null) {
            info("Output file name: " + output);
        } else if(notQuiet){
            info("Output file name: graph.json");
        }

        try {
        	boolean isError = false;
        	Osmparser osm = createOsmparser(file, output);
        	osm.start();
        	OSMWayNode graph = osm.getGraph();
        	Map<Long, Way> ways = graph.getGraphWays();
        	Map<Long, Node> nodes = graph.getGraphNodes();
        	Set<Long> keyWays = ways.keySet();
        	List<Long> list = new ArrayList<Long>();
        	for (Long key : keyWays) {
        		Way way = ways.get(key);
        		for (Long id : way.getWayNodesIds()) {
					if(!nodes.containsKey(id)) {
						isError = true;
						list.add(id);
					}
				}
			}
        	if(isError) {
        		System.err.println("All nodes don't have refrence");
        		for (Long id : list) {
					System.err.println(id);
				}
        		System.err.println("Total nodes is error: "+list.size());
        	}else {
        		System.out.println("File: " + file +" don't have error");        		
        	}
        } catch (RuntimeException ex) {
            if (!(ex.getCause() instanceof IOException)) {
                throw ex;
            }
            System.out.println("Error while reading file: " + ex.getCause().getMessage());
        }
    }


    private static Osmparser createOsmparser(String file, String output) {
        StreamingXmlGraphParser graphParser = new StreamingXmlGraphParser();
        if(output == null) return new Osmparser(file, graphParser);
        return new Osmparser(file,  graphParser);
    }

    private static Options createOptions() {
        Options options = new Options();

        Option file = Option.builder("f")
            .longOpt("file")
            .desc("the osm (xml) map files to be parsed")
            .hasArgs()
            .required()
            .build();
        options.addOption(file);
        return options;
    }

    // TODO: use a proper logger library
    private static void info(String message) {
        System.out.println(message);
    }


    private static void error(String message) {
        System.out.println(message);
    }
}
