import java.util.Collections;
import java.util.List;

public class AddIfMax implements BigCommand {
    private final Add add = new Add();

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        Movie movie = (Movie) command.getArgument();
        if (movie != null) {
            if (movieList.isEmpty()) {
                add.execute(movieList,command,sqlController);
                return "Element successfully added to the collection";
            } else {
                if (movie.compareTo(Collections.max(movieList)) > 0) {
                    add.execute(movieList,command,sqlController);
                    return "Movie was successfully added to the collection";
                } else {
                    return "Movie wasn't added to the collection";
                }
            }
        } else {
            return "Element won't be added to the collection.";
        }
    }
}