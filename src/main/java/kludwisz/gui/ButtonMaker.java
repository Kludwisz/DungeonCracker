package kludwisz.gui;

import javafx.scene.control.Button;

public class ButtonMaker {
	public static Button make(double scalex, double scaley, double translatex, double translatey) {
		Button b = new Button();
		b.setScaleX(scalex);
		b.setScaleY(scaley);
		b.setTranslateX(translatex);
		b.setTranslateY(translatey);
		return b;
	}
}
