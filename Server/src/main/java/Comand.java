import java.util.List;

public interface Comand {

    public String execute(List<Movie> movieList, SQLController sqlController);

}