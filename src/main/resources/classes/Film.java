package main.resources.classes;

import java.util.InputMismatchException;

public class Film {

    private static int numberOfFilms = 0;
    private int id;
    private String title;
    private int year;
    private String director;
    private String genre;
    private boolean hasWatched;
    private double rating;

    /**
     * Film object constructor
     *
     * @param title      Title of film
     * @param year       Year of release of film
     * @param director   Director(s) of film
     * @param genre      Genre(s) of film
     * @param hasWatched whether or not film has been watched
     * @param rating     rating of film
     * @throws IllegalArgumentException
     */
    public Film(String title, int year, String director, String genre, boolean hasWatched, double rating) throws IllegalArgumentException {
        id = numberOfFilms;
        setTitle(title);
        setYear(year);
        setDirector(director);
        setGenre(genre);
        this.hasWatched = hasWatched;
        setRating(rating);
        numberOfFilms++;
    }

    /**
     * id getter
     *
     * @return returns int value of Film id
     */
    public int getId() {
        return id;
    }

    /**
     * id setter
     *
     * @param id sets Film id to value
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * tile getter
     *
     * @return returns String value of Film title
     */
    public String getTitle() {
        return title;
    }

    /**
     * title setter
     *
     * @param title sets Film title to value
     * @throws IllegalArgumentException throws in case of empty title, null or title containing '|'
     */
    public void setTitle(String title) throws IllegalArgumentException {
        if (title.isEmpty() || title == null || title.contains("|")) {
            throw new IllegalArgumentException("Title must not be empty or contain the characacter '|'.");
        } else {
            this.title = title;
        }

    }

    /**
     * year getter
     *
     * @return returns int value of Film year
     */
    public int getYear() {
        return year;
    }

    /**
     * year setter
     *
     * @param year sets Film year to value
     * @throws IllegalArgumentException throws in case of year less than 1900
     */
    public void setYear(int year) throws IllegalArgumentException {
        if (year < 1900) {
            throw new IllegalArgumentException("Year must be greater then 1900.");
        } else {
            this.year = year;
        }
    }

    /**
     * director getter
     *
     * @return return String value of Film director
     */
    public String getDirector() {
        return director;
    }

    /**
     * director setter
     *
     * @param director set Film director to value
     * @throws IllegalArgumentException throws in case of empty director, null or title containing '|'
     */
    public void setDirector(String director) throws IllegalArgumentException {
        if (director.isEmpty() || director == null || director.contains("|")) {
            throw new IllegalArgumentException("Director must not be empty or contain the characacter '|'.");
        } else {
            this.director = director;
        }
    }

    /**
     * genre getter
     *
     * @return return String value of Film genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * genre setter
     *
     * @param genre set Film genre to value
     * @throws IllegalArgumentException throws in case of empty genre, null or title containing '|'
     */
    public void setGenre(String genre) throws IllegalArgumentException {
        if (genre.isEmpty() || genre == null || genre.contains("|")) {
            throw new IllegalArgumentException("Genre must not be empty.");
        } else {
            this.genre = genre;
        }
    }

    /**
     * hasWatched getter
     *
     * @return returns boolean value of Film hasWatched
     */
    public boolean isHasWatched() {
        return hasWatched;
    }

    /**
     * hasWatched setter
     *
     * @param hasWatched set Film hasWatched to value
     */
    public void setHasWatched(boolean hasWatched) {
        this.hasWatched = hasWatched;
    }

    /**
     * rating getter
     *
     * @return return double value of Film rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * rating setter
     *
     * @param rating set Film rating to value
     * @throws InputMismatchException throws in case of rating less than 0.0 or greater than 10.0
     */
    public void setRating(double rating) throws InputMismatchException {
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        } else {
            rating = Double.parseDouble(String.format("%.1f", rating));
            this.rating = rating;
        }
    }

    /**
     * sets static value numberOfFilms to 0. Useful when deleting records and re-adding data into watchList after
     * clearing all previous films
     */
    public static void resetNumberOfFilms() {
        numberOfFilms = 0;
    }

    /**
     * compareTo method comparing title, director and year
     *
     * @param obj object to compare with
     * @return returns 1 if the Film is the same, 0 if the Film is different and -1 if the obj comparing is not a Film
     */
    public int compareTo(Object obj) {
        if (obj instanceof Film) {
            if ((this.getTitle().equalsIgnoreCase(((Film) obj).getTitle().trim()))
                    && (this.getDirector().equalsIgnoreCase(((Film) obj).getDirector().trim()))
                    && (this.getYear() == ((Film) obj).getYear())) {
                return 1;
            } else
                return 0;
        } else
            return -1;
    }

    /**
     * toString method
     *
     * @return returns String representation of Film object
     */
    @Override
    public String toString() {
        String viewed = isHasWatched() ? "Yes" : "No";
        return String.format("ID: %d\nTitle:  %s  |  Director: %s\nYear:   %d  |   Genre: %s\nViewed: %s   |   " +
                "Rating: %.1f/10", getId(), getTitle(), getDirector(), getYear(), getGenre(), viewed, getRating());
    }

}


