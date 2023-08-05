package kludwisz.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LootFilterOptions extends Stage {
	private static final Font MONO = Font.font("Monospace", 26);
	public static LootFilterOptions instance;
	public boolean filteringEnabled = false;
	
	private final StackPane pane;
	private final Label mainLabel;
	public final TextArea filterInputArea;
	private final Button doneButton;
	private final Button filterEnabledButton;
	private final Button helpButton;
	
	private String savedContent = "";
	private boolean inHelpMode = false;
	
	LootFilterOptions() {
		this.pane = new StackPane();
		this.getIcons().add(new Image(DungeonCracker.class.getResourceAsStream("/dungeonCrackerIcon.png")));
		this.setTitle("Loot Filter Settings");
		this.setResizable(false);
		
		Scene scene = new Scene(this.pane, 720, 480);
		scene.getStylesheets().add("darkmode2.css");
		this.setScene(scene);
		
		this.mainLabel = new Label("   Edit Loot Filter   ");
		this.mainLabel.setScaleX(2.0D);
		this.mainLabel.setScaleY(2.0D);
		this.mainLabel.setTranslateY(-220.0D);
		
		this.filterInputArea = new TextArea();
		this.filterInputArea.setScaleX(0.75D);
		this.filterInputArea.setScaleY(0.75D);
		this.filterInputArea.setTranslateY(-10.0D);
		this.filterInputArea.setFont(MONO);
		this.filterInputArea.setText("");
		
		this.doneButton = ButtonMaker.make(2, 2, -200, 200);
		this.doneButton.setText("  Done  ");
		this.doneButton.setOnMouseClicked((event) -> {
			this.hide();
		});
		
		this.filterEnabledButton = ButtonMaker.make(2, 2, 0, 200);
		this.filterEnabledButton.setText(" Loot Filter: OFF ");
		this.filterEnabledButton.setOnMouseClicked((event) -> {
			if (this.filteringEnabled) {
				this.filteringEnabled = false;
				this.filterEnabledButton.setText(" Loot Filter: OFF ");
			} else {
				this.filteringEnabled = true;
				this.filterEnabledButton.setText(" Loot Filter: ON ");
			}
		});
		
		this.helpButton = ButtonMaker.make(2, 2, 200, 200);
		this.helpButton.setText("  Help  ");
		this.helpButton.setOnMouseClicked((event) -> {
			if (!this.inHelpMode) {
				this.savedContent = this.filterInputArea.getText();
				this.doneButton.setVisible(false);
				this.filterEnabledButton.setVisible(false);
				this.helpButton.setText("  Back  ");
				this.inHelpMode = true;
				this.mainLabel.setText("   Loot filtering tutorial   ");
				this.filterInputArea.setText( 
						  "// EXAMPLE #1:\n"
						+ "> 5 gunpowder\n"
						+ "= 1 golden_apple\n"
						+ "< 5 rotten_flesh\n\n"
						+ "The above filter checks if a dungeon chest\n"
						+ "has more than 5 gunpowder AND exactly 1\n"
						+ "golden apple AND less than 5 rotten flesh.\n\n"
						+ "// EXAMPLE #2:\n"
						+ "> 3 bone\n"
						+ "< 7 bone\n\n"
						+ "This filter checks if a dungeon chest has\n"
						+ "Between 4 and 6 bones (inclusive).");
				this.filterInputArea.setEditable(false);
			} else {
				this.exitHelp();
			}
		});
		
		this.setOnCloseRequest((event) -> {
			if (this.inHelpMode) 
				this.exitHelp();
		});
		
		this.pane.getChildren().addAll(mainLabel, filterInputArea, doneButton, filterEnabledButton, helpButton);
	}
	
	public void exitHelp() {
		this.doneButton.setVisible(true);
		this.filterEnabledButton.setVisible(true);
		this.filterInputArea.setEditable(true);
		this.filterInputArea.setText(this.savedContent);
		this.savedContent = "";
		this.helpButton.setText("  Help  ");
		this.mainLabel.setText("   Edit Loot Filter   ");
		this.inHelpMode = false;
	}
}
