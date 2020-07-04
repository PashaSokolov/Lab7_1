import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Logger;

/**
 * реализация команды add
 */
public class Add implements BigCommand {
    static Logger logger = Logger.getLogger("AddLogger");

    @Override
    public String execute(List movieList, Command command, SQLController sqlController) {
        Movie movie = (Movie) command.getArgument();
        if (movie == null) {
            logger.warning("Original movie is null");
            return "Original movie is null";
        } else {
            int id = addSQL(movie, sqlController.getUserId(command.getLogin()), sqlController.getConnection());
            if (id != -1)
                try {
                    movie.setId(id);
                    movie.setUsername(command.getLogin());
                    int prevSize = movieList.size();

                    if (!movieList.contains(movie)) {
                        movieList.add(movie);
                    }
                    if (prevSize != movieList.size()) {
                        logger.info("Element was successfully added to the collection");
                        return ("Element was successfully added to the collection.");
                    } else {
                        logger.info("Collection already contains such element");
                        return ("Collection already contains such element");
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Some number-fields have incorrect values");
                    return ("Some number-fields have incorrect values. Try changing them");
                }
            else return "Item has not been added";
        }
    }

    private int addSQL(Movie movie, int userId, Connection connection) {
        int id = -1;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into MOVIES" +
                            "(NAME , X, Y, CREATION_DATE, OSCARS_COUNT, MOVIE_GENRE_ID, MPAA_RATING_ID," +
                            "DIRECTOR_NAME,DIRECTOR_PASSPORT_ID,DIRECTOR_HAIR_COLOR_ID,DIRECTOR_COUNTRY_ID," +
                            "DIRECTOR_LOCATION_X,DIRECTOR_LOCATION_Y,DIRECTOR_LOCATION_Z, user_id) " +
                            "values (?,?,?,?,?,(select id from MOVIE_GENRES where MOVIE_GENRE_NAME = ?)," +
                            "(select id from MPAA_RATINGS where MPAA_RATING_NAME = ?),?,?," +
                            "(select id from COLORS where COLOR_NAME = ?)," +
                            "(select id from COUNTRY where COUNTRY_NAME = ?),?,?,?,?) returning id"
            );
            statement.setString(1, movie.getName());
            statement.setLong(2, movie.getCoordinates().getX());
            statement.setLong(3, movie.getCoordinates().getY());
            statement.setTimestamp(4, new Timestamp(movie.getCreationDate().toEpochSecond(ZoneOffset.UTC) * 1000));
            statement.setLong(5, movie.getOscarcount());
            statement.setString(6, movie.getMoviegenre().toString());
            statement.setString(7, movie.getMpaaRating().toString());
            statement.setString(8, movie.getDirector().getName());
            statement.setString(9, movie.getDirector().getPassportID());
            statement.setString(10, movie.getDirector().getHairColor().toString());
            statement.setString(11, movie.getDirector().getCountry().toString());
            statement.setFloat(12, movie.getDirector().getLocation().getX());
            statement.setInt(13, movie.getDirector().getLocation().getY());
            statement.setFloat(14, movie.getDirector().getLocation().getZ());
            statement.setInt(15, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
                logger.info("Added Movie id is " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}