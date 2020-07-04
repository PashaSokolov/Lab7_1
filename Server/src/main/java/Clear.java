import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * реализация команды clear
 */

public class Clear implements BigCommand {

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        if (!movieList.isEmpty()) {
            int userId = sqlController.getUserId(command.getLogin());
            try {
                PreparedStatement statement = sqlController.getConnection().prepareStatement(
                        "delete from movies where user_id = ? returning movies.id"
                );
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                ArrayList<Long> ids = new ArrayList<>();
                while (resultSet.next())
                    ids.add(resultSet.getLong("id"));
                if (!ids.isEmpty()) {
                    movieList.removeAll((movieList.stream().filter(movie -> ids.indexOf(movie.getId()) != -1)
                            .collect(Collectors.toCollection(ArrayList::new))));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "SQLException ;(";
            }
            return "Your items have been deleted";
        } else {
            return "Collection is empty";
        }
    }
}