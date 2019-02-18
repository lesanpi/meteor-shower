package e.equipo.probando;


public class AgujeroNegro {

    public float x, y;
    public float maxW, maxH;
    public float screenW,backW;
    public int angle=0;
    public float velocidadX,velocidadY;

    public AgujeroNegro(float x, float y, float maxW, float maxH,float screenW, float backW) {
        this.maxW = maxW;
        this.maxH = maxH;
        this.x = x;
        this.y = y;
        this.screenW=screenW;
        this.backW=backW;
        velocidadX=15*(maxW/1080);
        velocidadY=15*(maxH/1920);

    }

    public void posicionRand(){
        x= (float) (Math.random()*maxW-maxW/20);
        y= (float) (Math.random()*3*maxH/4);
    }
    public boolean choque(Vector2D position, float widthO, float heightO, float widthT, float heightT) {

        if (x + widthT >= position.getX() && x <= position.getX() + widthO
                && y + heightT >= position.getY() && y <= position.getY() + heightO) {


            return true;
        }

        return false;
    }

    public boolean choqueS(float x2, float y2, float widthO, float heightO, float widthT, float heightT) {

        if (x + widthT >= x2 && x <= x2 + widthO
                && y + heightT >= y2 && y <= y2 + heightO) {


            return true;
        }

        return false;
    }

    public void volver() {

        x = -50;
        y = (float) (Math.random() * maxH);

    }

    public void salio() {
        if (x > maxW || y > maxH) {
            volver();
        }

    }

    public void update() {
        x += velocidadX;
        if ((x > maxW / 4 && x < maxW / 2) || (x > maxW * 3 / 4 && x < maxW)) y += velocidadY;
        else y -= velocidadY;
        salio();
    }

    public void setVelocidadLentaXY(){
        velocidadY=10*(maxH/1920);
        velocidadX=10*(maxW/1080);
    }
    public void setVelocidadNormalXY(){
        velocidadX=15*(maxW/1080);
        velocidadY=15*(maxH/1920);

    }


}
