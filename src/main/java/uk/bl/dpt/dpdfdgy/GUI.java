package uk.bl.dpt.dpdfdgy;

import java.io.File;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class GUI extends Application {

	protected TabPane tabs = null;
	protected Tab resultsTab = null;
	protected Tab rulesTab = null;
	protected Rules rules;
	protected TextField dirTextField;
	protected TextArea resultsTextArea;
	protected File pdfDir;
	protected Map<String, Rule> rulesMap;

	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("PDF Eh?");
		Group root = new Group();
		Scene scene = new Scene(root, 640, 480, Color.ANTIQUEWHITE);

		tabs = new TabPane();

		BorderPane borderPane = new BorderPane();

		/*
		 * Results Tab
		 */
		resultsTab = new Tab();
		resultsTab.setText("Results");
		resultsTab.setClosable(false);
		tabs.getTabs().add(resultsTab);

		VBox resultsVbox = new VBox(8);

		Label label = new Label("Source Directory:");
		dirTextField = new TextField();
		dirTextField.setMinSize(400, 1);
		HBox hb = new HBox();
		hb.getChildren().addAll(label, dirTextField);
		hb.setSpacing(10);
		Button buttonLoad = new Button("Load");
		// TODO one file at a time? Need a fileChooser then!
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				DirectoryChooser dirChooser = new DirectoryChooser();
				pdfDir = dirChooser.showDialog(stage);
				dirTextField.setText(pdfDir.getAbsolutePath());
			}
		});
		hb.getChildren().add(buttonLoad);

		resultsVbox.getChildren().add(hb);

		Button buttonValidate = new Button("Validate");
		buttonValidate.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (pdfDir != null && pdfDir.exists()) {
					validatePDFs(pdfDir);
				}
			}
		});
		resultsVbox.getChildren().add(buttonValidate);

		resultsTextArea = new TextArea();
		resultsTextArea.setMinSize(400, 400);
		resultsTextArea.setText("Please select a directory...");

		resultsVbox.getChildren().add(resultsTextArea);

		resultsTab.setContent(resultsVbox);

		/*
		 * Rules Tab
		 */
		rules = new Rules();
		rulesMap = rules.getRules();
		rulesTab = new Tab();
		rulesTab.setText("Rules");
		rulesTab.setClosable(false);
		tabs.getTabs().add(rulesTab);

		VBox rulesVbox = new VBox(8); // spacing = 8

		for (String ruleKey : rulesMap.keySet()) {
			CheckBox cb = new CheckBox(ruleKey);
			cb.setSelected(rulesMap.get(ruleKey).isActive()); // TODO add warnOn
																// & failOn

			cb.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (event.getSource() instanceof CheckBox) {
						CheckBox chk = (CheckBox) event.getSource();
						if (rulesMap.containsKey(chk.getText()))
							;
						rulesMap.get(chk.getText()).setActive(
								!(rulesMap.get(chk.getText()).isActive()));
					}
				}
			});

			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> ov,
						Boolean old_val, Boolean new_val) {

				}
			});
			rulesVbox.getChildren().add(cb);
		}

		rulesTab.setContent(rulesVbox);

		// Test
		RobotInDisguise rib = new RobotInDisguise("test");
		resultsTextArea.setText(rib.getXsl());
		
		// bind to take available space
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());

		borderPane.setCenter(tabs);
		root.getChildren().add(borderPane);
		stage.setScene(scene);
		stage.show();
	}

	protected void validatePDFs(File pdfDir) {
		walkTreeAndValidate(pdfDir);
	}

	private void walkTreeAndValidate(File current) {
		if (current.isDirectory()) {
			File[] files = current.listFiles();
			for (File f : files) {
				walkTreeAndValidate(f);
			}
		} else { // is a file presumably!
			PDFValidator v = new PDFValidator(current);
			resultsTextArea.setText("Testing " + current.getName());
			v.validate();
			String result = v.getResult();
			resultsTextArea.setText(result);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
