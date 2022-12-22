package org.wh.tv_fcn;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    private static Stage primaryStage;
    private static Stage stageBte;
    private static Stage stageHelp;
    private static Stage stageAbout;
    private static Stage stageIntro;
    private static Locale locale;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //initialisation de la locale
        locale = Locale.getDefault();

        //gestion des paramètres généraux de la fenêtre
        primaryStage.setTitle("TV des Expressions FCN");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(480);
        Main.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon2.png")));
        primaryStage.setMaximized(true);
        Parent root = FXMLLoader.load(getClass().getResource("views/vue.fxml"));
        primaryStage.setScene(new Scene(root));

        //Affichage de la première scène
        showIntro();

        new Thread(() -> {
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e){
                System.out.println("Erreur d'interruption du thread dans la classe Main" + e.getMessage());
            }
            finally {
                Platform.runLater(() -> {
                    primaryStage.show();
                    stageIntro.close();
                });
            }
        }).start();

        primaryStage.setOnCloseRequest(event -> {
            try {
                fermer(event);
            } catch (IOException e) {
                System.out.println("Erreur de formation de l'evenement : "+e.getMessage());
            }
        });

    }

    private void showIntro(){
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("images/icon2-large.png")));
        StackPane pane = new StackPane(img);
        double a=Math.random();
        if(a>=0.5) pane.setStyle("-fx-background-color:black;");
        else pane.setStyle("-fx-background-color:aqua;");
        stageIntro = new Stage(StageStyle.UNDECORATED);
        stageIntro.getIcons().add(new Image(getClass().getResourceAsStream("images/icon2.png")));
        stageIntro.setMaximized(true);
        stageIntro.setScene(new Scene(pane));
        stageIntro.show();
    }


    public static void fermer(WindowEvent event) throws IOException{
        if(event!=null) event.consume();
        StackPane pane = FXMLLoader.load(Main.class.getResource("views/vueBteQ.fxml"));
        Stage stage = new Stage();
        stageBte = stage;
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(pane));
        stage.setResizable(false);
        stage.setTitle("Question");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon2.png")));
        stage.showAndWait();
    }

    public static void ouvrirAide() throws IOException{
        StackPane pane = FXMLLoader.load(Main.class.getResource("views/vueHelp.fxml"));
        stageHelp = new Stage();
        stageHelp.initOwner(primaryStage);
        stageHelp.initModality(Modality.NONE);
        stageHelp.setTitle("Aide sur TV des FCN");
        stageHelp.setResizable(false);
        stageHelp.setScene(new Scene(pane));
        stageHelp.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon2.png")));
        stageHelp.showAndWait();
    }

    public static void ouvrirAPropos() throws IOException{
        if(stageAbout!=null) return;
        StackPane pane = FXMLLoader.load(Main.class.getResource("views/vueAbout.fxml"));
        stageAbout = new Stage();
        stageAbout.initOwner(primaryStage);
        stageAbout.initModality(Modality.NONE);
        stageAbout.setTitle("A propos de TV des FCN");
        stageAbout.setResizable(false);
        stageAbout.setScene(new Scene(pane));
        stageAbout.getIcons().add(new Image(Main.class.getResourceAsStream("images/icon2.png")));
        stageAbout.showAndWait();
    }

    public static void close(){
        primaryStage.close();
    }

    public static void fermerBte(){
        stageBte.close();
    }

    public static void fermerAide(){
        stageHelp.close();
    }

    public static void fermerAPropos(){
        stageAbout.close();
    }

    public static void pleinEcran(){
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        Main.locale = locale;
    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
