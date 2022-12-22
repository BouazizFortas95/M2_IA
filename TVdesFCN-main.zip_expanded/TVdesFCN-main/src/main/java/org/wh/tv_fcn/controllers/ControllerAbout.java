package org.wh.tv_fcn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.wh.tv_fcn.Linguist;
import org.wh.tv_fcn.Main;

public class ControllerAbout {

  @FXML
  private Label aboutTxt;

  @FXML
  private TextArea aboutArea;

  private Linguist trans;

  @FXML
  private void initialize() {
    trans = new Linguist("org.wh.tv_fcn.bundles.textesAbout", Main.getLocale());
    translations();
  }

  public void onOKAbt() {
    Main.fermerAPropos();
  }

  private void translations() {
    aboutTxt.setText(trans.tr("about_txt"));
    aboutArea.setText(trans.tr("about_area"));
  }
}
