import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * реализация команды info
 */

public class Info implements Comand {

    private String msg;


    @Override
    public String execute(List<Movie> movieList, SQLController sqlController) {
        if (movieList.isEmpty()) {
            msg = "Collection is empty";
        } else {
            msg = ("Amount of cities in storage: " + movieList.size() + "\n" +
                    "Collection type: " + movieList.getClass().getSimpleName());
        }
        return msg;
    }
}