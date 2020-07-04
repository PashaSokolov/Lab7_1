import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger("MainLogger");
    private static Server server;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int port = -1;
        System.out.println("Write database host");
        if (!scanner.hasNext()) System.exit(0);
        String host = scanner.nextLine();
        System.out.println("Write database port");
        while (port == -1) {
            try {
                if (!scanner.hasNext()) System.exit(0);
                int a = Integer.valueOf(scanner.nextLine().trim());
                if (a < 1024 || a > 65535) {
                    logger.info("Wrong port was entered. Port should be a number from 1024 to 65535");
                } else {
                    port = a;
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Entered value is not a number");
            }
        }
        System.out.println("Write database name");
        if (!scanner.hasNext()) System.exit(0);
        String name = scanner.nextLine();
        System.out.println("Write database username");
        if (!scanner.hasNext()) System.exit(0);
        String username = scanner.nextLine();
        System.out.println("Write database password");
        if (!scanner.hasNext()) System.exit(0);
        String password = scanner.nextLine();
        SQLController sqlController = new SQLController();
        if (!sqlController.initConnection(host, port, name, username, password) ||
                !sqlController.initTables()) {
            logger.info("SQL initialisation error ;(");
            System.exit(0);
        }
        server = new Server();

        List movieList = Collections.synchronizedList(new ArrayList<Movie>());
        try {
            movieList.addAll(sqlController.localCollection());
        } catch (Exception e) {
            e.printStackTrace();
        logger.info("Error in loading collection");
        }
        Terminal t = new Terminal(movieList, server, sqlController);
        t.start();
    }
}