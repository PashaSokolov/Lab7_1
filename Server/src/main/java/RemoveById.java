import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveById implements BigCommand {

    @Override

    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        if (command.getArgument() == null) {
            return "Invalid argument!";
        }
        int userId = sqlController.getUserId(command.getLogin());

        int startSize = movieList.size();
        if (movieList.size() > 0) {
            Long id = Long.parseLong((String) command.getArgument());
            try {
                PreparedStatement statement = sqlController.getConnection().prepareStatement(
                        "delete from movies where user_id = ? and id = ? returning movies.id"
                );
                statement.setInt(1, userId);
                statement.setLong(2, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    List movies = movieList.stream().filter(product -> product.getId() == id)
                            .collect(Collectors.toCollection(ArrayList<Movie>::new));
                    movieList.removeAll(movies);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "SQLException ;(";
            }

            if (startSize == movieList.size()) {
                return "The item with id " + id + " does not exist or it’s not yours";
            }
            return "Movie with id " + id + " was successfully removed";
        } else {
            return "Nothing was removed";
        }
    }
}
