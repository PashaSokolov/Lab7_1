import java.util.List;

public class RemoveFirst implements BigCommand {
    private final RemoveById removeById = new RemoveById();

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        String msg = "";
        int prevSize = movieList.size();
        if (movieList.isEmpty()) {
            msg = "Collection is empty";
        } else {
            Command removeCommand = new Command("remove_by_id", String.valueOf(movieList.get(0).getId()));
            removeCommand.setLoginPassword(command.getLogin(),command.getPassword());
            removeById.execute(movieList, removeCommand, sqlController);
            if (prevSize != movieList.size()) {
                msg = ("First Movie was succesfully removed");
            } else {
                msg = "Nothing was removed";
            }
        }
        return msg;
    }
}
