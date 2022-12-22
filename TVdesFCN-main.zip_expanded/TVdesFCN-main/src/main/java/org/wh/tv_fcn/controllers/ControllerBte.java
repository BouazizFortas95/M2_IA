package org.wh.tv_fcn.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.wh.tv_fcn.Linguist;
import org.wh.tv_fcn.Main;

/**
 * Created by W3B on 02/09/2018.
 */
public class ControllerBte {
  @FXML
  private Text fermetureTxt;

  @FXML
  private Text questionTxt;

  @FXML
  private JFXButton quitter;

  @FXML
  private JFXButton annuler;

  private Linguist trans;

  @FXML
  public void initialize() {
    trans = new Linguist("org.wh.tv_fcn.bundles.textesQuit", Main.getLocale());
    translations();
  }

  public void onQuitter() {
    Main.fermerBte();
    Main.close();
  }

  public void onAnnuler() {
    Main.fermerBte();
  }

  private void translations() {
    questionTxt.setText(trans.tr("question_txt"));
    fermetureTxt.setText(trans.tr("closure_txt"));
    annuler.setText(trans.tr("annul_btn"));
    quitter.setText(trans.tr("quit_btn"));
  }
}
