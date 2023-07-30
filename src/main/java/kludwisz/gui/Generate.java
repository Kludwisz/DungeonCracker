package kludwisz.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import kludwisz.cracker.DungeonDataReverser;
import kludwisz.cracker.ThreadHandler;

public class Generate {

	public MCVersion version = MCVersion.v1_17;
	private final Button versionButton;
	private final Button findSeedsButton;
	private boolean crackingInProgress = false;

	public Generate() {
		this.versionButton = new Button();
		this.versionButton.setScaleX(2.0D);
		this.versionButton.setScaleY(2.0D);
		this.versionButton.setTranslateX(320.0D);
		this.versionButton.setTranslateY(-20.0D);

		this.versionButton.setOnMouseClicked(event -> {
			this.version = MCVersion.values()[(this.version.ordinal() + 1) % MCVersion.values().length];
			this.updateVersion();
		});

		this.updateVersion();

		this.findSeedsButton = new Button("          Crack Seed          ");
		this.findSeedsButton.setScaleX(2.0D);
		this.findSeedsButton.setScaleY(2.0D);
		this.findSeedsButton.setTranslateX(320.0D);
		this.findSeedsButton.setTranslateY(40.0D);
		this.findSeedsButton.setDisable(true);

		this.findSeedsButton.setOnMouseClicked(event -> {
			if (!this.crackingInProgress) {
				ThreadHandler.startRunning();
				this.crackingInProgress = true;
				this.findSeedsButton.setText("            Cancel            ");
				
			}
			else {
				ThreadHandler.stopRunning();
				this.crackingInProgress = false;
				this.findSeedsButton.setText("          Crack Seed          ");
			}
		});
	}

	private void updateVersion() {
		this.versionButton.setText("          Version: " + this.version.toString() + "          ");
	}

	public void addToPane(StackPane pane) {
		pane.getChildren().addAll(this.versionButton, this.findSeedsButton);
	}

	public void update() {
		boolean flag = DungeonCracker.spawnerCoords.parse();
		this.findSeedsButton.setDisable(!flag);
	}

}
