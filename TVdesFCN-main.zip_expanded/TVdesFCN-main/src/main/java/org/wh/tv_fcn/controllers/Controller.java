package org.wh.tv_fcn.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.wh.tv_fcn.Linguist;
import org.wh.tv_fcn.Main;
import org.wh.tv_fcn.fcn.Action;
import org.wh.tv_fcn.fcn.ExpressionFCN;
import org.wh.tv_fcn.fcn.FCNException;
import org.wh.tv_fcn.fcn.DisplayMode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Controller {

  @FXML
  private BorderPane mainWindow;

  @FXML
  private Menu fileMenu;

  @FXML
  private MenuItem exportAct;

  @FXML
  private MenuItem printAct;

  @FXML
  private MenuItem closeAct;

  @FXML
  private Menu editMenu;

  @FXML
  private MenuItem historyAct;

  @FXML
  private Menu affModeAct;

  @FXML
  private RadioMenuItem vf;

  @FXML
  private RadioMenuItem zu;

  @FXML
  private Menu displayMenu;

  @FXML
  private MenuItem fullscreenAct;

  @FXML
  private Menu zoomAct;

  @FXML
  private MenuItem zoomNormAct;

  @FXML
  private MenuItem zoomPlusAct;

  @FXML
  private MenuItem zoomMoinsAct;

  @FXML
  private Menu helpMenu;

  @FXML
  private MenuItem helpAct;

  @FXML
  private MenuItem aboutAct;

  @FXML
  private HBox bteField;

  @FXML
  private TextField entree;

  @FXML
  private WebView vue;

  @FXML
  private ProgressIndicator progressInd;

  @FXML
  private Label status;

  @FXML
  private Menu langMenu;

  @FXML
  private RadioMenuItem dfltLangAct;

  @FXML
  private RadioMenuItem englishAct;

  @FXML
  private RadioMenuItem frenchAct;

  private VBox boxHistory;

  private JFXListView<String> liste;

  private HashSet<String> listeHist;

  private DropShadow ombre;

  private Text txt;

  private JFXButton btnMenuExp;
  private JFXButton btnMenuPrint;
  private JFXButton btnMenuQuit;
  private JFXButton btnMenuHist;
  private JFXButton btnMenuFullscreen;
  private JFXButton btnMenuHelp;
  private JFXButton btnMenuAbout;

  private VBox boxMenu;

  private ExpressionFCN expression;
  private WebEngine moteur;
  private static String teteHtml = "<html>\n<head>\n\t<title>TV des FCN</title>\n\t<meta content=\"text/html; charset=utf-8\">\n</head>\n<body>\n<style>\nth,td{\n\tborder: 1px solid black;\n\tmin-width: 40px;\n}\ntable{\n\tborder-collapse: collapse;\n\ttext-align: center;\n}\nbody{\n\tfont-family:'Segoe UI', 'trebuchet MS';\n}\nspan{\n\tcolor:royalblue;\n}\n.err{color:red;}\n</style>\n";
  private static String finHtml;
  private DisplayMode mode;

  private Linguist trans;

  @FXML
  private void initialize() {
    //Initialisation du moteur WEB
    moteur = vue.getEngine();
    mode = DisplayMode.TRUE_FALSE;

    //Ajout des menus
    ToggleGroup group0 = new ToggleGroup();
    group0.getToggles().addAll(vf, zu);

    ToggleGroup group1 = new ToggleGroup();
    group1.getToggles().addAll(dfltLangAct, frenchAct, englishAct);

    //Création de la boite qui va contenir la box

    txt = new Text();
    txt.setFont(new Font(20));
    ImageView iconClock = new ImageView(new Image(Main.class.getResourceAsStream("images/clock.png")));
    HBox pre = new HBox(10);
    pre.setAlignment(Pos.CENTER);
    pre.getChildren().addAll(iconClock, txt);
    pre.setAlignment(Pos.CENTER);
    pre.getStyleClass().add("fond-blanc");

    liste = new JFXListView<>();
    liste.setPlaceholder(new Label("Aucun contenu dans l'historique!"));
    liste.setOnMouseClicked(this::onListeClk);

    boxHistory = new VBox(5);
    VBox.setVgrow(liste, Priority.ALWAYS);
    boxHistory.setPadding(new Insets(5, 5, 5, 5));
    boxHistory.getStyleClass().addAll("fond-blanc", "htry");
    boxHistory.getStylesheets().add("org/wh/tv_fcn/css/style.css");
    boxHistory.setAlignment(Pos.TOP_CENTER);
    boxHistory.getChildren().addAll(pre, liste);

    //Création de la box du menuSlide
    boxMenu = creerMenuBox();

    //Initialisation de la langue du programme
    trans = new Linguist("org.wh.tv_fcn.bundles.textes", Main.getLocale());
    translations();

    //la liste de l'historique
    listeHist = new HashSet<>();

    ombre = new DropShadow();


  }

  public void valiClick() {
    if (entree.getText().isEmpty()) {
      setStatus(trans.tr("enter_first_sts"), false);
      return;
    }
    moteur.load(Main.class.getResource("html/chargement.html").toString());
    setStatus(trans.tr("resolution_on_sts"), true);
    new Thread(() -> {
      try {
        expression = new ExpressionFCN(entree.getText());
        expression.resolve();
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      } catch (FCNException e) {
        e.printStackTrace();
      } finally {
        Platform.runLater(() -> {
          setStatus(trans.tr("resolution_off_sts"), false);
          this.afficherTable();
          if (listeHist.add(expression.getExpression())) liste.getItems().add(expression.getExpression());
        });
      }
    }).start();
  }

  private void afficherTable() {
//        if(expression.getAction()==Action.ERREUR){
//            moteur.loadContent(teteHtml+"<p class='err'>"+trans.tr("expr_has_err_msg")+"<br>"+expression.getErrStr()+"</p></body></html>");
//        }
    String titre = "<h1>" + trans.tr("table_of_msg") + "<br><span>" + expression.getExpression() + "</span></h1>";
    moteur.loadContent(teteHtml + titre + expression.toHTML(mode) + finHtml);
  }

  public void onClose() {
    try {
      Main.fermer(null);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void onSaveHtml() {
//        if(expression.getAction()!= Action.FIN){
//            setStatus(trans.tr("solve_first_sts"),false);
//            return;
//        }
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(trans.tr("export_html_act"));
    fileChooser.setInitialDirectory(new File("C:/"));
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(trans.tr("html_file_diag"), "*.htm", "*.html"));
    File fic = null;
    try {
      fic = fileChooser.showSaveDialog(Main.getPrimaryStage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    if (fic == null) {
      System.out.println(trans.tr("save_imp_sts"));
      return;
    }
    try {
      setStatus(trans.tr("save_on_sts"), true);
      DateFormat fmt = DateFormat.getDateInstance(DateFormat.SHORT, Main.getLocale());
      String titre = "<h1>" + trans.tr("table_of_msg") + " <span>" + expression.getExpression() + "</span></h1>";
      String html = "<!--" + trans.tr("generated_by_msg") + " : " + fmt.format(new Date()) + "-->\n" + teteHtml + titre + expression.toHTML(mode) + finHtml;
      Files.write(fic.toPath(), html.getBytes(StandardCharsets.UTF_8));
      setStatus(trans.tr("save_off_sts"), false);
    } catch (IOException e) {
      setStatus(trans.tr("save_imp_sts"), false);
    }
  }

  public void onPrint() {
//        if(expression.getAction()!= Action.FIN){
//            setStatus(trans.tr("solve_first_sts"),false);
//            return;
//        }
    PrinterJob printerJob = PrinterJob.createPrinterJob();
    if (printerJob == null) {
      setStatus(trans.tr("print_err_sts"), false);
      return;
    }
    printerJob.getJobSettings().setJobName("Table de vérité " + System.currentTimeMillis());
    boolean proceed = printerJob.showPrintDialog(Main.getPrimaryStage());
    if (proceed) {
      setStatus(trans.tr("print_on_sts"), true);
      moteur.print(printerJob);
      printerJob.endJob();
      setStatus(trans.tr("print_off_sts"), false);
    }
  }

  public void zoomPlus() {
    vue.setZoom(vue.getZoom() + 0.1);
    zoomAct.setText(trans.tr("zoom_act") + (int) (vue.getZoom() * 100) + "%");
  }

  public void zoomMoins() {
    vue.setZoom(vue.getZoom() - 0.1);
    zoomAct.setText(trans.tr("zoom_act") + (int) (vue.getZoom() * 100) + "%");
  }

  public void zoomNormal() {
    vue.setZoom(1);
    zoomAct.setText(trans.tr("zoom_act") + "100%");
  }

  public void onAffVF() {
    mode = DisplayMode.TRUE_FALSE;
    // if(expression.getAction()==Action.FIN) this.afficherTable();
  }

  public void onAff01() {
    mode = DisplayMode.NUMBERS;
    // if(expression.getAction()==Action.FIN) this.afficherTable();
  }

  public void onHelpAct() {
    onVueClk();
    // @FixMe: expression.setExpression("");
    //moteur.load("org/wh/tv_fcn/help/TVdesFCN.html");
    String url = "https://tvdesfcn-aide.web.app/TVdesFCN.html";
    if (url.startsWith("jar")) { //La classe est empaquettée dans un jar
      url = url.substring(4, url.lastIndexOf("org") - 1);
      url = url.substring(0, url.lastIndexOf("/")) + "/help/TVdesFCN.html";
      System.out.println("dans le jar ----" + url);
    }
    moteur.load(url);
  }

  public void onAbout() {
    try {
      Main.ouvrirAPropos();
    } catch (IOException e) {
      System.err.println("Erreur de chargement du fichier fxml : " + e.getMessage());
    }
  }

  public void afficherHistorique() {
    //if(liste==null) System.out.println("Attention la liste est nulle");
    if (liste.getItems().size() < 1) setStatus("Aucun contenu dans l'historique", false);
    if (mainWindow.getLeft() == boxHistory) hideSlide(boxHistory, 400);
    else if (mainWindow.getLeft() == boxMenu) switchSlide(boxMenu, boxHistory, 410);
    else showSlide(boxHistory, 500);
  }

  private void onAfficherBtn() {
    String chaine = liste.getSelectionModel().getSelectedItem();
    if (chaine == null) {
      setStatus(trans.tr("select_first_act"), false);
      return;
    }
    entree.setText(chaine);
    this.valiClick();
  }

  public void onVueClk() {
    if (mainWindow.getLeft() != null)
      hideSlide((VBox) mainWindow.getLeft(), 350);
  }

  private void onListeClk(MouseEvent event) {
    this.onAfficherBtn();
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2 && mainWindow.getLeft() != null)
      hideSlide((VBox) mainWindow.getLeft(), 400);
  }

  public void onFullscreenAct() {
    Main.pleinEcran();
  }

  public void onBteFieldEntered() {
    ombre.setRadius(5);
    ombre.setColor(Color.BLACK);
    ombre.setWidth(10);
    ombre.setHeight(10);
    bteField.setEffect(ombre);
  }

  public void onBteFieldExited() {
    ombre.setRadius(2);
    ombre.setColor(Color.GRAY);
    ombre.setWidth(5);
    ombre.setHeight(5);
    bteField.setEffect(ombre);
  }

  private void translations() {
    fileMenu.setText(trans.tr("file_menu"));
    exportAct.setText(trans.tr("export_html_act"));
    printAct.setText(trans.tr("print_act"));
    closeAct.setText(trans.tr("close_act"));
    editMenu.setText(trans.tr("edit_menu"));
    historyAct.setText(trans.tr("history_act"));
    affModeAct.setText(trans.tr("mode_aff_act"));
    displayMenu.setText(trans.tr("display_menu"));
    fullscreenAct.setText(trans.tr("fullscreen_act"));
    zoomAct.setText(trans.tr("zoom_act"));
    zoomNormAct.setText(trans.tr("zoom0_act"));
    zoomMoinsAct.setText(trans.tr("zoom-_act"));
    zoomPlusAct.setText(trans.tr("zoom+_act"));
    helpMenu.setText(trans.tr("help_menu"));
    helpAct.setText(trans.tr("help_act"));
    aboutAct.setText(trans.tr("about_act"));
    entree.setPromptText(trans.tr("enter_logic_prompt"));
    langMenu.setText(trans.tr("lang_menu"));
    dfltLangAct.setText(trans.tr("dflt_lang_act"));
    englishAct.setText(trans.tr("english_lang_act"));
    frenchAct.setText(trans.tr("french_lang_act"));
    this.setStatus(trans.tr("enter_first_sts"), false);
    txt.setText(trans.tr("history_act"));
    liste.setPlaceholder(new Label(trans.tr("no_content_msg")));
    finHtml = "<p>\n\t<span>" + trans.tr("generated_by_msg") + "</span>\n</p>\n</body>\n</html>\n";
//        @FixMe: switch (expression.getAction()){
//            case DEBUT:
//                moteur.loadContent(teteHtml+"<center><p>"+trans.tr("welcome_msg")+"</p></center></body></html>");
//                break;
//            case ERREUR:
//            case FIN:
//                this.afficherTable();
//                break;
//        }
    btnMenuQuit.setText(trans.tr("close_act"));
    btnMenuPrint.setText(trans.tr("print_act"));
    btnMenuHist.setText(trans.tr("history_act"));
    btnMenuHelp.setText(trans.tr("help_act"));
    btnMenuFullscreen.setText(trans.tr("fullscreen_act"));
    btnMenuExp.setText(trans.tr("export_menu"));
    btnMenuAbout.setText(trans.tr("about_act"));
  }

  private void setStatus(String text, boolean enable) {
    progressInd.setDisable(!enable);
    status.setText(text);
  }

  public void onDfltLangAct() {
    Main.setLocale(Locale.getDefault());
    trans.setLocale(Main.getLocale());
    translations();
  }

  public void onFrenchLangAct() {
    Main.setLocale(Locale.FRENCH);
    trans.setLocale(Locale.FRENCH);
    translations();
  }

  public void onEnglishLangAct() {
    Main.setLocale(Locale.ENGLISH);
    trans.setLocale(Locale.ENGLISH);
    translations();
  }

  private void showSlide(VBox box, long time) {
    mainWindow.setLeft(box);
    box.setTranslateX(-box.getWidth());
    TranslateTransition transition = new TranslateTransition(Duration.millis(time), box);
    transition.setByX(box.getWidth());
    transition.play();
  }

  private void hideSlide(VBox box, long time) {
    TranslateTransition transition = new TranslateTransition(Duration.millis(time), box);
    transition.setByX(-box.getWidth());
    transition.play();
    transition.setOnFinished(e -> mainWindow.setLeft(null));
  }

  private void switchSlide(VBox oldBox, VBox newBox, long time) {
    FadeTransition transition = new FadeTransition(Duration.millis(time), oldBox);
    transition.setToValue(0);
    //transition.setByX(oldBox.getWidth());
    transition.play();
    transition.onFinishedProperty().set(event -> {
      mainWindow.setLeft(newBox);
      oldBox.setOpacity(1);
      showSlide(newBox, time + 100);
    });
  }

  private VBox creerMenuBox() {
    VBox menuBox = new VBox(4);
    menuBox.setPadding(new Insets(2, 10, 0, 10));
    menuBox.setId("menu-slide");
    btnMenuAbout = new JFXButton();
    btnMenuAbout.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/info32-blue.png"))));
    btnMenuAbout.setPrefSize(140, 36);
    btnMenuAbout.setOnMouseClicked(event -> onAbout());
    btnMenuExp = new JFXButton();
    btnMenuExp.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/export32-color.png"))));
    btnMenuExp.setPrefSize(140, 36);
    btnMenuExp.setOnMouseClicked(event -> onSaveHtml());
    btnMenuFullscreen = new JFXButton();
    btnMenuFullscreen.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/fullscreen32-color.png"))));
    btnMenuFullscreen.setPrefSize(140, 36);
    btnMenuFullscreen.setOnMouseClicked(event -> onFullscreenAct());
    btnMenuHelp = new JFXButton();
    btnMenuHelp.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/question32-blue.png"))));
    btnMenuHelp.setPrefSize(140, 36);
    btnMenuHelp.setOnMouseClicked(event -> onHelpAct());
    btnMenuHist = new JFXButton();
    btnMenuHist.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/clock32.png"))));
    btnMenuHist.setPrefSize(140, 36);
    btnMenuHist.setOnMouseClicked(event -> afficherHistorique());
    btnMenuPrint = new JFXButton();
    btnMenuPrint.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/save32-color.png"))));
    btnMenuPrint.setPrefSize(140, 36);
    btnMenuPrint.setOnMouseClicked(event -> onPrint());
    btnMenuQuit = new JFXButton();
    btnMenuQuit.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("images/exit32.png"))));
    btnMenuQuit.setPrefSize(140, 36);
    btnMenuQuit.setOnMouseClicked(event -> onClose());
    menuBox.getChildren().addAll(btnMenuExp, btnMenuPrint, new Separator(), btnMenuHist, btnMenuFullscreen, new Separator(), btnMenuHelp, btnMenuAbout, new Separator(), btnMenuQuit);
    return menuBox;
  }

  public void onMenuClick() {
    if (mainWindow.getLeft() == boxMenu) hideSlide(boxMenu, 450);
    else if (mainWindow.getLeft() == boxHistory) switchSlide(boxHistory, boxMenu, 400);
    else showSlide(boxMenu, 500);
  }

}
