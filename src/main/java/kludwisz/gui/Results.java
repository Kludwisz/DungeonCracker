package kludwisz.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class Results {
	private final Label [] labels = new Label[3];
	private final Button copyResults;
	public boolean gotResults = false;
	
	public Results() {
		this.labels[0] = new Label("Results:                     ");
		this.labels[1] = new Label("Total dungeon seeds:");
		this.labels[2] = new Label("Total structure seeds:");

		for (int i=0; i<3; i++) {
			Label label = this.labels[i];
			label.setVisible(false);
			label.setFont(Font.font(30.0D));
			label.setTranslateY(i * 60.0D + 100.0D);
			label.setTranslateX(180.0D);
		}
		
		this.copyResults = new Button("  Copy results to clipboard  ");
		this.copyResults.setScaleX(2.0D);
		this.copyResults.setScaleY(2.0D);
		this.copyResults.setTranslateX(320.0D);
		this.copyResults.setTranslateY(300.0D);
		
		this.copyResults.setOnMouseClicked(event -> {
			// copy results to user's clipboard
		});
		
		this.update();
	}
	
	public void addToPane(StackPane pane) {
		pane.getChildren().addAll(this.labels);
		pane.getChildren().add(copyResults);
	}
	
	public void update() {
		this.copyResults.setDisable(!this.gotResults);
		for (int i=0; i<3; i++)
			this.labels[i].setVisible(this.gotResults);
	}
}
