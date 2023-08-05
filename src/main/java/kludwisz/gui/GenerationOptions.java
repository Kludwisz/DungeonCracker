package kludwisz.gui;

import com.seedfinding.mccore.version.MCVersion;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class GenerationOptions {
	public MCVersion version = MCVersion.v1_17;
	private final MCVersion[] supportedVersions = {MCVersion.v1_13, MCVersion.v1_14, MCVersion.v1_15, MCVersion.v1_16, MCVersion.v1_17};
	private int versionIndex = 4;

	public BiomeType biome = BiomeType.NOTDESERT;
	private final Button versionButton;
	private final Button biomeButton;
	private final Button lootFilterButton;
	private final Button findSeedsButton;
	private int programStage = 0; // 0- waiting for input,  1- cracking seed,  2- finished, waiting for reset
	
	public GenerationOptions() {
		this.versionButton = ButtonMaker.make(2, 2, 420, -20);
		this.versionButton.setOnMouseClicked(event -> {
			this.version = this.supportedVersions[(++this.versionIndex) % 5];
			this.updateVersion();
		});
		this.updateVersion();

		this.biomeButton = ButtonMaker.make(2, 2, 220, -20);
		this.biomeButton.setText(this.biome.displayName);
		this.biomeButton.setOnMouseClicked(event -> {
			this.biome = BiomeType.values()[(this.biome.ordinal() + 1) % BiomeType.values().length];
			this.biomeButton.setText(this.biome.displayName);
		});
		
		this.lootFilterButton = ButtonMaker.make(2, 2, 555, -20);
		this.lootFilterButton.setText(" Loot... ");
		this.lootFilterButton.setOnMouseClicked(event -> {
			if (!LootFilterOptions.instance.isShowing()) {
				LootFilterOptions.instance.show();
			}
		});
			
		this.findSeedsButton = ButtonMaker.make(2, 2, 320, 40);
		this.findSeedsButton.setText("          Crack Seed          ");
		this.findSeedsButton.setDisable(true);
		this.findSeedsButton.setOnMouseClicked(event -> {
			if (this.programStage == 0) {
				this.programStage = 1;
				this.findSeedsButton.setText("            Cancel            ");
				this.update();
				DungeonCracker.results.startTask();
			}
			else if (this.programStage == 1){
				this.programStage = 0;
				this.findSeedsButton.setText("          Crack Seed          ");
				this.update();
				DungeonCracker.results.stopTask();
			}
			else if (this.programStage == 2){
				this.programStage = 0;
				this.findSeedsButton.setText("          Crack Seed          ");
				this.update();
				DungeonCracker.results.reset();
			}
		});
	}

	private void updateVersion() {
		this.versionButton.setText(" MC " + this.version.toString() + " ");
	}

	public void addToPane(StackPane pane) {
		pane.getChildren().addAll(this.versionButton, this.biomeButton, this.lootFilterButton, this.findSeedsButton);
	}

	public void update() {
		boolean flag = (DungeonCracker.spawnerCoords.parse() && this.programStage == 0) || this.programStage != 0;
		this.findSeedsButton.setDisable(!flag);
	}
	
	public void resetMode() {
		this.findSeedsButton.setText("         Reset Cracker         ");
		this.programStage = 2;
	}
	
	public enum BiomeType {
		NOTDESERT(" Biome: Not Desert "),
		DESERT(" Biome: Desert "),
		UNKNOWN(" Biome: Unknown ");
		
		public String displayName;
		private BiomeType(String s) {
			this.displayName = s;
		}
	}
}
