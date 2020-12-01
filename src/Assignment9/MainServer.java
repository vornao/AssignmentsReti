package Assignment9;

import org.apache.commons.cli.*;

public class MainServer {
    private static PingServer pingServer;
    private static int PORT = 0;
    private static String ADDRESS = "";

    public static void main(String args[]) throws InterruptedException {

        Options options = new Options();
        options.addOption("p", "set-port", true, "Server Port");

        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(options, args);
            if(args.length < 1) throw new ParseException("ERROR: too few arguments");
            if (commandLine.hasOption("p")) {
                PORT = Integer.parseInt(commandLine.getOptionValues("p")[0]);
            }
        } catch (ParseException p) {
            System.out.println("ERROR" + p.getMessage());
            helpFormatter.printHelp("java PingServer", options);
            System.exit(-1);
        }

        System.out.println("Server ready: port " + PORT);
        pingServer = new PingServer(PORT, 300);
        Thread t = new Thread(pingServer);
        t.start();
        t.join();
    }
}
