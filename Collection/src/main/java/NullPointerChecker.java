import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class NullPointerChecker implements Checker {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private String newLine = "";

    @Override
    public void checkEverything(Movie movie, CheckParametr parametr) {
        if (parametr == CheckParametr.WITH_ASKING) {
            if (movie.getName() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the movie name is null. \n Enter the new name:");

                while (true) {
                    try {
                        newLine = in.readLine();

                        if (!newLine.isEmpty()) {
                            movie.setName(newLine);
                            break;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one: ");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with default value");
                        movie.setName("Movie" + movie.getId());
                        break;
                    } catch (Exception e) {
                        System.out.println("Пожалуйста примите исполнение");
                        System.exit(0);
                    }
                }
            }

            if (movie.getCoordinates() == null) {
                boolean passedValue = false;
                System.out.println("Looks like movie coordinates are null. \n Enter the new coordinates:");

                movie.setCoordinates(new Coordinates(0, 0));
                changeX(movie);
                changeY(movie);
            }

            if (Long.valueOf(movie.getCoordinates().getX()) == null) {
                System.out.println("Looks like the movie X-coordinate is null");
                changeX(movie);
            }

            if (Long.valueOf(movie.getCoordinates().getY()) == null) {
                System.out.println("Looks like the movie X-coordinate is null");
                changeY(movie);
            }

            if (movie.getOscarcount() <= 0) {

                boolean passedValue = false;
                System.out.println("Looks like the movie oscarCount is null. \n Enter the new oscarCount");

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
                        System.out.println("This is not a numberO");
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
            if (movie.getDirector() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the movie director is null. \n Enter the new name:");

                while (!passedValue) {
                    try {
                        try {
                            movie.setDirector(new Person(in.readLine()));
                            passedValue = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Illegal Argument. Try another one:\n");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with default value");
                        movie.setName("Movie" + movie.getId());
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("Пожалуйста примите исполнениеД");
                        System.exit(0);
                    }
                }
            }
            if (movie.getDirector().getName() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the director name is null. \n Enter the new name:");

                while (!passedValue) {
                    try {
                        if ((newLine = in.readLine()) != "") {
                            movie.getDirector().setName(newLine);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one: ");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with default value");
                        movie.getDirector().setName("Director" + movie.getId());
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("Пожалуйста примите исполнение");
                        System.exit(0);
                    }
                }
            }
            if (movie.getDirector().getCountry() == null) {
                while (true) {
                    System.out.println("\n" + "Looks like director country is null");
                    System.out.println("Try this:" + "\n"
                            + "UNITED_KINGDOM" + "\n"
                            + "ITALY" + "\n"
                            + "NORTH_KOREA" + "\n");
                    try {
                        newLine = in.readLine();
                        if (newLine == null) return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (newLine.contains("UNITED_KINGDOM") || newLine.contains("ITALY") || newLine.contains("NORTH_KOREA")) {
                        movie.getDirector().setCountry(Country.valueOf(newLine));
                        break;
                    }
                }
            }
            if (movie.getDirector().getHairColor() == null) {
                while (true) {
                    System.out.println("\n" + "Looks like director hair color is null");
                    System.out.println("Try this:" + "\n");
                    for (Color color : Color.values()) {
                        System.out.println(color);
                    }
                    try {
                        newLine = in.readLine();
                        if (newLine == null) return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        movie.getDirector().setHairColor(Color.valueOf(newLine));
                        break;
                    } catch (IllegalArgumentException ignore) {
                    }
                }
            }
            if (movie.getDirector().getPassportID() == null) {
                boolean passedValue = false;
                System.out.println("Looks like the director passport id is null. \n Enter the new name:");

                while (!passedValue) {
                    try {
                        if ((newLine = in.readLine()) != "") {
                            movie.getDirector().setPassportID(newLine);
                            passedValue = true;
                        } else {
                            System.out.println("Entered value is inappropriate. Try another one: ");
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("End of input. Field will be replaced with default value");
                        movie.getDirector().setPassportID("GEN_ID_" + (int) java.lang.Math.random() * 10000);
                        passedValue = true;
                    } catch (Exception e) {
                        System.out.println("Пожалуйста примите исполнение");
                        System.exit(0);
                    }
                }
            }
            if (movie.getDirector().getLocation() == null) {
                boolean passedValue = false;
                System.out.println("Looks like movie coordinates are null. \n Enter the new coordinates:");

                movie.getDirector().setLocation(new Location(0, 0, 0f));
                changeLocationX(movie);
                changeLocationY(movie);
                changeLocationZ(movie);
            }
            if (movie.getMoviegenre() == null) {
                while (true) {
                    System.out.println("\n" + "Переменная moviegenre - Null, либо не содержится в ENUM");
                    System.out.println("Введите одну из переменных:" + "\n"
                            + "ACTION" + "\n"
                            + "DRAMA" + "\n"
                            + "MUSICAL" + "\n"
                            + "THRILLER" + "\n"
                            + "FANTASY" + "\n");
                    try {
                        newLine = in.readLine();
                        if (newLine == null) return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (newLine.contains("ACTION") || newLine.contains("DRAMA") || newLine.contains("MUSICAL")
                            || newLine.contains("THRILLER") || newLine.contains("FANTASY")) {
                        movie.setMoviegenre(MovieGenre.valueOf(newLine));
                        break;
                    }
                }
            }
            if (movie.getMpaaRating() == null) {
                while (true) {
                    System.out.println("\n" + "Переменная mpaarating - Null, либо не содержится в ENUM");
                    System.out.println("Введите одну из переменных:" + "\n"
                            + "G" + "\n"
                            + "PG" + "\n"
                            + "PG_13" + "\n"
                            + "R" + "\n"
                            + "NC_17" + "\n");
                    try {
                        newLine = in.readLine();
                        if (newLine == null) return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (newLine.contains("G") || newLine.contains("PG") || newLine.contains("PG_13")
                            || newLine.contains("R") || newLine.contains("NC_17")) {
                        movie.setMpaaRating(MpaaRating.valueOf(newLine));
                        break;
                    }
                }
            }
        } else {
            this.checkEverything(movie);
        }
        System.out.println("Your object:");
        System.out.println(movie);
        System.out.println("Username and id will be set when adding to the database");
    }

    @Override
    public void checkEverything(Movie movie) {
        if (movie.getName() == null) {
            System.out.println("Name - field is null, it will be replaced with default value");
            movie.setName("id" + movie.getId());
        }
        if (movie.getCoordinates() == null) {
            System.out.println("Coordinates - field is null, it will be replaced with default value");
            movie.setCoordinates(new Coordinates(100L, 100L));
        }
        if (Long.valueOf(movie.getCoordinates().getX()) == null) {
            System.out.println("XCoordinate - field is null, it will be replaced with default value");
            movie.getCoordinates().setX(100L);
        }
        if (Long.valueOf(movie.getCoordinates().getY()) == null) {
            System.out.println("YCoordinate - field is null, it will be replaced with default value");
            movie.getCoordinates().setY(100L);
        }
        if (Long.valueOf(movie.getOscarcount()) == null) {
            System.out.println("OscarCount - field is null, it will be replaced with default value");
            movie.setOscarsCount(1);
        }
        if (movie.getMoviegenre() == null) {
            System.out.println("MovieGenre - field is null, it will be replaced with default value");
            movie.setMoviegenre(MovieGenre.ACTION);
        }
        if (movie.getMpaaRating() == null) {
            System.out.println("MovieGenre - field is null, it will be replaced with default value");
            movie.setMpaaRating(MpaaRating.PG);
        }
        if (movie.getDirector() == null) {
            System.out.println("Director - field is null, it will be replaced with default value");
            movie.setDirector(new Person("Roma", "55", Color.RED, Country.ITALY, new Location(5.0f, 5, 6.0F)));
        }
        if (movie.getDirector().getName() == null) {
            System.out.println("Director name - field is null, it will be replaced with default value");
            movie.getDirector().setName("Movie" + movie.getId());
        }
        if (movie.getDirector().getPassportID() == null) {
            System.out.println("Director passportID - field is null, it will be replaced with default value");
            movie.getDirector().setPassportID(String.valueOf(Math.random() * 100));
        }
        if (movie.getDirector().getCountry() == null) {
            System.out.println("Director country - field is null, it will be replaced with default value");
            movie.getDirector().setCountry(Country.UNITED_KINGDOM);
        }
        if (movie.getDirector().getHairColor() == null) {
            System.out.println("Director hairColor - field is null, it will be replaced with default value");
            movie.getDirector().setHairColor(Color.GREEN);
        }
        if (movie.getDirector().getLocation() == null) {
            System.out.println("Director Location - field is null, it will be replaced with default value");
            movie.getDirector().setLocation(new Location(5.0f, 5, 6.0F));
        }
    }

    private void changeX(Movie movie) {
        boolean passedValue = false;
        System.out.println("Enter new X-coordinate:");

        while (true) {
            if (newLine == null) System.exit(0);
            try {
                Long tempX = Long.parseLong(newLine = in.readLine());

                if (tempX > 0) {
                    movie.getCoordinates().setX(tempX);
                    break;
                } else {
                    System.out.println("Entered value is inappropriated. Try another one: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("It is not a numberX");
            } catch (NoSuchElementException e) {
                System.out.println("Field will be replaced with random value");
                movie.getCoordinates().setX((long) (Math.random() * 100));
                break;
            } catch (Exception e) {
                System.out.println("Пожалуйста примите исполнение");
                System.exit(0);
            }
        }
    }

    private void changeY(Movie movie) {
        System.out.println("Enter new Y-coordinate:");
        while (true) {
            if (newLine == null) System.exit(0);
            try {
                Long tempY = Long.parseLong(newLine = in.readLine());

                if (tempY > 0) {
                    movie.getCoordinates().setY(tempY);
                    break;
                } else {
                    System.out.println("Entered value is inappropriated. Try another one: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("It is not a numberY");
            } catch (NoSuchElementException e) {
                System.out.println("Field will be replaced with random value");
                movie.getCoordinates().setY((long) (Math.random() * 100));
                break;
            } catch (Exception e) {
                System.out.println("Пожалуйста примите исполнение");
                System.exit(0);
            }
        }
    }

    private void changeLocationX(Movie movie) {
        System.out.println("Enter new X-location-coordinate (float):");
        while (true) {
            if (newLine == null) System.exit(0);
            try {
                float tempX = Float.parseFloat(newLine = in.readLine());
                movie.getDirector().getLocation().setX(tempX);
                break;

            } catch (NumberFormatException e) {
                System.out.println("It is not a number X");
            } catch (NoSuchElementException e) {
                System.out.println("Field will be replaced with random value");
                movie.getDirector().getLocation().setX((float) Math.random() * 100);
                break;
            } catch (Exception e) {
                System.out.println("Пожалуйста примите исполнение");
                System.exit(0);
            }
        }
    }

    private void changeLocationY(Movie movie) {
        System.out.println("Enter new Y-location-coordinate (int):");
        while (true) {
            if (newLine == null) System.exit(0);
            try {
                int tempY = Integer.parseInt(newLine = in.readLine());
                movie.getDirector().getLocation().setY(tempY);
                break;

            } catch (NumberFormatException e) {
                System.out.println("It is not a number Y");
            } catch (NoSuchElementException e) {
                System.out.println("Field will be replaced with random value");
                movie.getDirector().getLocation().setY((int) Math.random() * 100);
                break;
            } catch (Exception e) {
                System.out.println("Пожалуйста примите исполнение");
                System.exit(0);
            }
        }
    }

    private void changeLocationZ(Movie movie) {
        System.out.println("Enter new Z-location-coordinate (float):");
        while (true) {
            if (newLine == null) System.exit(0);
            try {
                float tempZ = Float.parseFloat(newLine = in.readLine());
                movie.getDirector().getLocation().setZ(tempZ);
                break;
            } catch (NumberFormatException e) {
                System.out.println("It is not a number Z");
            } catch (NoSuchElementException e) {
                System.out.println("Field will be replaced with random value");
                movie.getDirector().getLocation().setZ((float) Math.random() * 100);
                break;
            } catch (Exception e) {
                System.out.println("Пожалуйста примите исполнение");
                System.exit(0);
            }
        }
    }
}
