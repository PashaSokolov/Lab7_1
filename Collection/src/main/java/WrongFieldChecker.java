import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


public class WrongFieldChecker implements Checker {
    static Logger logger = Logger.getLogger("FieldCheckLogger");
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private String newLine = "";

    @Override
    public void checkEverything(Movie movie, CheckParametr parametr) {
        if (parametr == CheckParametr.WITHOUT_ASKING) {
            this.checkWithoutAsking(movie);
        } else {
            this.checkWithAsking(movie);
        }

    }

    @Override
    public void checkEverything(Movie movie) {
        checkEverything(movie, CheckParametr.WITHOUT_ASKING);
    }

    private void checkWithAsking(Movie movie) {
        if (movie.getName() == "") {
            boolean passedValue = false;
            System.out.println("Looks like movie name is empty. \n Enter a new name:");

            while (!passedValue) {
                try {
                    if ((newLine = in.readLine()) != "") {
                        movie.setName(newLine);
                        passedValue = true;
                    } else {
                        System.out.println("Entered value is inappropriate. Try another one:");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("End of input. Field will be replaced with default value");
                    movie.setName("Movie" + movie.getId());
                    passedValue = true;
                } catch (Exception e) {
                    System.out.println("пожалуйста примите выполнение");
                    System.exit(0);
                }
            }
        }
        if (movie.getOscarcount() < 0) {
            boolean passedValue = false;
            System.out.println("Looks like movie oscarCount is less than 0. \n Enter new counter:");

            while (!passedValue) {
                try {

                    Long tempOscar = Long.parseLong(newLine = in.readLine());

                    if (tempOscar >= 0) {
                        movie.setOscarsCount(tempOscar);
                        passedValue = true;
                    } else {
                        System.out.println("Entered value is inappropriate. Try another one");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("This is not a number");
                } catch (NoSuchElementException e) {
                    System.out.println("Field will be replaced with random value");
                    movie.setOscarsCount((long) (Math.random() * 10));
                    passedValue = true;
                } catch (Exception e) {
                    System.out.println("Пожалуйста примите исполнение");
                    System.exit(0);
                }
            }
        }
        if (movie.getCoordinates().getX() <= -210) {
            boolean passedValue = false;
            System.out.println("Enter new X-coordinate:");

            while (!passedValue) {
                try {
                    Long tempX = Long.parseLong(newLine = in.readLine());

                    if (tempX > 0) {
                        movie.getCoordinates().setX(tempX);
                    } else {
                        System.out.println("Entered value is inappropriated. Try another one: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("It is not a number");
                } catch (NoSuchElementException e) {
                    System.out.println("Field will be replaced with random value");
                    movie.getCoordinates().setX((long) (Math.random() * 100));
                    passedValue = true;
                } catch (Exception e) {
                    System.out.println("Пожалуйста примите исполнение");
                    System.exit(0);
                }
            }
        }
    }

    private void checkWithoutAsking(Movie movie) {
        boolean chandged = false;

        if (movie.getName() == "") {
            movie.setName("Movie" + movie.getId());
            if (!chandged) {
                logger.info("Name-field is wrong, it will be replaced with default value");
                chandged = true;
            }
        }
        if (movie.getOscarcount() < 0) {
            movie.setOscarsCount((long) (Math.random() * 10));
            if (!chandged) {
                logger.info("OscarCount-field is wrong, it will be replaced with default value");
                chandged = true;
            }
        }
        if (movie.getCoordinates().getX() <= -210) {
            movie.getCoordinates().setX(111L);
            if (!chandged) {
                logger.info("Coordinate-field is wrong, it will be replaced with default value");
                chandged = true;
            }
        }
    }
}
