import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class ExecuteScript implements BigCommand {
    private final Server server;
    private final Terminal fileTerminal;
    private final Gson gson = new Gson();
    private final NullPointerChecker np = new NullPointerChecker();
    private final WrongFieldChecker wf = new WrongFieldChecker();
    private String msg = "Script executed";

    public ExecuteScript(Terminal terminal, Server server) {
        this.server = server;
        this.fileTerminal = terminal;
    }

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
//        String path = System.getenv("JSON_ENV");
//        while (!path.endsWith(System.getProperty("file.separator"))) {
//            path = path.replaceAll(".$", "");
//        }
//        fileName = path + fileName;

        try {
            File script = new File((String) command.getArgument());

            if (!script.exists()) {
                msg = ("File does not exist");
                throw new FileNotFoundException();
            }
            if (!script.isFile()) {
                msg = ("This is not a file");
                throw new FileNotFoundException();
            }
            if (!script.canRead()) {
                msg = ("File is unreachable");
                throw new FileNotFoundException();
            }

            if (!script.canWrite()) {
                msg = ("File is unreachable");
                throw new FileNotFoundException();
            }

            Scanner scanner = new Scanner(script);
            while (scanner.hasNext()) {
                String newLine = scanner.nextLine().trim();
                String[] arr = newLine.split(" ", 2);
                Command localCommand = null;
                if (arr.length == 1) {
                    localCommand = new Command(arr[0], null);
                } else {
                    switch (arr[0]) {
                        case "add":
                        case "add_if_max":
                            localCommand = new Command(arr[0], makeMovieFromJSON(arr[1]));
                            break;
                        default:
                            localCommand = new Command(arr[0], arr[1]);
                            break;
                    }
                }
                localCommand.setLoginPassword(command.getLogin(),command.getPassword());
                msg +=fileTerminal.startWithCommand(localCommand).getMsg() +"\n";
            }

        } catch (FileNotFoundException e) {
            msg = (msg + (". Script was not executed"));
        } catch (OutOfMemoryError e) {
            msg = ("File is too big");
        }
        return msg;
    }

    public Movie makeMovieFromJSON(String s) {
        try {
            Movie m = gson.fromJson(s, Movie.class);
            np.checkEverything(m, CheckParametr.WITHOUT_ASKING);
            wf.checkEverything(m, CheckParametr.WITHOUT_ASKING);
            return m;
        } catch (JsonSyntaxException e) {
            msg = ("JSON Syntax error. ");
        } catch (NumberFormatException e) {
            msg = ("Some number-fields have incorrect values. ");
        }
        return null;
    }


}