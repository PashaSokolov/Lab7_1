import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

public class Update implements BigCommand {
    private String msg;

    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        int userId = sqlController.getUserId(command.getLogin());
        if (command.getArgument() == null || command.getAdditional() == null) {
            return "Arguments are either null or filled up incorrectly";
        }
        Long idUpdate = (Long) command.getAdditional();
        Movie movie = (Movie) command.getArgument();
        long updatedSqlId = updateSQL(movie, idUpdate, userId, sqlController.getConnection());

        if (movie == null && !Objects.isNull(idUpdate)) {
            msg = "Movie is null";
        } else if (Objects.isNull(idUpdate)) {
            msg = "ID is null";
        } else if (updatedSqlId != -1) {
            int prevSize = movieList.size();
            movieList.removeIf(city1 -> (city1.getId() == idUpdate));
            if (prevSize != movieList.size()) {
                movie.setId(idUpdate);
                movie.setUsername(command.getLogin());
                movieList.add(movie);
                msg = "Movie " + idUpdate + " was updated successfully";
            } else {
                msg = "Needed movie wasn't found";
            }
        } else {
            msg = "The item with id " + idUpdate + " does not exist or it’s not yours";
        }
        return msg;
    }

    private int updateSQL(Movie movie, long idUpdate, int userId, Connection connection) {
        int id = -1;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE movies " +
                            "set name=?, " +
                            "x=?, " +
                            "y=?, " +
                            "creation_date=?, " +
                            "oscars_count=?, " +
                            "movie_genre_id=(select id from movie_genres where movie_genre_name = ?), " +
                            "mpaa_rating_id=(select id from mpaa_ratings where mpaa_rating_name = ?), " +
                            "director_name=?, " +
                            "director_passport_id=?, " +
                            "director_hair_color_id=(select id from colors where color_name = ?), " +
                            "director_country_id=(select id from country where country_name = ?), " +
                            "director_location_x=?, " +
                            "director_location_y=?, " +
                            "director_location_z=? " +
                            "where  id =? and user_id = ? returning id"
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
            statement.setLong(15, idUpdate);
            statement.setInt(16, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (Exception ignore) {
        }
        return id;
    }
}