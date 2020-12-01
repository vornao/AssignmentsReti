package Assignment9;

import org.apache.commons.cli.*;

public class MainClient {
    private static final int PING_THREADS = 1;
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

        for(int i = 0 ; i < PING_THREADS; i++) {
            Thread t = new Thread(new PingClient(ADDRESS, PORT));
            t.start();
        }

    }
}
