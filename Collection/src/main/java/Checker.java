import java.text.ParseException;

public interface Checker {
    public void checkEverything(Movie movie, CheckParametr parametr) throws ParseException;

    public void checkEverything(Movie movie) throws ParseException;
}
