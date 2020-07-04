import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс, реализующий все клиентские методы
 */
public class Client {
    private SocketAddress inetSocket;
    private final Scanner scanner = new Scanner(System.in);
    private DatagramSocket datagramSocket;
    private DatagramPacket input;
    private DatagramPacket output;
    private final byte[] buffer = new byte[64000];
    private final Gson gson = new Gson();
    private final NullPointerChecker np = new NullPointerChecker();
    private final WrongFieldChecker wf = new WrongFieldChecker();
    private String login = "";
    private String password = "";

    public Client() {
        System.out.println("Enter a server address");
        String add = "";

        try {

            while (add == "") {
                String a = scanner.nextLine();
                //if (isRussian(a)){
                //    System.out.println("Entered server name contains inappropriate symbols");
                //}else {
                add = a;
                System.out.println("Server address is now: " + add);
                //}
            }

            System.out.println("Enter a port");
            int port = -1;

            while (port == -1) {
                try {
                    int p = Integer.valueOf(scanner.nextLine().trim());
                    if (p < 0 || p > 65535) {
                        System.out.println("Wrong port was entered");
                    } else {
                        port = p;
                        System.out.println("Port is now: " + port);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entered value is not a number");
                }
            }

            inetSocket = new InetSocketAddress(add, port);
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(3000);
            System.out.println("You can start working");

        } catch (NoSuchElementException e) {
            System.out.println("End of input");
            System.exit(0);
        } catch (SocketException e) {
            System.out.println("Socket unreachable");
            System.exit(0);
        }
    }

    /**
     * Метод для определения русских букв в строке
     *
     * @param str Строка, для анализа
     * @return Результат (True - содержит русские буквы, False - нет)
     */
    public boolean isRussian(String str) {
        char[] chr = str.toCharArray();
        for (int i = 0; i < chr.length; i++) {
            if (chr[i] >= 'А' && chr[i] <= 'я')
                return true;
        }
        return false;
    }

    /**
     * Метод для получения СТРОКИ
     *
     * @return строка, полученная с сервера
     */
    public String getLine() {
        Arrays.fill(buffer, (byte) 0);
        input = new DatagramPacket(buffer, buffer.length);
        try {
            datagramSocket.receive(input);
        } catch (IOException e) {
            System.out.println("Error while receiving message from server");
        }
        byte[] b = input.getData();
        return new String(b);
    }

    /**
     * Метод для записи СТРОКИ на сервер
     *
     * @param s строка, которую надо записать
     */
    public void writeLine(String s) {
        output = new DatagramPacket(s.getBytes(), s.getBytes().length, inetSocket);
        try {
            datagramSocket.send(output);
        } catch (IOException e) {
            System.out.println("Error while sending message to server");
        }
    }

    /**
     * Метод для записи МАССИВА ТИПА byte[] на сервер
     *
     * @param array массив, который надо записать
     */
    public void writeBytes(byte[] array) {
        output = new DatagramPacket(array, array.length, inetSocket);
        try {
            datagramSocket.send(output);
        } catch (IOException e) {
            System.out.println("Error while sending message to server");
        }
    }

    /**
     * Метод для получения МАССИВА ТИПА byte[] с сервера
     *
     * @return массив с сервера
     */
    public byte[] getBytes() {
        Arrays.fill(buffer, (byte) 0);
        input = new DatagramPacket(buffer, buffer.length);
        try {
            datagramSocket.receive(input);
        } catch (IOException e) {
            System.out.println("Error while receiving message from server");
        }
        return input.getData();
    }

    /**
     * Метод для получения ФИЛЬМА с сервера
     *
     * @return фильм с сервера
     */
    public Movie getMovie() {
        return byteArrayToMovie(this.getLine().getBytes());
    }

    /**
     * Метод для записи ФИЛЬМА на сервер
     *
     * @param movie фильм с сервера
     */
    public void writeMovie(Movie movie) {
        this.writeLine(movieToByteArray(movie).toString());
    }

    /**
     * Метод для записи ФИЛЬМА на сервер из JSON-строки
     *
     * @param s строка в формате JSON
     */
    public void writeMovieFromJSON(String s) {
        try {
            Movie c = gson.fromJson(s, Movie.class);
            np.checkEverything(c, CheckParametr.WITH_ASKING);
            wf.checkEverything(c, CheckParametr.WITH_ASKING);
            this.writeMovie(c);
        } catch (JsonSyntaxException e) {
            System.out.print("JSON Syntax error. ");
        } catch (NumberFormatException e) {
            System.out.print("Some number-fields have incorrect values. ");
        }
    }

    /**
     * Метод для конвертации JSON-строки в Movie
     *
     * @param s строка в формате JSON
     * @return фильм
     */
    public Movie makeMovieFromJSON(String s) {
        try {
            Movie c = gson.fromJson(s, Movie.class);
            np.checkEverything(c, CheckParametr.WITH_ASKING);
            wf.checkEverything(c, CheckParametr.WITH_ASKING);
            return c;
        } catch (JsonSyntaxException e) {
            System.out.print("JSON Syntax error. ");
        } catch (NumberFormatException e) {
            System.out.print("Some number-fields have incorrect values. ");
        }
        return null;
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Метод для записи КОМАНДЫ на сервер
     *
     * @param command команда для записи
     */
    public void writeCommand(Command command) {
        command.setLoginPassword(login, sha256(password));
        this.writeBytes(commandToByteArray(command));
    }

    /**
     * Команда для получения ОТВЕТА с сервера
     *
     * @return ответ с сервера
     */
    public Respond getRespond() {
        return byteArrayToRespond(getBytes());
    }

    private byte[] movieToByteArray(Movie movie) {
        return SerializationUtils.serialize(movie);
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

    public String getLogin() {
        return login;
    }

    private byte[] respondToByteArray(Respond respond) {
        return SerializationUtils.serialize(respond);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Respond byteArrayToRespond(byte[] array) {
        return (Respond) SerializationUtils.deserialize(array);
    }
}