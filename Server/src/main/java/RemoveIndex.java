import java.util.List;

public class RemoveIndex implements BigCommand {
    private final RemoveById removeById = new RemoveById();

    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        String msg = "";
        if (command.getArgument() == null) {
            return "Invalid argument";
        }
        int indexToRemove = Integer.valueOf((String) command.getArgument());
        if (Integer.valueOf(indexToRemove) != null) {
            try {
                int prevSize = movieList.size();

                if (!movieList.isEmpty()) {
                    if (movieList.get(indexToRemove) == null) {
                        msg = "Nothing was removed";
                    } else {
                        Command removeCommand = new Command("remove_by_id", String.valueOf(movieList.get(indexToRemove).getId()));
                        removeCommand.setLoginPassword(command.getLogin(),command.getPassword());
                        removeById.execute(movieList, removeCommand, sqlController);
                        if (prevSize != movieList.size()) {
                            msg = ("Movie" + indexToRemove + "was succesfully removed");
                        } else {
                            msg = "Nothing was removed";
                        }
                    }
                } else {
                    msg = "Collection is empty";
                }
            } catch (NumberFormatException e) {
                msg = ("ID should be a number from 0 to " + Long.MAX_VALUE);
            } catch (IndexOutOfBoundsException e) {
                msg = ("Max ID is: " + (movieList.size() - 1));
            }
        }
        return msg;
    }
}
