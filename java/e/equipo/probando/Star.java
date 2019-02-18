package e.equipo.probando;


public class Star {

	public float x, y;
	public float maxW, maxH;
	public float screenW,backW;
	public float velocidadY,velocidadX;

	public Star(float x, float y, float maxW, float maxH,float screenW, float backW) {
		this.maxW = maxW;
		this.maxH = maxH;
		this.x = x;
		this.y = y;
		this.screenW=screenW;
		this.backW=backW;
		velocidadX=27*(maxW/1080);
		velocidadY=17*(maxW/1080);
	}


	public void update() {
		x += velocidadX;
		if ((x > maxW / 4 && x < maxW / 2) || (x > maxW * 3 / 4 && x < maxW)) y += velocidadY;
		else y -= velocidadY;
		salio();
	}


	public void volver() {

		x = -30;
		y = (float) (Math.random() * maxH);

	}

	public void salio() {
		if (x > maxW || y > maxH) {
			volver();
		}

	}

	public boolean choque(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

		if (x + widthT >= position.getX() && x <= position.getX() + widthO
				&& y + heightT >= position.getY() && y <= position.getY() + heightO) {

			volver();
			return true;
		}

		return false;
	}

	public void setVelocidadLentaXY(){
		velocidadY=7*(maxH/1080);
		velocidadX=17*(maxW/1080);
	}
	public void setVelocidadNormalXY(){
		velocidadY=17*(maxW/1080);
		velocidadX=27*(maxW/1080);
	}

}
