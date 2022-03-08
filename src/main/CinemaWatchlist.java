/*
    Name:  Shawn Pudjowargono, Patrick Mcginnis

    Description:
    Cinema Watchlist program. The purpose of this program is to allow users to keep track of films they have seen or
    not seen and rate films. Users will be able to add, edit or delete any films in the list, as well as search for
    any specific films. The film list can be sorted and a random film can be selected.

    Lastly, users have access to a summary window that will display information such as the total number of films,
    total number of watched films, total number of unwatched films, a list of the user's highest rated watched films
    and a list of the user's lowest rated watched films.

*/
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class CinemaWatchlist extends Application {
    // data fields used for moving window
    private static double xOffset = 0;
    private static double yOffset = 0;

    /**
     * main application window
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CinemaWatchlist.fxml"));
        setMainWindowMoveable(root, primaryStage);
        Scene scene = new Scene(root, 1050, 620);
        scene.getStylesheets().add("/main/resources/styles/CinemaWatchlist.css");
        primaryStage.setTitle("Cinema Watchlist");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * add film window
     *
     * @return returns loader upon stage creation, used to set the parent controller
     */
    protected static addWindowController addFilmWindow() {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader();
            loader.setLocation(CinemaWatchlist.class.getResource("addWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            setWindowMoveable(root, stage);
            Scene scene = new Scene(root, 470, 275);
            scene.getStylesheets().add("/main/resources/styles/addWindow.css");
            stage.setTitle("Add Film");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setOnHiding(event -> CinemaWatchlistController.setAltWindowOpen());
            setStageLocation(stage);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return loader.getController();
    }

    /**
     * edit film window
     *
     * @return returns loader upon stage creation, used to set the parent controller
     */
    protected static editWindowController editFilmWindow() {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader();
            loader.setLocation(CinemaWatchlist.class.getResource("editWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            setWindowMoveable(root, stage);
            Scene scene = new Scene(root, 470, 275);
            scene.getStylesheets().add("/main/resources/styles/editWindow.css");
            stage.setTitle("Edit Film");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setOnHiding(event -> CinemaWatchlistController.setAltWindowOpen());
            setStageLocation(stage);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return loader.getController();
    }

    /**
     * summary window
     */
    static void summaryWindow() {
        try {
            Parent root = FXMLLoader.load(CinemaWatchlist.class.getResource("summaryWindow.fxml"));
            Stage stage = new Stage();
            setWindowMoveable(root, stage);
            Scene scene = new Scene(root, 350, 275);
            scene.getStylesheets().add("main/resources/styles/summaryWindow.css");
            stage.setTitle("Summary");
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setOnHiding(event -> CinemaWatchlistController.setAltWindowOpen());
            setStageLocation(stage);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * method to make main window moveable on move drag
     *
     * @param root  root of main stage
     * @param stage stage of main stage
     */
    private void setMainWindowMoveable(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * method to make stage moveable on move drag
     *
     * @param root  root of stage to set as moveable on drag
     * @param stage root of stage to set as moveable on drag
     */
    private static void setWindowMoveable(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * sets X and Y of stage on screen based on mouse location
     *
     * @param stage stage of window to set X and Y
     */
    private static void setStageLocation(Stage stage) {
        Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
        double x = mouse.getX() - 150;
        double y = mouse.getY() - 75;
        stage.setX(x);
        stage.setY(y);
    }
}