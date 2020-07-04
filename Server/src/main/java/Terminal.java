import org.apache.commons.lang3.SerializationUtils;

import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Terminal {
    static Logger logger = Logger.getLogger("TerminalLogger");
    private final Server server;
    private final List movieList;
    private final Clear clear = new Clear();
    private final String newLine = "";
    private final Info info = new Info();
    private final Show show = new Show();
    private final Add add = new Add();
    private final Update update = new Update();
    private final RemoveById removeById = new RemoveById();
    private final RemoveFirst removeFirst = new RemoveFirst();
    private final AddIfMax addIfMax = new AddIfMax();
    private final ExecuteScript executeScript;
    private final FilterContains filterContains = new FilterContains();
    private final PrintAscending printAscending = new PrintAscending();
    private final PrintDiscending printDiscending = new PrintDiscending();
    private final RemoveIndex remove_index = new RemoveIndex();
    private final Registration registration = new Registration();
    private final SQLController sqlController;
    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService executePool = Executors.newFixedThreadPool(10);
    private final ExecutorService sendPool = Executors.newCachedThreadPool();

    public Terminal(List<Movie> movieList, Server server, SQLController sqlController) {
        this.movieList = movieList;
        this.server = server;
        this.sqlController = sqlController;
        executeScript = new ExecuteScript(this, server);
        logger.finest("Terminal created successfully");
    }

    public void start() {
        readPool.execute(() -> {
            while (true) {
                DatagramPacket datagramPacket = server.getDatagram();
                SocketAddress socketAddress = datagramPacket.getSocketAddress();
                Command command = byteArrayToCommand(datagramPacket.getData());
                execute(command, socketAddress);
            }
        });
    }

    private void execute(Command command, SocketAddress socketAddress) {
        executePool.execute(() -> {
            Respond respond = startWithCommand(command);
            send(respond, socketAddress);
        });
    }

    private void send(Respond respond, SocketAddress socketAddress) {
        sendPool.execute(() -> server.writeRespond(respond, socketAddress));
    }

    private Command byteArrayToCommand(byte[] array) {
        return (Command) SerializationUtils.deserialize(array);
    }


    public Respond startWithCommand(Command command) {
        logger.info("Server is in process of executing the command \"" + command.getCommandname() + "\"");
        Respond respond = new Respond();
        if (command.getCommandname().equals("reg")) {
            respond.setMsg(registration.execute(movieList, command, sqlController));
        } else if (command.getCommandname().equals("login")) {
            if (sqlController.checkUser(command))
                respond.setMsg("Good login");
            else respond.setMsg("Wrong username or password!");
        } else if (sqlController.checkUser(command))
            switch (command.getCommandname()) {
                case "server_exit":
                    logger.info("Server is closed now");
                    System.exit(0);
                    break;
                case "clear":
                    respond.setMsg(clear.execute(movieList, command, sqlController));
                    break;
                case "help":
                    respond.setMsg(
                            "help : вывести справку по доступным командам\n" +
                                    "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                    "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                                    "add {element} : добавить новый элемент в коллекцию\n" +
                                    "update {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                                    "remove_by_id id : удалить элемент из коллекции по его id\n" +
                                    "clear : очистить коллекцию\n" +
                                    "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                                    "exit : завершить программу \n" +
                                    "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index)\n" +
                                    "remove_first : удалить первый элемент из коллекции\n" +
                                    "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                    "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку\n" +
                                    "print_ascending : вывести значения поля mpaaRating всех элементов в порядке возрастания\n" +
                                    "print_discending : вывести значения поля mpaaRating всех элементов в порядке убывания"
                    );
                    break;
                case "info":
                    respond.setMsg(info.execute(movieList, sqlController));
                    break;
                case "remove_at":
                    respond.setMsg(remove_index.execute(movieList, command, sqlController));
                    break;
                case "show":
                    respond.setMsg(show.execute(movieList, sqlController));
                    break;
                case "print_ascending":
                    respond.setMsg(printAscending.execute(movieList, sqlController));
                    break;
                case "print_discending":
                    respond.setMsg(printDiscending.execute(movieList, sqlController));
                    break;
                case "add":
                    respond.setMsg(add.execute(movieList, command, sqlController));
                    break;
                case "add_if_max":
                    respond.setMsg(addIfMax.execute(movieList, command, sqlController));
                    break;
                case "remove_first":
                    respond.setMsg(removeFirst.execute(movieList, command, sqlController));
                    break;
                case "filter_contains_name":
                    respond.setMsg(filterContains.execute(movieList, command, sqlController));
                    break;
                case "remove_by_id":
                    respond.setMsg(removeById.execute(movieList, command, sqlController));
                    break;
                case "execute_script":
                    LoopAnalyzer la = new LoopAnalyzer();
                    String msg = "EoI\n";
                    try {
                        if (la.isLooped((String) command.getArgument())) {
                            logger.warning("Loop was found");
                            msg += "Loop found. Remove it or try another script";
                        } else {
                            msg += executeScript.execute(movieList, command, sqlController);
                        }
                    } catch (FileNotFoundException e) {
                        logger.warning("File not found");
                        msg += "File not found\n";
                    } catch (SecurityException e) {
                        logger.warning("File unacceptable");
                        msg += "File unacceptable";
                    }
                    respond.setMsg(msg);
                    break;
                case "update":
                    respond.setMsg(update.execute(movieList, command, sqlController));
                    break;
                default:
                    logger.info("Unknown command");
                    respond.setMsg("Unknown command, write \"help\"");
                    break;
            }
        else {
            respond.setMsg("Wrong username or password!\n" +
                    "Write \"reg [username] [password]\" or \"login [username] [password]\"");
            respond.setLogin(false);
        }
        logger.info("Command executed successfully");
        return respond;
    }
}