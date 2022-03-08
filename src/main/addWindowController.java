package main;

import main.resources.classes.Film;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class addWindowController implements Initializable {
    CinemaWatchlistController cinemaWatchlistController;

    /**
     * initializes variable with value of parent controller CinemaWatchList, called upon opening of Add Window.
     *
     * @param cinemaWatchlistController
     */
    public void setParentController(CinemaWatchlistController cinemaWatchlistController) {
        this.cinemaWatchlistController = cinemaWatchlistController;
    }

    @FXML
    private TextField Title;
    @FXML
    private TextField Year;
    @FXML
    private TextField Genre;
    @FXML
    private TextField Director;
    @FXML
    private CheckBox hasWatched;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label ratingLabel;

    /**
     * sets up live updating of ratingLabel based on rating slider.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ratingLabel.setText(String.format("%s: %.1f", "Rating", newValue.doubleValue()));
        });
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
     * event handler for minimizing Add Film window.
     *
     * @param event Button click on minimize window button (-).
     */
    @FXML
    private void minimizeButtonAction(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).setIconified(true);
    }

    /**
     * event handler for Add Film button.
     *
     * @param event Button click event on Add Film button.
     */
    @FXML
    private void addFilmButtonAction(ActionEvent event) {
        try {
            String title = Title.getText();
            int year = Integer.parseInt(Year.getText());
            String genre = Genre.getText();
            String director = Director.getText();
            boolean watched = hasWatched.isSelected();
            double rating = ratingSlider.getValue();
            Film film = new Film(title, year, director, genre, watched, rating);

            // below loop uses the comparesTo method to check if a duplicate film exists then throws exception if so
            for (int i = 0; i < cinemaWatchlistController.watchList.getSize(); i++) {
                if (cinemaWatchlistController.watchList.getFilm(i).compareTo(film) == 1) {
                    throw new InputMismatchException();
                }
            }

            film.setId(cinemaWatchlistController.watchList.getSize());

            cinemaWatchlistController.watchList.add(film);
            cinemaWatchlistController.watchList.writeToFile(new File("src/main/resources/data/mainWatchlist.csv"));

            refreshTableView();
            cinemaWatchlistController.setAltWindowOpen();   // sets altWindowOpen back to false, allowing another window
            // to be opened
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();  // closes window

        } catch (NumberFormatException ex3) {
            // in case user enters anything other than an integer as the year
            Year.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Year.setText("");
            Year.setPromptText("INVALID");
            invalidUserInput();
        } catch (IllegalArgumentException ex1) {
            invalidUserInput();
        } catch (InputMismatchException ex2) {
            duplicateFilmDetected();
        } catch (Exception ex2) {
            System.out.println("ERROR");
        }

    }

    /**
     * method called if user input is invalid (a field is empty or user includes '|' chararacter.
     */
    public void invalidUserInput() {
        if (Title.getText().isEmpty()) {
            Title.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Title.setPromptText("REQUIRED");
        } else if (Title.getText().contains("|")) {
            Title.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Title.setText("");
            Title.setPromptText("INVALID CHARACTER ' | '");
        }

        if (Genre.getText().isEmpty()) {
            Genre.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Genre.setPromptText("REQUIRED");
        } else if (Genre.getText().contains("|")) {
            Genre.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Genre.setText("");
            Genre.setPromptText("INVALID CHARACTER ' | '");
        }

        if (Director.getText().isEmpty()) {
            Director.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Director.setPromptText("REQUIRED");
        } else if (Director.getText().contains("|")) {
            Director.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Director.setText("");
            Director.setPromptText("INVALID CHARACTER ' | '");
        }

        if (Year.getText().isEmpty() || Integer.parseInt(Year.getText()) < 1900
                || Integer.parseInt(Year.getText()) > 3000) {
            Year.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            Year.setText("");
            Year.setPromptText("INVALID YEAR");
        }
    }

    /**
     * method called user attempts to add a Film that already exists.
     */
    public void duplicateFilmDetected() {
        Title.clear();
        Director.clear();
        Genre.clear();
        Year.clear();
        hasWatched.setSelected(false);
        ratingSlider.setValue(0.0);
        Title.setPromptText("FILM ALREADY EXISTS");
        Title.setStyle("-fx-text-box-border: yellow ;");
        Year.setStyle("-fx-text-box-border: yellow ;");
        Genre.setStyle("-fx-text-box-border: yellow ;");
        Director.setStyle("-fx-text-box-border: yellow ;");
    }

    /**
     * Refreshes tableview upon making any changes to watchList.
     * This is accomplished by calling one of the library, watched, or unwatched button actions in the main controller,
     * which one it calls depends on whichever button was last clicked (which is the current view).
     */
    private void refreshTableView() {
        switch (cinemaWatchlistController.currentView) {
            case 0:
                cinemaWatchlistController.libraryButtonAction();
                break;
            case 1:
                cinemaWatchlistController.watchedButtonAction();
                break;
            case 2:
                cinemaWatchlistController.unwatchedButtonAction();
        }
    }

    /**
     * method reverts each input field to default state upon click.
     * useful when exception is thrown upon attempting to add a film to an already existing film.
     * or if user input missing a field.
     *
     * @param actionEvent Mouse click event on user input field.
     */
    public void revertColor(MouseEvent actionEvent) {
        if (actionEvent.getSource().equals(Title)) {
            Title.setStyle("-fx-text-box-border-width: 0px;");
            Title.setPromptText("Enter title...");
        } else if (actionEvent.getSource().equals(Genre)) {
            Genre.setStyle("-fx-text-box-border-width: 0px;");
            Genre.setPromptText("Enter genres... (eg. Action, Horror)");
        } else if (actionEvent.getSource().equals(Director)) {
            Director.setStyle("-fx-text-box-border-width: 0px;");
            Director.setPromptText("Enter directors...");
        } else if (actionEvent.getSource().equals(Year)) {
            Year.setStyle("-fx-text-box-border-width: 0px;");
            Year.setPromptText("Enter year...");
        }

    }
}