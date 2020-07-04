public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            Terminal terminal = new Terminal(client);
            terminal.start();
        } catch (IllegalArgumentException e) {
            System.out.println("Server name you entered is incorrect. Client will be shut down now.");
            System.exit(0);
        }
    }
}