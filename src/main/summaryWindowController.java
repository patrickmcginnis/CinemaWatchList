package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class summaryWindowController implements Initializable {

    CinemaWatchlistController cinemaWatchlistController = new CinemaWatchlistController();

    @FXML
    private TextField totalFilms;
    @FXML
    private TextField totalWatched;
    @FXML
    private TextField totalUnwatched;
    @FXML
    private TextField latestEntry;
    @FXML
    private Label highestRatingLabel;
    @FXML
    private ComboBox highestRating;
    @FXML
    private Label lowestRatingLabel;
    @FXML
    private ComboBox lowestRating;

    /**
     * initializes all summary data
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        totalFilms.setText(Integer.toString(cinemaWatchlistController.watchList.getSize()));
        totalWatched.setText(Integer.toString(cinemaWatchlistController.watchList.getWatched().size()));
        totalUnwatched.setText(Integer.toString(cinemaWatchlistController.watchList.getUnwatched().size()));

        int latestEntryIndex = cinemaWatchlistController.watchList.getSize() - 1;
        latestEntry.setText(cinemaWatchlistController.watchList.getFilm(latestEntryIndex).getTitle() +
                " (" + cinemaWatchlistController.watchList.getFilm(latestEntryIndex).getYear() + ")");

        ObservableList<String> highestRatingsOv = FXCollections.observableArrayList(getHighestRatings());
        highestRating.getItems().addAll(highestRatingsOv);

        ObservableList<String> lowestRatingsOv = FXCollections.observableArrayList(getLowestRatings());
        lowestRating.getItems().addAll(lowestRatingsOv);
    }

    /**
     * gets String ArrayList of all the highest rated films
     * also sets highestRatingLabel Label to display what the highest rating double value is
     *
     * @return
     */
    public ArrayList<String> getHighestRatings() {
        ArrayList<String> highestRatings = new ArrayList<>();
        double highestRatingNum = 0.0;

        for (int k = 0; k < cinemaWatchlistController.watchList.getSize(); k++) {
            if (cinemaWatchlistController.watchList.getCollection().get(k).getRating() > highestRatingNum) {
                highestRatingNum = cinemaWatchlistController.watchList.getCollection().get(k).getRating();
            }
        }

        highestRatingLabel.setText("Highest Rating(s): " + highestRatingNum);

        for (int k = 0; k < cinemaWatchlistController.watchList.getSize(); k++) {
            if (cinemaWatchlistController.watchList.getCollection().get(k).getRating() == highestRatingNum) {
                highestRatings.add(cinemaWatchlistController.watchList.getCollection().get(k).getTitle() +
                        " (" + cinemaWatchlistController.watchList.getCollection().get(k).getYear() + ")");
            }
        }
        Collections.sort(highestRatings);
        return highestRatings;
    }

    /**
     * gets String ArrayList of all the lowest rated films watched
     * also sets lowestRatingLabel Label to display what the highest rating double value is
     *
     * @return
     */
    public ArrayList<String> getLowestRatings() {
        ArrayList<String> lowestRatings = new ArrayList<>();
        double lowestRatingsNum = 10.0;

        for (int k = 0; k < cinemaWatchlistController.watchList.getSize(); k++) {
            if (cinemaWatchlistController.watchList.getCollection().get(k).getRating() < lowestRatingsNum
                    && cinemaWatchlistController.watchList.getCollection().get(k).isHasWatched()) {
                lowestRatingsNum = cinemaWatchlistController.watchList.getCollection().get(k).getRating();
            }
        }

        lowestRatingLabel.setText("Lowest Rating(s): " + lowestRatingsNum);

        for (int k = 0; k < cinemaWatchlistController.watchList.getSize(); k++) {
            if (cinemaWatchlistController.watchList.getCollection().get(k).getRating() == lowestRatingsNum
                    && cinemaWatchlistController.watchList.getCollection().get(k).isHasWatched()) {
                lowestRatings.add(cinemaWatchlistController.watchList.getCollection().get(k).getTitle() +
                        " (" + cinemaWatchlistController.watchList.getCollection().get(k).getYear() + ")");
            }
        }
        Collections.sort(lowestRatings);
        return lowestRatings;
    }

    /**
     * event handler for closing Add Film window.
     *
     * @param event Button click on closing window button (X).
     */
    @FXML
    private void closeButtonAction(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        cinemaWatchlistController.setAltWindowOpen();
    }

    /**
     * event handler for Edit Film button.
     *
     * @param event Button click event on Edit Film button.
     */
    @FXML
    private void minimizeButtonAction(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }
}
