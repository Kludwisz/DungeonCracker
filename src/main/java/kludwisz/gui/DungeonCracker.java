package kludwisz.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DungeonCracker extends Application {

	public static Floor floor = new Floor();
	public static SpawnerCoords spawnerCoords = new SpawnerCoords();
	public static GenerationOptions genOptions = new GenerationOptions();
	public static Results results = new Results();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		StackPane pane = new StackPane();
		stage.getIcons().add(new Image(DungeonCracker.class.getResourceAsStream("/dungeonCrackerIcon.png")));
		stage.setTitle("Kludwisz's Updated Dungeon Cracker");
		stage.setResizable(false);

		Scene scene = new Scene(pane, 1280, 720);
        
		scene.getStylesheets().add("darkmode2.css");

		floor.addToPane(pane);
		spawnerCoords.addToPane(pane);
		genOptions.addToPane(pane);
		results.addToPane(pane);
		LootFilterOptions.instance = new LootFilterOptions();
		
		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest(event -> {
			if (LootFilterOptions.instance.isShowing())
				LootFilterOptions.instance.close();
		});
	}

}
