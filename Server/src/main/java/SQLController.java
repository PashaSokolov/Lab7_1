import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс для работы с базай данных
 */
public class SQLController {
    static Logger logger = Logger.getLogger("SaveLogger");
    private Connection connection;

    public boolean initConnection(String host, int port, String dataBaseName, String user, String password) {
        String databaseUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dataBaseName;
        try {
            logger.info("Database URL is " + databaseUrl);
            connection = DriverManager.getConnection(databaseUrl, user, password);
            logger.info("Database '" + connection.getCatalog() + "' is connected ");
            return true;
        } catch (SQLException e) {
            logger.info("SQLException ;(: " + e.toString());
            return false;
        }

    }

    public boolean initTables() {
        try {
            Statement statement = connection.createStatement();

            statement.execute("create table if not exists users (" +
                    "id serial primary key not null, " +
                    "UserName text unique , " +
                    "Password bytea)"
            );

            statement.execute("CREATE TABLE if not exists COLORS " +
                    "(Id serial primary key, " +
                    "COLOR_NAME text NOT NULL UNIQUE )");
            try {
                for (Color color : Color.values())
                    statement.execute("insert into COLORS(COLOR_NAME) values('" + color + "') ");
            } catch (SQLException ignore) {
            }


            statement.execute("CREATE TABLE if not exists MPAA_RATINGS " +
                    "(Id serial primary key, " +
                    "MPAA_RATING_NAME text NOT NULL UNIQUE )");
            try {
                for (MpaaRating mpaaRating : MpaaRating.values())
                    statement.execute("insert into MPAA_RATINGS(MPAA_RATING_NAME) values('" + mpaaRating + "') ");
            } catch (SQLException ignore) {
            }

            statement.execute("CREATE TABLE if not exists MOVIE_GENRES " +
                    "(Id serial primary key, " +
                    "MOVIE_GENRE_NAME text NOT NULL UNIQUE )");
            try {
                for (MovieGenre movieGenre : MovieGenre.values())
                    statement.execute("insert into MOVIE_GENRES(MOVIE_GENRE_NAME) values('" + movieGenre + "') ");
            } catch (SQLException ignore) {
            }

            statement.execute("CREATE TABLE if not exists COUNTRY" +
                    "(Id serial primary key, " +
                    "COUNTRY_NAME text NOT NULL UNIQUE )");
            try {
                for (Country country : Country.values())
                    statement.execute("insert into COUNTRY(COUNTRY_NAME) values('" + country + "') ");
            } catch (SQLException ignore) {
            }

            statement.execute("create table if not exists MOVIES " +
                    "(ID serial primary key," +
                    "NAME text, " +
                    "X bigint , " +
                    "Y bigint , " +
                    "CREATION_DATE timestamp, " +
                    "OSCARS_COUNT bigint, " +
                    "MOVIE_GENRE_ID  int," +
                    "MPAA_RATING_ID int, " +
                    "USER_ID integer, " +
                    "DIRECTOR_NAME text, " +
                    "DIRECTOR_PASSPORT_ID text, " +
                    "DIRECTOR_HAIR_COLOR_ID int, " +
                    "DIRECTOR_COUNTRY_ID int, " +
                    "DIRECTOR_LOCATION_X float, " +
                    "DIRECTOR_LOCATION_Y int, " +
                    "DIRECTOR_LOCATION_Z float, " +
                    "foreign key (MOVIE_GENRE_ID) references MOVIE_GENRES(ID), " +
                    "foreign key (MPAA_RATING_ID) references MPAA_RATINGS(ID), " +
                    "foreign key (DIRECTOR_COUNTRY_ID) references COUNTRY(ID), " +
                    "foreign key (DIRECTOR_HAIR_COLOR_ID) references COLORS(ID), " +
                    "foreign key (user_id) references users(id)) "
            );
            logger.info("GOOOD tables initialisation.");
            return true;
        } catch (SQLException e) {
            logger.info("Error in tables initialisation.");
            e.printStackTrace();
            return false;
        }
    }

    public int getUserId(String userName) {
        int userId = -1;
        try {
            PreparedStatement s = connection
                    .prepareStatement("select ID from USERS where (USERNAME =?)");
            s.setString(1, userName);
            ResultSet resultSet = s.executeQuery();
            if (resultSet.next()) userId = resultSet.getInt("ID");
        } catch (SQLException ignore) {
        }
        return userId;
    }

    public boolean checkUser(Command command) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from USERS where USERNAME = ? and PASSWORD = ?"
            );
            statement.setString(1, command.getLogin());
            statement.setBytes(2, command.getPassword().getBytes());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.info(e.toString());
            return false;
        }
    }

    public List localCollection() {
        List collection = new ArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select movies.id," +
                            "       name," +
                            "       x," +
                            "       y," +
                            "       creation_date," +
                            "       oscars_count," +
                            "       movie_genre_name," +
                            "       mpaa_rating_name," +
                            "       director_name," +
                            "       director_passport_id," +
                            "       color_name," +
                            "       country_name," +
                            "       director_location_x," +
                            "       director_location_y," +
                            "       director_location_z," +
                            "       username " +
                            "from movies" +
                            "         join movie_genres mg on movies.movie_genre_id = mg.id" +
                            "         join mpaa_ratings mr on movies.mpaa_rating_id = mr.id" +
                            "         join colors c on movies.director_hair_color_id = c.id" +
                            "         join country c2 on movies.director_country_id = c2.id" +
                            "         join users u on movies.user_id = u.id"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Person director = new Person(resultSet.getString("director_name"),
                        resultSet.getString("director_passport_id"),
                        Color.valueOf(resultSet.getString("color_name")),
                        Country.valueOf(resultSet.getString("country_name")),
                        new Location(resultSet.getFloat("director_location_x"),
                                resultSet.getInt("director_location_y"),
                                resultSet.getFloat("director_location_z"))
                );
                Movie movie = new Movie(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        new Coordinates(resultSet.getLong("x"), resultSet.getLong("y")),
                        resultSet.getTimestamp("creation_date").toLocalDateTime(),
                        resultSet.getLong("oscars_count"),
                        MovieGenre.valueOf(resultSet.getString("movie_genre_name")),
                        MpaaRating.valueOf(resultSet.getString("mpaa_rating_name")),
                        director);
                movie.setUsername(resultSet.getString("username"));
                collection.add(movie);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return collection;
    }

    public Connection getConnection() {
        return connection;
    }

}
