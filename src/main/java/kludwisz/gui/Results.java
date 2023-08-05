package kludwisz.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kludwisz.cracker.TaskOutput;
import kludwisz.cracker.ReverseDungeonTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Results {
	private static final Font MONO = Font.font("Monospace", FontWeight.BOLD , 26);
	private final Label [] labels = new Label[4];
	private final Button [] copyButtons = new Button[3];
	private final Button saveResults;
	private TaskOutput out;
	
	public Results() {
		for (int i=0; i<4; i++) {
			this.labels[i] = new Label("");
			Label label = this.labels[i];
			if (i!=0)
				label.setVisible(false);
			label.setFont(MONO);
			label.setTranslateY(i * 50.0D + 100.0D);
			if (i!=0)
				label.setTranslateX(250.0D);
			else 
				label.setTranslateX(320.0D);
			
			if (i!=3) {
				this.copyButtons[i] = ButtonMaker.make(2, 2, 550, i * 50.0D + 150.0D);
				this.copyButtons[i].setVisible(false);
				this.copyButtons[i].setText(" Copy seeds ");
				
				final int index = i;
				this.copyButtons[i].setOnMouseClicked((event) -> {
					List<Long> seeds = this.out.get(index);
					Clipboard cb = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					String s="";
					for (long seed : seeds) {
						s += Long.toString(seed)+"\n";
					}
					content.putString(s);
					cb.setContent(content);
				});
			}
		}
		
		this.saveResults = new Button("  Save all seeds as...  ");
		this.saveResults.setScaleX(2.0D);
		this.saveResults.setScaleY(2.0D);
		this.saveResults.setTranslateX(320.0D);
		this.saveResults.setTranslateY(320.0D);
		this.saveResults.setVisible(false);
		
		this.saveResults.setOnMouseClicked(event -> {
			File file = this.getSaveLocation();
			if (file != null) {
				try {
					FileWriter fout = new FileWriter(file);
					fout.append("Dungeon Seeds:\n\n");
					for (long seed : this.out.dungeonSeeds) {
						fout.append(Long.toString(seed) + "\n");
					}
					fout.append("\nStructure Seeds:\n\n");
					for (long seed : this.out.structureSeeds) {
						fout.append(Long.toString(seed) + "\n");
					}
					fout.append("\nRandom World Seeds:\n\n");
					for (long seed : this.out.worldSeeds) {
						fout.append(Long.toString(seed) + "\n");
					}
					fout.close();
					this.saveResults.setText("  File saved.  ");
					this.saveResults.setDisable(true);
				} 
				catch (IOException ex) {}
			}
		});
	}
	
	public void addToPane(StackPane pane) {
		pane.getChildren().addAll(this.labels);
		pane.getChildren().addAll(this.copyButtons);
		pane.getChildren().add(saveResults);
	}
	
	private ReverseDungeonTask task = null;
	private ExecutorService exec = null;
	public void startTask() {
		if (task != null)
			return;
		task = new ReverseDungeonTask(	DungeonCracker.spawnerCoords.x, 
										DungeonCracker.spawnerCoords.y,
										DungeonCracker.spawnerCoords.z,
										DungeonCracker.genOptions.version,
										DungeonCracker.genOptions.biome);
		
		this.labels[0].setVisible(true);
		this.labels[0].textProperty().bind(task.messageProperty());
		
		task.setOnSucceeded((event) -> {
			if (task != null) {
				this.out = task.getValue();
				
				if (this.out != null) {
					int [] s = this.out.size;
					this.labels[1].setText("Dungeon seeds:      " + s[0] + "" + align(s[0], Math.max(s[1], s[2])));
					this.labels[2].setText("Structure seeds:    " + s[1] + "" + align(s[1], Math.max(s[1], s[2])));
					this.labels[3].setText("Random world seeds: " + s[2] + "" + align(s[2], Math.max(s[1], s[2])));
					this.setAllVisible(true);
					DungeonCracker.genOptions.resetMode();
				}
			}
		});
		
		exec = Executors.newFixedThreadPool(1);
		exec.execute(task);
		exec.shutdown();
	}

	public void stopTask() {
		if (task == null || exec == null)
			return;
		exec.shutdownNow();
		task = null;
		exec = null;
		this.out = null;
		this.setAllVisible(false);
	}
	
	private void setAllVisible(boolean val) {
		for (int i=0; i<4; i++) {
			this.labels[i].setVisible(val);
			if (i!=3)
				this.copyButtons[i].setVisible(val);
		}
		this.saveResults.setVisible(val);
	}
	
	// simple method for aligning text in app
	private static String align(int a, int b) {
		int lenA = Integer.toString(a).length();
		int lenB = Integer.toString(b).length();

		String res = "";
		for (int i=0; i<lenB-lenA; i++) {
			res += " ";
		}
		return res;
	}
	
	private File getSaveLocation() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		chooser.setAcceptAllFileFilterUsed(false);
		
		int result = chooser.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		else return null;
	}
	
	public void reset() {
		this.stopTask();
		this.saveResults.setText("  Save all seeds as...  ");
		this.saveResults.setDisable(false);
	}
}
