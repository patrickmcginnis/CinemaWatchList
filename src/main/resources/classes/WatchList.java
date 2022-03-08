package main.resources.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WatchList {


    private ArrayList<Film> collection;

    /**
     * WatchList constructor
     */
    public WatchList() {
        collection = new ArrayList<Film>();
    }

    /**
     * adds a single Film object to the WatchList
     *
     * @param film Film object to be added
     */
    public void add(Film film) {
        collection.add(film);
    }

    /**
     * adds an array of Film objects to the Watchlist
     *
     * @param films Film object array to be added
     */
    public void add(Film[] films) {
        collection.addAll(Arrays.asList(films));
    }

    /**
     * adds an array list of Film objects to the WatchList
     *
     * @param collection Film object array list to be added
     */
    public void add(ArrayList<Film> collection) {
        this.collection.addAll(collection);
    }

    /**
     * adds Films to the WatchList reading from an external file. Fields MUST be separated by '|'
     *
     * @param file
     */
    public void add(File file) {
        try (Scanner keysIn = new Scanner(file)) {
            Film.resetNumberOfFilms();
            while (keysIn.hasNext()) {
                String record = keysIn.nextLine();
                if (record.charAt(0) == '"') {
                    record = record.substring(1, record.length() - 1);
                }
                String[] fields = record.split("\\|");
                Film films = new Film((fields[1]), Integer.parseInt(fields[2]), fields[3], fields[4],
                        Boolean.parseBoolean(fields[5]), Double.parseDouble(fields[6]));
                collection.add(films);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR NOT FOUND");
        }

    }

    /**
     * prints the contents of the WatchList to a file
     *
     * @param file File to be printed to
     */
    public void writeToFile(File file) {
        int id = 0;
        try (PrintWriter output = new PrintWriter(file)) {
            for (Film film : collection) {
                output.println(id++ + "" + '|' + film.getTitle() + '|' + film.getYear() + '|' +
                        film.getDirector() + '|' + film.getGenre() + '|' +
                        film.isHasWatched() + '|' + film.getRating());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR NOT FOUND");
        }
    }

    /**
     * sets the Film object of a specific index in the WatchList to another Film (edit)
     *
     * @param index index of Film to edit
     * @param film  new Film to change previous Film to
     */
    public void set(int index, Film film) {
        collection.set(index, film);
    }

    /**
     * remove Film object from WatchList
     *
     * @param index index of the Film object to remove
     */
    public void remove(int index) {
        collection.remove(index);
    }

    /**
     * clears all Film objects from WatchList
     */
    public void clearWatchlist() {
        collection.clear();
    }

    /**
     * returns Film object in WatchList at index
     *
     * @param index index of Film to return
     * @return
     */
    public Film getFilm(int index) {
        return collection.get(index);
    }

    /**
     * returns an ArrayList of Films contains a specific String
     *
     * @param string value of String to search for and return ArrayList of Films containing
     * @return ArrayList of Film objects
     */
    public ArrayList<Film> get(String string) {
        string.toLowerCase();
        ArrayList<Film> searchedList = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getTitle().toLowerCase().contains(string) ||
                    collection.get(i).getDirector().toLowerCase().contains(string) ||
                    collection.get(i).getGenre().toLowerCase().contains(string) ||
                    Integer.toString(collection.get(i).getYear()).contains(string)) {


                searchedList.add(collection.get(i));
            }
        }
        return searchedList;
    }

    /**
     * returns ArrayList of Films in WatchList where hasWatched == true
     *
     * @return ArrayList of Film objects
     */
    public ArrayList<Film> getWatched() {
        ArrayList<Film> searchedList = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).isHasWatched()) {
                searchedList.add(collection.get(i));
            }
        }
        return searchedList;
    }

    /**
     * returns ArrayList of Films in WatchList where hasWatched == false
     *
     * @return ArrayList of Film objects
     */
    public ArrayList<Film> getUnwatched() {
        ArrayList<Film> searchedList = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            if (!collection.get(i).isHasWatched()) {
                searchedList.add(collection.get(i));
            }
        }
        return searchedList;
    }

    /**
     * returns ArrayList of all Films in WatchList
     *
     * @return ArrayList of Film objects
     */
    public ArrayList<Film> getCollection() {
        return this.collection;
    }

    /**
     * returns the number of Film objects in WatchList
     *
     * @return
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * toString method for WatchList
     *
     * @return returns String representation of WatchList object
     */
    @Override
    public String toString() {
        String output = "";
        if (collection.isEmpty()) {
            output = "No movies.";
        } else {
            for (Film film : collection) {
                output += film.toString() + "\n------------------------------------------------------------\n";
            }
        }
        return output;
    }
}