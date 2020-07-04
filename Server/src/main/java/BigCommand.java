import java.util.List;

public interface BigCommand {

    public String execute(List<Movie> movieList, Command command, SQLController sqlController);

}