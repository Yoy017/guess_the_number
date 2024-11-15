package ch.heigvd.dai.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Start the server part of the network game.")
public class Server implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-p", "--port"},
            description = "Port to use (default: ${DEFAULT-VALUE}).",
            defaultValue = "6433")
    protected int port;

    private static final int NUMBER_OF_THREADS = 5;

    @Override
    public Integer call() {
        System.out.println("Server is listening on port " + port);
        try (
                ServerSocket serverSocket = new ServerSocket(port); ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);) {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept(); // Accept the client connection
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                executor.submit(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
        return 0;
    }

    static class ClientHandler implements Runnable {

        private final Socket socket;
        private static final int LOWERBOUND = 1, UPPERBOUND = 100;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
                ) {
                // Generate a random number to guess
                int numberToGuess = (int) (Math.random() * (UPPERBOUND - LOWERBOUND + 1)) + LOWERBOUND;
                out.write("Welcome to the number guessing game!\nGuess a number between " + LOWERBOUND + " and " + UPPERBOUND + ": ");

                String guess;
                while ((guess = in.readLine()) != null) {
                    try {
                        int guessedNumber = Integer.parseInt(guess);
                        System.out.println("[Client] " + guessedNumber);
                        if (guessedNumber < LOWERBOUND || guessedNumber > UPPERBOUND) {
                            out.write("Please guess a number within the range " + LOWERBOUND + " to " + UPPERBOUND);
                        } else if (guessedNumber < numberToGuess) {
                            out.write("Higher!");
                        } else if (guessedNumber > numberToGuess) {
                            out.write("Lower!");
                        } else if (guessedNumber == numberToGuess) {
                            out.write("Congratulations! You've guessed the number, Bye.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        out.write("Please enter a valid number.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client handling exception: " + e.getMessage());
            }
        }
    }
}
