import java.util.List;

public class PrintAscending implements Comand {
    private String msg;

    @Override
    public String execute(List<Movie> movieList, SQLController sqlController) {
        msg = "";
        if (!movieList.isEmpty()) {
            for (Movie m : movieList) {
                if (m.getMpaaRating() == MpaaRating.G) {
                    msg = msg + m.toString() + "\n----//----\n";
                }
            }
            for (Movie m : movieList) {
                if (m.getMpaaRating() == MpaaRating.PG) {
                    msg = msg + m.toString() + "\n----//----\n";
                }
            }
            for (Movie m : movieList) {
                if (m.getMpaaRating() == MpaaRating.PG_13) {
                    msg = msg + m.toString() + "\n----//----\n";
                }
            }
            for (Movie m : movieList) {
                if (m.getMpaaRating() == MpaaRating.R) {
                    msg = msg + m.toString() + "\n----//----\n";
                }
            }
            for (Movie m : movieList) {
                if (m.getMpaaRating() == MpaaRating.NC_17) {
                    msg = msg + m.toString() + "\n----//----\n";
                }
            }
        } else {
            msg = ("Collection is empty");
        }
        return msg;
    }
}
