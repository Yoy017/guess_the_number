package ch.heigvd.dai.commands;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import picocli.CommandLine;

@CommandLine.Command(name = "server", description = "Start the server part of the network game.")
public class Server implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-p", "--port"},
            description = "Port to use (default: ${DEFAULT-VALUE}).",
            defaultValue = "6433")
    protected int port;

    @Override
    public Integer call() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        // Handle the client connection in this block
                        System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                        // Add your client handling code here
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Client handling exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        return 0;
    }
}
