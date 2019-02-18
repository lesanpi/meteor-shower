package e.equipo.probando;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Meteor {
	public float x,y;
	public float maxW,maxH;
	public float screenW,backW;
	public float velocidad;

	public Meteor(float x,float y,float maxW,float maxH,float screenW, float backW) {
		this.maxW=maxW;
		this.maxH=maxH;
		this.x=x;
		this.y=y;
		this.screenW=screenW;
		this.backW=backW;
		velocidad=25*(maxW/1080);
	}

	public void volver() {

		x=((float) (Math.random() * maxW*0.8));
		if (x > 10) {
			y=((float) (-100));
		} else {
			x=-100;
			y=((float) (Math.random() *0.7* maxH) + 60);
		}
	}

	public void volver2() {

		x=((float) (Math.random() * 1));
		if (x > 10) {
			y=((float) (-100));
		} else {
			x=-100;
			y=((float) (Math.random() * 0.7*maxH));
		}
	}

	public void salio() {
		int aux;
		if (x > maxW || y > maxH) {
			volver();
		}

	}

	public void salio2() {
		int aux;
		if (x > maxW || y > maxH) {
			volver2();
		}

	}

	public boolean choqueM(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

		if (x + widthT >= position.getX() && x <= position.getX() + widthO
				&& y + heightT >= position.getY() && y <= position.getY() + heightO) {

			volver();
			return true;
		}

		return false;
	}
	public boolean choqueM2(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

		if (x + widthT >= position.getX() && x <= position.getX() + widthO
				&& y + heightT >= position.getY() && y <= position.getY() + heightO) {

			volver2();
			return true;
		}

		return false;
	}

	public boolean choque(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

		if (x + widthT-widthT/3 >= position.getX() && x <= position.getX() + widthO
				&& y + heightT-heightT/3 >= position.getY() && y <= position.getY() + heightO) {

			volver();
			return true;
		}

		return false;
	}
	public boolean choque2(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

		if (x + widthT-widthT/3 >= position.getX() && x <= position.getX() + widthO
				&& y + heightT-heightT/3 >= position.getY() && y <= position.getY() + heightO) {

			volver2();
			return true;
		}

		return false;
	}

	public void update() {
		x+=velocidad;
		y+=velocidad;


	}
	public void setVelocidadLenta(){
		velocidad=15*(maxW/1080);

	}
	public void setVelocidadNormal(){
		velocidad=25*(maxW/1080);
	}


}

