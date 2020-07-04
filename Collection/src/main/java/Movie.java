import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Movie implements Comparable<Movie>, Serializable {
    private String username;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long oscarsCount; //Значение поля должно быть больше 0
    private MovieGenre genre; //Поле не может быть null
    private MpaaRating mpaaRating;
    private Person director;

    public Movie() {
        this.setId();
        this.setCreationDate();
    }

    public Movie(long id, String name, Coordinates coordinates, LocalDateTime creationDate, long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person director) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.director = director;
    }

    public void Movie(String name, Coordinates coordinates, long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person director) {
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.director = director;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getOscarcount() {
        return oscarsCount;
    }

    public MovieGenre getMoviegenre() {
        return genre;
    }

    public void setMoviegenre(MovieGenre genre) {
        this.genre = genre;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public void setCreationDate() {
        this.creationDate = LocalDateTime.now();
    }

    public void setOscarsCount(long oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public void setId() {
        id = 0;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(Movie movie) {
        int diff = 0;

        diff += String.valueOf(this.getName()).compareTo(String.valueOf(movie.getName()));

        return diff;
    }

    /**
     * @return поток вывода об элементе класса Movie
     */
    public String toString() {
        return "Movie ID: " + this.getId() + "\n"
                + "Owner's username: " + this.username + "\n"
                + "Movie name: " + this.getName() + "\n"
                + "Coordinates: \n" + "\t x: " + this.getCoordinates().getX()
                + "\n \t y: " + this.getCoordinates().getY() + "\n"
                + "Oscars count: " + this.getOscarcount() + "\n"
                + "Movie genre: " + this.getMoviegenre() + "\n"
                + "Mpaa Raiting: " + this.mpaaRating + "\n"
                + "Director: " + Person.valueOf(this.director) + "\n"
                + "Director's hair color: " + this.getDirector().getHairColor().toString() + "\n"
                + "Location: \n" + "\t x: " + this.getDirector().getLocation().getX()
                + "\n \t y: " + this.getDirector().getLocation().getY()
                + "\n \t z: " + this.getDirector().getLocation().getZ() + "\n"
                + "Creation Date: " + this.getCreationDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id &&
                oscarsCount == movie.oscarsCount &&
                Objects.equals(username, movie.username) &&
                Objects.equals(name, movie.name) &&
                genre == movie.genre &&
                mpaaRating == movie.mpaaRating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id, name, coordinates, creationDate, oscarsCount, genre, mpaaRating, director);
    }
}



