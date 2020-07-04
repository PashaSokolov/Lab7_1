import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Registration implements BigCommand {

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        String msg = "";
        if (command.getLogin() == null || command.getPassword() == null)
            msg = "Invalid argument";
        else {
            if (sqlController.getUserId(command.getLogin()) != -1) {
                return ("User with name " + command.getLogin() + " is exist!");
            } else {
                try {
                    PreparedStatement statement = sqlController.getConnection().prepareStatement(
                            "insert into USERS (USERNAME,PASSWORD) values (?, ?) "
                    );
                    statement.setString(1, command.getLogin());
                    statement.setBytes(2, command.getPassword().getBytes());
                    statement.execute();
                    msg = "Good registration";
                } catch (SQLException e) {
                    msg = "Registration error!";
                }
            }

        }
        return msg;
    }
}
