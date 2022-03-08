
package main;

import main.resources.classes.Film;
import main.resources.classes.WatchList;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CinemaWatchlistController implements Initializable {
    static WatchList watchList = new WatchList();

    CinemaWatchlistController thisController;

    addWindowController addWindow;
    editWindowController editWindow;

    /**
     * variable value is set when user double clicks on a record to edit it
     * used so the application knows which film to change in the watchlist
     */
    protected static int editingId;

    /**
     * variable used to keep track of whenever another window is opened.
     * when user opens another window, such as the add, edit or summary windows, altWindowOpen is set to true.
     * while true, no other additional windows may be opened by the user.
     */
    protected static boolean altWindowOpen;

    /**
     * keeps track of whether library, watched or unwatched is displayed
     * library when currentView = 0;
     * watched when currentView = 1;
     * unwatched when currentView = 2;
     * used to refresh tableview upon any changes in watchList collection
     */
    protected static int currentView;

    // variable for each tableview column
    @FXML
    protected TableView<Film> tableView;
    @FXML
    protected TableColumn<Film, String> Title;
    @FXML
    protected TableColumn<Film, String> Year;
    @FXML
    protected TableColumn<Film, String> Genre;
    @FXML
    protected TableColumn<Film, String> Director;
    @FXML
    protected TableColumn<Film, String> Watched;
    @FXML
    protected TableColumn<Film, String> Rating;
    @FXML
    protected TextField searchBar;

    /**
     * variable for the bottom right text indicating number of records displayed out of total records
     */
    @FXML
    protected Label numberOfRecords;

    /**
     * variable for the text in title bar of the main application window
     */
    @FXML
    protected Label title;

    /**
     * populates tableview on start of application, initializes controller and sets up dynamic search bar
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        thisController = this;
        currentView = 0; // sets to 0 as library view is the first view displayed

        watchList.add(new File("src/main/resources/data/mainWatchlist.csv"));

        Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Year.setCellValueFactory(new PropertyValueFactory<>("Year"));
        Genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        Director.setCellValueFactory(new PropertyValueFactory<>("Director"));
        // sets value of watched column to Yes or No based on boolean value in data
        Watched.setCellValueFactory(cellData -> {
            boolean watched = cellData.getValue().isHasWatched();
            String watchedAsString;
            if (watched == true) {
                watchedAsString = "Yes";
            } else {
                watchedAsString = "No";
            }
            return new ReadOnlyStringWrapper(watchedAsString);
        });
        Rating.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        tableView.getItems().setAll(parseFilmList(watchList.getCollection()));

        dynamicSearchBar();
        updateDisplayNumber(watchList.getSize());
        openEditFilmWindow();
    }

    /**
     * parse and construct user datamodel list
     */
    protected ArrayList<Film> parseFilmList(ArrayList<Film> collection) {
        ArrayList<Film> tablewatchlist;
        tablewatchlist = new ArrayList<>();
        for (int k = 0; k < collection.size(); k++) {
            tablewatchlist.add(collection.get(k));
        }
        return tablewatchlist;
    }

    /**
     * sets up dynamic search bar, called in initializer
     */
    private void dynamicSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                title.setText("Cinema Watchlist - Search Results: " + newValue);
                tableView.getItems().setAll(parseFilmList(watchList.get(newValue.toLowerCase().trim())));
                updateDisplayNumber(watchList.get(newValue.toLowerCase().trim()).size());
            } else {
                // returns to previous tableview if search bar is empty
                switch (currentView) {
                    case 0:
                        libraryButtonAction();
                        break;
                    case 1:
                        watchedButtonAction();
                        break;
                    case 2:
                        unwatchedButtonAction();
                }
            }
        });
    }

    /**
     * method that opens edit film window on double-click of any record
     * is called in the initializer
     */
    protected void openEditFilmWindow() {
        tableView.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() == 2 && !altWindowOpen) {
                editingId = tableView.getSelectionModel().getSelectedItem().getId();

                editWindow = CinemaWatchlist.editFilmWindow();
                editWindow.setParentController(thisController);

                // sets addWindowOpen to true so that only a single addFilm window can be open at a time
                altWindowOpen = true;
            }
        });
    }

    /**
     * when any other window is opened (add window, edit window, summary) the boolean variable altWindowOpen is
     * set to "true", while true, no other windows can be opened. This is so only a single other window can be open at
     * a time.
     * this method sets altWindowOpen boolean variable to false. It will be called whenever any of the other windows
     * are closed, allowing the user to open another window again.
     */
    protected static void setAltWindowOpen() {
        altWindowOpen = false;
    }


    /**
     * event handler for closing Add Film window.
     *
     * @param event Button click on closing window button (X).
     */
    @FXML
    private void closeButtonAction(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
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
     * event handler for Library button, displaying the full watch list.
     */
    @FXML
    protected void libraryButtonAction() {
        currentView = 0;
        title.setText("Cinema Watchlist - Library");
        tableView.getItems().setAll(parseFilmList(watchList.getCollection()));
        // set bottom right text displaying "xx of xx"
        updateDisplayNumber(watchList.getSize());
    }

    /**
     * event handler for Watched button, displaying watched films.
     */
    @FXML
    protected void watchedButtonAction() {
        currentView = 1;
        title.setText("Cinema Watchlist - Watched");
        tableView.getItems().setAll(parseFilmList(watchList.getWatched()));
        // set bottom right text displaying "xx of xx"
        updateDisplayNumber(watchList.getWatched().size());
    }

    /**
     * event handler for Unwatched button, displaying unwatched films.
     */
    @FXML
    protected void unwatchedButtonAction() {
        currentView = 2;
        title.setText("Cinema Watchlist - Unwatched");
        tableView.getItems().setAll(parseFilmList(watchList.getUnwatched()));
        // set bottom right text displaying "xx of xx"
        updateDisplayNumber(watchList.getUnwatched().size());
    }

    /**
     * event handler for Add Film button, opens add film window on click.
     * also sets altWindowOpen to true so that no other windows can be opened simultaneously.
     */
    @FXML
    private void addFilmButtonAction() {
        if (!altWindowOpen) {

            addWindow = CinemaWatchlist.addFilmWindow();
            addWindow.setParentController(thisController);

            // sets addWindowOpen to true so that only a single addFilm window can be open at a time
            altWindowOpen = true;
        }
    }

    /**
     * highlights a random record in the tableview
     */
    @FXML
    private void randomButtonAction() {
        int randomIndex = 0;
        switch (currentView) {
            case 0:
                randomIndex = (int) (Math.random() * watchList.getSize() - 1);
                break;
            case 1:
                randomIndex = (int) (Math.random() * watchList.getWatched().size() - 1);
                break;
            case 2:
                randomIndex = (int) (Math.random() * watchList.getUnwatched().size() - 1);
        }
        tableView.getSelectionModel().select(randomIndex);
        tableView.scrollTo(randomIndex);
    }

    /**
     * event handler for Summary button, opens summary window on click.
     * also sets altWindowOpen to true so that no other windows can be opened simultaneously.
     */
    @FXML
    private void summaryButtonAction() {
        if (!altWindowOpen) {
            CinemaWatchlist.summaryWindow();
            // sets addWindowOpen to true so that only a single addFilm window can be open at a time
            altWindowOpen = true;
        }
    }

    /**
     * event handler for search bar. obsolete as search bar is now dynamic
     */
    @FXML
    private void searchOnEnter() {
        title.setText("Cinema Watchlist - Search Results: " + searchBar.getText());
        tableView.getItems().setAll(parseFilmList(watchList.get(searchBar.getText().toLowerCase().trim())));
        updateDisplayNumber(watchList.get(searchBar.getText().toLowerCase().trim()).size());
    }

    /**
     * updates label "xx of xx" in the bottom right
     *
     * @param numberOfDisplayedFilms number of displayed films in current table
     */
    private void updateDisplayNumber(int numberOfDisplayedFilms) {
        numberOfRecords.setText(numberOfDisplayedFilms + " of " + watchList.getSize() + " records");
    }

}