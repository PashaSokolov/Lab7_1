import java.util.List;

public class FilterContains implements BigCommand {


    @Override
    public String execute(List<Movie> movieList, Command command, SQLController sqlController) {
        String msg = "";
        if (command.getArgument()==null){
            return "Filter is null!";
        }
        String subName = (String) command.getArgument();
        if (subName.isEmpty()) {
            return "String is Empty";
        }
        boolean contains = false;
        if (!movieList.isEmpty()) {
            for (Movie m : movieList) {
                if (m.getName().contains(subName)) {
                    contains = true;
                    msg = msg + (m.toString()) + "\n----//----\n";
                }
            }
            if (!contains) {
                msg = ("Nothing was found");
            } else {
                msg = msg.substring(0, msg.length() - 1);
            }
        } else {
            msg = ("Collection is empty");
        }
        return msg;
    }
}