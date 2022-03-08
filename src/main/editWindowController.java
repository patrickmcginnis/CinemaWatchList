package main;

import main.resources.classes.Film;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.ResourceBundle;

public class editWindowController implements Initializable {
    CinemaWatchlistController cinemaWatchlistController;

    /**
     * initializes variable with value of parent controller CinemaWatchList, called upon opening of Edit Window
     *
     * @param cinemaWatchlistController
     */
    public void setParentController(CinemaWatchlistController cinemaWatchlistController) {
        this.cinemaWatchlistController = cinemaWatchlistController;
    }

    // variable for each user input field
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
    @FXML
    private Label title;

    /**
     * sets up the Edit Film window when it opens, setting all fields with the current values of the film being edited,
     * as well as the prompt text.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // sets rating slider label to update dynamically
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ratingLabel.setText(String.format("%s: %.1f", "Rating", newValue.doubleValue()));
        });

        String titleBarFilm = "Edit Film: " + cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle();
        // a film with a title too long will mess with the GUI layout, this will limit the size of the displayed title
        if (titleBarFilm.length() > 50) {
            titleBarFilm = titleBarFilm.substring(0, 50) + "...";
        }

        title.setText(titleBarFilm);

        // sets the text and prompt text and initial values of all input fields based on the film to be edited
        Title.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle());
        Title.setText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle());
        Title.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle());
        Year.setText(Integer.toString(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getYear()));
        Year.setPromptText(Integer.toString(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getYear()));
        Genre.setText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getGenre());
        Genre.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getGenre());
        Director.setText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getDirector());
        Director.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getDirector());
        hasWatched.setSelected(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).isHasWatched());
        ratingSlider.setValue(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getRating());
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
     * event handler for Edit Film button.
     *
     * @param event Button click event on Edit Film button.
     */
    @FXML
    private void editFilmButtonAction(ActionEvent event) {
        try {
            String title = Title.getText();
            int year = Integer.parseInt(Year.getText());
            String genre = Genre.getText();
            String director = Director.getText();
            boolean watched = hasWatched.isSelected();
            double rating = ratingSlider.getValue();
            // create new Film based on info provided by user
            Film film = new Film(title, year, director, genre, watched, rating);

            // below loop uses the comparesTo method to check if a duplicate film exists then throws exception if so
            for (int i = 0; i < cinemaWatchlistController.watchList.getSize(); i++) {
                if (cinemaWatchlistController.watchList.getFilm(i).compareTo(film) == 1
                        && cinemaWatchlistController.watchList.getFilm(i).getId() != cinemaWatchlistController.editingId) {
                    throw new InputMismatchException();
                }
            }

            Film.resetNumberOfFilms();
            film.setId(cinemaWatchlistController.editingId);
            cinemaWatchlistController.watchList.set(cinemaWatchlistController.editingId, film);
            cinemaWatchlistController.watchList.writeToFile(new File("src/main/resources/data/mainWatchlist.csv"));
            cinemaWatchlistController.watchList.clearWatchlist();
            cinemaWatchlistController.watchList.add(new File("src/main/resources/data/mainWatchlist.csv"));

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
        } catch (Exception ex4) {
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
            Year.setPromptText("INVALID");
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
     * event handler for Delete button.
     *
     * @param event Button click event on Delete button.
     */
    @FXML
    private void deleteFilmButtonAction(ActionEvent event) {
        // creates a Dialog alert upon click of Delete button
        Dialog deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        deleteConfirmation.setHeight(145);
        deleteConfirmation.setTitle("WARNING");
        deleteConfirmation.setHeaderText(null);
        deleteConfirmation.setGraphic(null);
        deleteConfirmation.initStyle(StageStyle.UNDECORATED);

        String filmTitle = cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle();
        if (filmTitle.length() > 50) {
            filmTitle = filmTitle.substring(0, 50) + "...";
        }

        deleteConfirmation.setContentText("You are about to delete film record '" + filmTitle + "'.\nProceed?");

        // adds stylesheet to Dialog alert
        DialogPane dialogPane = deleteConfirmation.getDialogPane();
        dialogPane.getStylesheets().add("/main/resources/styles/editWindow.css");
        dialogPane.getStyleClass().add("deleteConfirmation");

        // set location of Dialog alert based on mouse position
        deleteConfirmation.setOnShowing(event1 -> {
                    Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
                    double x = mouse.getX() - 200;
                    double y = mouse.getY() - 150;
                    deleteConfirmation.setX(x);
                    deleteConfirmation.setY(y);
                }
        );

        // deletes film upon clicking "OK"
        Optional<ButtonType> result = deleteConfirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            cinemaWatchlistController.watchList.remove(cinemaWatchlistController.editingId);
            cinemaWatchlistController.watchList.writeToFile(new File("src/main/resources/data/mainWatchlist.csv"));
            cinemaWatchlistController.watchList.clearWatchlist();
            cinemaWatchlistController.watchList.add(new File("src/main/resources/data/mainWatchlist.csv"));
            refreshTableView();
            cinemaWatchlistController.setAltWindowOpen();   // sets altWindowOpen back to false, allowing another window
            // to be opened
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();  // closes window
        }
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
     * useful when exception is thrown upon attempting to edit a film to an already existing film.
     * or if user input missing a field.
     *
     * @param actionEvent Mouse click event on user input field.
     */
    public void revertColor(MouseEvent actionEvent) {
        if (actionEvent.getSource().equals(Title)) {
            Title.setStyle("-fx-text-box-border-width: 0px;");
            Title.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getTitle());
        } else if (actionEvent.getSource().equals(Genre)) {
            Genre.setStyle("-fx-text-box-border-width: 0px;");
            Genre.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getGenre());
        } else if (actionEvent.getSource().equals(Director)) {
            Director.setStyle("-fx-text-box-border-width: 0px;");
            Director.setPromptText(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getDirector());
        } else if (actionEvent.getSource().equals(Year)) {
            Year.setStyle("-fx-text-box-border-width: 0px;");
            Year.setPromptText(Integer.toString(cinemaWatchlistController.watchList.getFilm(cinemaWatchlistController.editingId).getYear()));
        }
    }
}