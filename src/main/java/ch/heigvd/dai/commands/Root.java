package ch.heigvd.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
    description = "A small game to experiment TCP on multithreading connection.",
    version = "1.0.0",
    subcommands = {
      Client.class,
      Server.class,
    },
    scope = CommandLine.ScopeType.INHERIT,
    mixinStandardHelpOptions = true)
public class Root {}
