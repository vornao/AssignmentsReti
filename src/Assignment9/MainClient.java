package Assignment9;

import org.apache.commons.cli.*;

import java.io.IOException;

public class MainClient {
    private static String ADDRESS;
    private static int    PORT;

    public static void main(String[] args){
        Options options = new Options();
        options.addOption("n", "set-server", true, "Server Address");
        options.addOption("p", "set-port", true, "Server Port");
        HelpFormatter helpFormatter = new HelpFormatter();

        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(options, args);
            if(args.length < 2) throw new ParseException("Too few arguments");

            if (commandLine.hasOption("n") || commandLine.hasOption("--set-server")) {
                ADDRESS = (commandLine.getOptionValues("n")[0]);
            }
            if (commandLine.hasOption("p")){
                PORT = Integer.parseInt(commandLine.getOptionValues("p")[0]);
            }
        }catch(ParseException p){
            System.out.println("ERROR: " + p.getMessage());
            helpFormatter.printHelp("java PingClient", options);
            System.exit(-1);
        }

        PingClient pingClient = new PingClient(ADDRESS, PORT);
        try{
            pingClient.run();
        }
        catch (IOException E){
            E.getMessage();
        }
    }
}
