package ch.heigvd.dai.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import picocli.CommandLine;

@CommandLine.Command(name = "client", description = "Start the client part of the network game.")
public class Client implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-H", "--host"},
      description = "Host to connect to.",
      required = true)
  protected String host;

  @CommandLine.Option(
      names = {"-p", "--port"},
      description = "Port to use (default: ${DEFAULT-VALUE}).",
      defaultValue = "6433")
  protected int port;

  @Override
  public Integer call() {
    System.out.println("Connecting to host " + host + " on port " + port);
    try (Socket socket = new Socket(host, port)) {
      System.out.println("Connected successfully!");
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

      String fromServer;
      String fromUser;

      while ((fromServer = in.readLine()) != null) {
        System.out.println("Server: " + fromServer);
        if (fromServer.equals("Bye."))
          break;

        fromUser = stdIn.readLine();
        if (fromUser != null) {
          System.out.println("Client: " + fromUser);
          out.println(fromUser);
        }
      }
      // Game logic for guessing the number
      int lowerBound = 1;
      int upperBound = 100;
      int numberToGuess = (int) (Math.random() * (upperBound - lowerBound + 1)) + lowerBound;
      System.out.println("Guess a number between " + lowerBound + " and " + upperBound);

      while (true) {
        fromUser = stdIn.readLine();
        if (fromUser != null) {
          try {
        int guess = Integer.parseInt(fromUser);
        if (guess < lowerBound || guess > upperBound) {
          System.out.println("Please guess a number within the range " + lowerBound + " to " + upperBound);
        } else if (guess < numberToGuess) {
          System.out.println("Higher!");
        } else if (guess > numberToGuess) {
          System.out.println("Lower!");
        } else {
          System.out.println("Congratulations! You've guessed the number.");
          out.println("Bye.");
          break;
        }
          } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number.");
          }
        }
      }
      return 0;
    } catch (IOException e) {
      System.err.println("Unable to connect to host " + host + " on port " + port);
      return 1;
    }
    
  }
}
