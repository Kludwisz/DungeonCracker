package kludwisz.gui;

import java.util.Objects;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

public class Floor {

	public static Image UNKNOWN_SOLID;
	public static Image UNKNOWN;
	public static Image MOSSY;
	public static Image COBBLE;
	public static Image AIR;

	public static Image[] IMAGES;

	static {
		try {
			UNKNOWN_SOLID = new Image(Objects.requireNonNull(Floor.class.getClassLoader().getResourceAsStream("unknown_solid.png")));
			UNKNOWN = new Image(Objects.requireNonNull(Floor.class.getClassLoader().getResourceAsStream("unknown.png")));
			MOSSY = new Image(Objects.requireNonNull(Floor.class.getClassLoader().getResourceAsStream("mossy.png")));
			COBBLE = new Image(Objects.requireNonNull(Floor.class.getClassLoader().getResourceAsStream("cobble.png")));
			AIR = new Image(Objects.requireNonNull(Floor.class.getClassLoader().getResourceAsStream("air.png")));
			IMAGES = new Image[] {MOSSY, COBBLE, AIR, UNKNOWN, UNKNOWN_SOLID,};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ImageView[][] floorPattern = new ImageView[9][9];
	public final Button floorSizeButton;
	private final Button clearFloor;
	public FloorSize floorSize = FloorSize._9x9;

	public Floor() {
		this(UNKNOWN_SOLID);
	}

	public Floor(Image defaultImage) {
		for (int i = 0; i < this.floorPattern.length; i++) {
			for (int j = 0; j < this.floorPattern[i].length; j++) {
				ImageView imageView = new ImageView(defaultImage);
				imageView.setScaleX(0.125D);
				imageView.setScaleY(0.125D);
				imageView.setTranslateX(65 * j - 1280.0D / 2.0D + 104.0D);
				imageView.setTranslateY(65 * i - 720.0D / 2.0D + 100.0D);

				imageView.setOnMouseClicked(event -> {
					int k = indexOf(imageView.getImage());
					k -= event.getButton().ordinal() - 2;
					imageView.setImage(IMAGES[Math.floorMod(k, IMAGES.length)]);
				});

				imageView.setOnMouseEntered(event -> {
					imageView.setScaleX(0.13D);
					imageView.setScaleY(0.13D);
				});

				imageView.setOnMouseExited(event -> {
					imageView.setScaleX(0.125D);
					imageView.setScaleY(0.125D);
				});

				this.floorPattern[i][j] = imageView;
			}
		}
		
		this.floorSizeButton = ButtonMaker.make(2, 2, 200, -280);
		this.floorSizeButton.setOnMouseClicked(event -> {
			this.floorSize = this.floorSize.next();
			this.updateFloorSize(false);
		});
		this.updateFloorSize(true);
		
		this.clearFloor = ButtonMaker.make(2, 2, 480, -280);
		this.clearFloor.setText(" Clear Floor Pattern ");
		this.clearFloor.setOnMouseClicked(event -> {
			for (int i = 0; i < this.floorPattern.length; i++) {
				for (int j = 0; j < this.floorPattern[i].length; j++) {
					this.floorPattern[i][j].setImage(UNKNOWN_SOLID);
				}
			}
			this.floorSize = FloorSize._9x9;
			this.updateFloorSize(false);
		});
	}

	private void updateFloorSize(boolean init) {
		this.floorSizeButton.setText("    Floor Size: " + this.floorSize.toString() + "    ");

		for (int i = 0; i < this.floorPattern.length; i++) {
			for (int j = 0; j < this.floorPattern[i].length; j++) {
				ImageView imageView = this.floorPattern[i][j];
				imageView.setVisible(this.floorSize.test(j, i));
			}
		}
	}

	public static int indexOf(Image image) {
		for (int i = 0; i < IMAGES.length; i++) {
			if (IMAGES[i] == image) return i;
		}

		return -1;
	}

	public void addToPane(StackPane pane) {
		for (int i = 0; i < this.floorPattern.length; i++) {
			for (int j = 0; j < this.floorPattern[i].length; j++) {
				ImageView imageView = this.floorPattern[i][j];
				pane.getChildren().add(imageView);
			}
		}

		Label north = new Label("North (-Z)");
		north.setFont(Font.font(30.0D));
		north.setTranslateX(-270.0D);
		north.setTranslateY(-320.0D);

		Label south = new Label("South (+Z)");
		south.setFont(Font.font(30.0D));
		south.setTranslateX(-270.0D);
		south.setTranslateY(320.0D);

		Label west = new Label("West (-X)");
		west.setFont(Font.font(30.0D));
		west.getTransforms().add(new Rotate(-90));
		west.setTranslateX(-560.0D);
		west.setTranslateY(80.0D);

		Label east = new Label("East (+X)");
		east.setFont(Font.font(30.0D));
		east.getTransforms().add(new Rotate(90));
		east.setTranslateX(135.0D);
		east.setTranslateY(-40.0D);

		pane.getChildren().addAll(this.floorSizeButton, this.clearFloor);
		pane.getChildren().addAll(north, south, west, east);
	}
	
	public String getSequence() {
		String seq = "";
		int xMin = this.floorSize == FloorSize._7x7 || this.floorSize == FloorSize._7x9 ? 1 : 0;
		int zMin = this.floorSize == FloorSize._7x7 || this.floorSize == FloorSize._9x7 ? 1 : 0;
		int xMax = this.floorSize == FloorSize._7x7 || this.floorSize == FloorSize._7x9 ? 8 : 9;
		int zMax = this.floorSize == FloorSize._7x7 || this.floorSize == FloorSize._9x7 ? 8 : 9;
		//System.out.println(xMax + " " + zMax);
		
		for (int x=xMin; x<xMax; x++) 
			for (int z=zMin; z<zMax; z++) {
				ImageView imageView = this.floorPattern[z][x];
				int ix = indexOf(imageView.getImage());
			seq += Integer.toString(ix);	
		}
		
		//System.out.println(seq);
		return seq;
	}
}
