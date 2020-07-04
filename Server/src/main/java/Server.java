import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    static Logger logger = Logger.getLogger("ServerLogger");
    private final Scanner scanner = new Scanner(System.in);
    private final byte[] buffer = new byte[64000];
    private int port;
    private DatagramSocket socket;
    private DatagramPacket output;
    private SocketAddress socketAddress;

    public Server() {
        logger.info("Enter a port number to start a server");
        int port = -1;

        while (port == -1) {
            try {
                if (!scanner.hasNext()) System.exit(0);
                int a = Integer.valueOf(scanner.nextLine().trim());
                if (a < 1024 || a > 65535) {
                    logger.info("Wrong port was entered. Port should be a number from 1024 to 65535");
                } else {
                    port = a;
                    logger.info("Port is now: " + port);
                    this.port = port;
                    socketAddress = new InetSocketAddress(port);
                    socket = new DatagramSocket(port);
                    logger.info("Server at port " + port + " is working now");
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Entered value is not a number");
            } catch (SocketException e) {
                logger.log(Level.WARNING, "Entered port is already in use");
                port = -1;
            }
        }
    }

    public void writeLine(String s, SocketAddress socketAddress) {
        output = new DatagramPacket(s.getBytes(), s.getBytes().length, socketAddress);
        try {
            socket.send(output);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void writeBytes(byte[] array, SocketAddress socketAddress) {
        output = new DatagramPacket(array, array.length, socketAddress);
        try {
            socket.send(output);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public DatagramPacket getDatagram() {
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(datagramPacket);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return datagramPacket;
    }

    public void writeMovie(Movie movie, InetSocketAddress inetSocketAddress) {
        this.writeLine(movieToByteArray(movie).toString(), inetSocketAddress);
    }

    public void writeRespond(Respond respond, SocketAddress socketAddress) {
        this.writeBytes(respondToByteArray(respond), socketAddress);
    }

    private byte[] movieToByteArray(Movie movie) {
        return SerializationUtils.serialize(movie);
    }

    private byte[] respondToByteArray(Respond respond) {
        return SerializationUtils.serialize(respond);
    }

    private Movie byteArrayToMovie(byte[] array) {
        return (Movie) SerializationUtils.deserialize(array);
    }

    private byte[] commandToByteArray(Command command) {
        return SerializationUtils.serialize(command);
    }

    private Command byteArrayToCommand(byte[] array) {
        return (Command) SerializationUtils.deserialize(array);
    }

    private Respond byteArrayToRespond(byte[] array) {
        return (Respond) SerializationUtils.deserialize(array);
    }
}