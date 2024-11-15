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
                Socket socket = new Socket(host, port); BufferedReader in
                = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            ) {
            System.out.println("Connected successfully!");

            String serverOut, userIn, welcomeMessage;

            // Server communication
            welcomeMessage = in.readLine();
            System.err.println(welcomeMessage);

            while (!socket.isClosed()) {
               
                // User guess
                userIn = in.readLine(); // stdin
                out.write(userIn + "\n");
                out.flush();

                serverOut = in.readLine();
                
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to host " + host + " on port " + port);
        }
        return 0;
    }
}
