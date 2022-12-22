package org.wh.tv_fcn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.wh.tv_fcn.Linguist;
import org.wh.tv_fcn.Main;

/**
 * Created by W3B on 02/09/2018.
 */
public class ControllerHelp {

  @FXML
  private Label helpTxt;

  @FXML
  private TextArea helpArea;

  private Linguist trans;

  public void initialize() {
    trans = new Linguist("org.wh.tv_fcn.bundles.textesHelp", Main.getLocale());
    translations();
  }

  public void onOk() {
    Main.fermerAide();
  }

  private void translations() {
    helpTxt.setText(trans.tr("help_txt"));
    helpArea.setText(trans.tr("help_area"));
  }
}
