import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле не может быть null
    private Location location; //Поле может быть null

    public Person(String name, String passportID, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public Person(String name) {
        this.name = name;
    }

    /**
     * @param person объект класса Person
     * @return имя человека, возвращает "null", если поле name человека равно null
     */
    public static String valueOf(Person person) {
        if (person == null) {
            return null;
        } else {
            return String.valueOf(person.getName());
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return this.nationality;
    }

    public void setCountry(Country country) {
        this.nationality = country;
    }

    /**
     * @param person объект класса Person
     * @return результат сравнения
     */
    @Override
    public int compareTo(Person person) {
        if (person == null) {
            return 0;
        } else {
            return String.valueOf(this.getName()).compareTo(String.valueOf(person.getName()));
        }
    }

    /**
     * @return имя человека в формате String, возвращает "null", если поле name человека равно null
     */
    public String toString() {
        return Person.valueOf(this);
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}