package entities;

public enum QuizThemes {


    ANIMALS("Animals.json"),
    BAKERY("Bakery.json"),
    CULTURE("General Culture.json"),
    OCEAN("meditarranee.json"),
    MUSIC("Music.json"),
    COUNTRIES("worldCountries.json");

    private final String value;

    QuizThemes(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
