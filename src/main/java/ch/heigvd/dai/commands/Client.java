package ch.heigvd.dai.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));) {
            System.out.println("Connected successfully!");

            String serverOut, userIn, welcomeMessage;

            // Server communication
            welcomeMessage = in.readLine();
            System.out.println(welcomeMessage);

            while (!socket.isClosed()) {
                // User input
                userIn = stdIn.readLine();
                System.out.println(userIn);
                out.write(userIn + "\n");
                out.flush();

                // Server response
                serverOut = in.readLine();
                System.out.println("[Server] " + serverOut);

                if (serverOut.contains("Congratulations! You've guessed the number, Bye.")) {
                    socket.close();
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to host " + host + " on port " + port);
        }
        return 0;
    }
}
