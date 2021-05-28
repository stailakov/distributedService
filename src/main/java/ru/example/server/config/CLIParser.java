package ru.example.server.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author TaylakovSA
 */
public class CLIParser {

    public CommandLine parseCLI(String [] args) {

        Options options = new Options();

        Option port = new Option("p", "port", true, "service port");
        port.setRequired(true);
        options.addOption(port);

        Option host = new Option("h", "host", true, "service host");
        host.setRequired(true);
        options.addOption(host);

        Option heartbeatTimeout = new Option("election", "election-timeout", true, "Election timeout");
        heartbeatTimeout.setRequired(true);
        options.addOption(heartbeatTimeout);

        Option electionTimeout = new Option("heartbeat", "heartbeat-timeout", true, "Heartbeat timeout");
        electionTimeout.setRequired(true);
        options.addOption(electionTimeout);
        Option nodeId = new Option("n", "node-id", true, "ID Node");
        nodeId.setRequired(true);
        options.addOption(nodeId);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("help", options);

            System.exit(1);
        }
        return cmd;
    }

}
