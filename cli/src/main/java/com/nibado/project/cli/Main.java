package com.nibado.project.cli;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String... argv) throws Exception {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, argv);

        if (cmd.hasOption("h") || cmd.getOptions().length == 0 || !cmd.hasOption("u")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("cli", options);
            return;
        }

        Client client = new Client(cmd.getOptionValue("u"));
        Reader reader;

        if (cmd.hasOption("l") && cmd.hasOption("p")) {
            reader = new Reader(client, cmd.getOptionValue("l"), cmd.getOptionValue("p"));
        } else {
            reader = new Reader(client);
        }

        if (cmd.hasOption("e")) {
            reader.execute(cmd.getOptionValue("e"));
        } else {
            reader.run();
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Shows help")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("u")
                .longOpt("u")
                .desc("Base url")
                .hasArg(true)
                .type(String.class)
                .build());

        options.addOption(Option.builder("l")
                .longOpt("l")
                .desc("Login (email)")
                .hasArg(true)
                .type(String.class)
                .build());

        options.addOption(Option.builder("p")
                .longOpt("password")
                .desc("Password")
                .hasArg(true)
                .type(String.class)
                .build());

        options.addOption(Option.builder("e")
                .longOpt("execute")
                .desc("Execute commands")
                .hasArg(true)
                .type(String.class)
                .build());

        return options;
    }
}
