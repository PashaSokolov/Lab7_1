import java.util.List;

/**
 * реализация команды show
 */
public class Show implements Comand {

    @Override
    public String execute(List<Movie> movieList, SQLController sqlController) {
        String msg = "";
        if (movieList.isEmpty()) {
            msg = ("Collection is empty");
        } else {
            for (Movie c : movieList) {
                msg = msg + (c.toString() + "\n----------//----------\n");
            }
            msg = msg.substring(0, msg.length() - 2);

        }
        return msg;
    }
}