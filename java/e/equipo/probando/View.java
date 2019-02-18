package e.equipo.probando;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.os.Handler;
import android.graphics.Matrix;

public class View extends SurfaceView implements Runnable{

    private SparseArray<PointF> mActivePointers;
    private SparseArray<PointF> mActivePointers2;

    // Todo el código correra separado a la UI
    private Thread m_Thread = null;
    // Esta variable determina cuando el juego está corriendo
    // Es declarada como volátil porque
    // puede ser accesada desde dentro y fuera del hilo
    private volatile boolean m_Playing;

    // Esta es sobre la que trazamos
    private Canvas m_Canvas;
    // Esta es requerida por la clase Canvas para hacer el trazo
    private SurfaceHolder m_Holder;
    // Esta nos deja controlar colores etc
    private Paint m_Paint;

    // Esta será una referencia a la Activity
    private Context m_context;
    private final Handler handler = new Handler();
    // Sonido
    private SoundPool m_SoundPool;
    private int m_dead_sound = -1;
    private int m_laser_sound = -1;
    private int m_laser2_sound = -1;
    private int m_star_sound = -1;
    private int m_globo_sound = -1;
    private int m_up_sound = -1;
    private int m_lose_sound = -1;
    private int m_rock_sound = -1;
    private int m_fondo_sound = -1;
    private int m_tele_sound = -1;


    // Para seguir movimiento m_Direction
    // Cual es la resolución de la pantalla
    public int m_ScreenWidth;
    public int m_ScreenHeight;

    // Controla la pausa entre actualizaciones
    private long m_NextFrameTime;
    // Actualiza el juego 10 veces por segundo
    private final long FPS = 10;
    // Hay 1000 milisegundos en un segundo
    private final long MILLIS_IN_A_SECOND = 1000;
    // Trazaremos el marco mucho más seguido
    private long time;
    private long lastTime;

    Power globoV;
    public boolean salVida=false;
    Bitmap GloboVida;
    private long timeV;

    Power globo2x;
    public boolean sal2x=false;
    Bitmap Globo2x;
    private long time2x;
    public int time2xA;

    Power globoP;
    public boolean salPower=false;
    Bitmap GloboPower;
    private long timeU;
    public int timePow;

    public boolean salSlow = false;
    public int timeSlow=0;
    public boolean slowly =false;
    Power globoSlow;

    Bitmap star;
    Star estrella;
    Bitmap background;
    Bitmap L,R,ACC,LAS,corazon,logo,tap,pause,play,mute,nomute;
    Bitmap meteor,meteorM,meteorG,meteorM2,meteorG2;
    Meteor m1;
    Meteor m2;
    Meteor m3;
    Meteor m4;
    Meteor m5;
    Meteor m6;
    AgujeroNegro agujeroNegro;
    public Bitmap agujeroN;
    int skin;
    Bitmap player,playerPower;
    Bitmap speed;
    Bitmap blueLaser;

    public Bitmap numberU;
    public Bitmap numberD;
    public Bitmap numberC;
    public Bitmap numberUS;
    public Bitmap numberDS;
    public Bitmap numberCS;
    public Bitmap joystick;


    public Bitmap gameOver;
    public Bitmap expbar;
    public Bitmap againorback;
    public Bitmap top;
    public Bitmap starlevel;
    public Bitmap x2ac;
    Bitmap pp;

    public Bitmap gameOver2;
    public Bitmap expbar2;
    public Bitmap againorback2,ayudaBoton,ayudaBoton2;
    public Bitmap top2;
    public Bitmap starlevel2;
    public Bitmap x2ac2;
    public Bitmap pp2;
    public Bitmap L2,R2,ACC2,LAS2,corazon2,logo2,tap2,pause2,play2,mute2,nomute2,joystick2,mandoClassic,mandoClassic2,mandoAlt,mandoAlt2,menuAyuda;
    public Bitmap star2,meteor2;
    Bitmap GloboVida2;
    Bitmap Globo2x2;
    Bitmap GloboPower2;
    Bitmap player2,playerPower2,agujeroN2;
    Bitmap speed2;
    Bitmap blueLaser2;


    Matrix atL,atL2;
    Matrix atAgujero;
    public boolean left;
    public boolean right;
    public boolean acc;
    public boolean shoot;
    public boolean laser;
    public boolean power;
    public boolean x2;
    public boolean ayuda=false;
    public Bitmap slow;
    public Bitmap slow2;
    public Matrix at;
    public float angle;
    public float angleLaser;
    public int vida=3;


    public int unit;
    public int dec;
    public int cent;
    public int unitS;
    public int decS;
    public int centS;
    public int level;
    public int experience;

    private final double DELTAANGLE = 10;
    public Vector2D velocity;
    public Vector2D position;
    public Vector2D positionLaser;
    public Vector2D velocityLaser;
    public Vector2D positionLaser2;
    boolean portada=true;
    SharedPreferences myPreferences;
    SharedPreferences.Editor editor;
    public int volumen=1;
    int sound=0;
    int musicR=0;
    float volR=0;
    int timeMeteor=0;
    int timeNegro=0;
    int timeMusic=0;
    boolean agujeroAct=false;
    int modoControl = 0;
    int timeSlowly=0;
    int musicRock=0;


    public View(Context context, Point size,SharedPreferences myPref,SharedPreferences.Editor edit) {
        super(context);

        mActivePointers = new SparseArray<PointF>();
        mActivePointers2 = new SparseArray<PointF>();
        myPreferences= myPref;
        editor=edit;
        m_context = context;
        m_ScreenWidth = size.x;
        m_ScreenHeight = size.y;



        //Determinar el tamaño de cada bloque/lugar en el tablero de juego

        // Establece el sonido
        loadSound();

        // Inicializa los objetos de trazado
        m_Holder = getHolder();
        m_Paint = new Paint();

        background=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.background);
        menuAyuda=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.menuayuda);

        star=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.star);
        estrella=new Star(0,(float)Math.random()*m_ScreenHeight,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        L=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.bottonl);
        R=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.bottonr);
        ACC=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.bottonacc);
        LAS=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.botonlas);
        corazon=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.corazon);
        pause=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.pausec);
        play=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.playc);
        againorback=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.again_or_back);
        logo=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.logo_meteorshower_g);
        tap=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tap);
        pp=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.portadaapp3);
        GloboVida=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.vida);
        gameOver=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.gameover);
        expbar=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.expbar);
        x2ac=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.doublep);
        slow=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.reloj);
        ayudaBoton=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ayuda);
        ayudaBoton2=mutableBMP(ayudaBoton);

        slow2=mutableBMP(slow);
        mute=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.mute);
        nomute=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.sound);
        joystick=BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.joystick);
        top=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.top);
        globoV=new Power(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        agujeroN = BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.agujeronegro);
        mandoClassic = BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.mandojuego);
        mandoAlt = BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.mandojuego2);
        mandoClassic2=mutableBMP(mandoClassic);
        mandoAlt2=mutableBMP(mandoAlt);

        GloboPower=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.power);
        globoP=new Power(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());

        globoSlow=new Power(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());

        agujeroNegro = new AgujeroNegro(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());

        Globo2x=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.doublep);
        globo2x=new Power(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());

        meteor = BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.meteoro3n);
        meteorG = BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.meteoro3m);
        meteorM = BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.meteoro3mp);
        meteorM2=mutableBMP(meteorM);
        meteorG2=mutableBMP(meteorG);
        m1= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        m2= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        m3= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        m4= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        m5= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());
        m6= new Meteor(0,0,m_ScreenWidth,m_ScreenHeight,m_ScreenWidth,background.getWidth());

        player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navelouis);
        playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navelouis2);
        speed = BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.fire08);
        blueLaser = BitmapFactory.decodeResource(getResources(),e.equipo.probando.R.drawable.laserblue01);


        modoControl=myPreferences.getInt("modoControl",0);
        skin=myPreferences.getInt("SKIN", 0);
        unit=0;
        dec=0;
        cent=0;
        unitS=myPreferences.getInt("UnitH", 0);
        decS=myPreferences.getInt("DecH", 0);
        centS=myPreferences.getInt("CentH", 0);
        atL= new Matrix();
        atL2= new Matrix();
        at= new Matrix();
        atAgujero=new Matrix();
        angle=270;

        level=myPreferences.getInt("Level",1);;
        experience=myPreferences.getInt("Exp",0);

        left=right=acc=shoot=laser=false;
        velocity=new Vector2D(0,0);
        position=new Vector2D((m_ScreenWidth-player.getWidth())/2,m_ScreenHeight-3*player.getHeight());
        positionLaser=new Vector2D();
        velocityLaser=new Vector2D(0,0);
        positionLaser2=new Vector2D();
        time = 0;
        timeU = 0;
        timeV = 0;
        timePow=0;
        time2x=0;
        lastTime = System.currentTimeMillis();

        star2=mutableBMP(star);
        L2=mutableBMP(L);
        R2=mutableBMP(R);
        ACC2=mutableBMP(ACC);
        LAS2=mutableBMP(LAS);
        corazon2=mutableBMP(corazon);
        pause2=mutableBMP(pause);
        play2=mutableBMP(play);
        againorback2=mutableBMP(againorback);
        logo2=mutableBMP(logo);
        tap2=mutableBMP(tap);
        GloboVida2=mutableBMP(GloboVida);
        gameOver2=mutableBMP(gameOver);
        expbar2=mutableBMP(expbar);
        top2=mutableBMP(top);
        x2ac2=mutableBMP(x2ac);
        player2= mutableBMP(player);
        playerPower2= mutableBMP(playerPower);
        speed2 = mutableBMP(speed);
        blueLaser2 = mutableBMP(blueLaser);
        GloboPower2= mutableBMP(GloboPower);
        Globo2x2=mutableBMP(Globo2x);
        meteor2 = mutableBMP(meteor);
        mute2=mutableBMP(mute);
        nomute2=mutableBMP(nomute);
        pp2=mutableBMP2(pp);
        joystick2=mutableBMP(joystick);
        Punto CentroJ = new Punto (joystick2.getWidth(),m_ScreenHeight*7/8-joystick2.getHeight()/2);

        agujeroN2=mutableBMP2(agujeroN);
        // Empezar el juego
        startGame();
    }

    @Override
    public void run() {
        // El chequeo de m_Playing previene un crash al inicio
        // Podrías además extender el código para proveer una característica de pausa

        while (m_Playing) {

            while(portada) {
                if (checkForUpdate()) {

                    estrella.update();
                    m1.update();
                    m2.update();
                    m3.update();
                    m4.update();
                    m5.update();
                    m6.update();


                    if(timeMusic%25==0)  m_SoundPool.play(m_fondo_sound,volumen,volumen,0, 0, 0);
                    timeMusic+=1;
                    m1.salio();
                    m2.salio2();
                    m3.salio();
                    m4.salio2();
                    m5.salio();
                    m6.salio2();
                    drawPortada();
                }
                time = 0;
                timeU = 0;
                timeV = 0;


            }

            // Actualiza 10 veces por segundo
            if (checkForUpdate()) {
                updateGame();
                drawGame();


            }

        }

    }

    public void pause() {
        m_Playing = false;
        try {
            m_Thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        m_Playing = true;
        m_Thread = new Thread(this);
        m_Thread.start();
    }

    public void startGame() {
       // Establece m_NextFrameTime para que una actualización sea provocada inmediatamente
        m_NextFrameTime = System.currentTimeMillis();
    }

    public void loadSound() {
        m_SoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            // Crear objetos de las 2 clases requeridas
            // Usa m_Context porque esto es una referencia a la Activity
            AssetManager assetManager = m_context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepara los dos sonidos en memoria

            descriptor = assetManager.openFd("globo.wav");
            m_globo_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("laser_sound.wav");
            m_laser_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("laser2.wav");
            m_laser2_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("star.wav");
            m_star_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerup.wav");
            m_up_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerup.wav");
            m_up_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("lose.wav");
            m_lose_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("death_sound.wav");
            m_dead_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("rocking2.wav");
            m_rock_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("teletrans.wav");
            m_tele_sound = m_SoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("");
            m_fondo_sound = m_SoundPool.load(descriptor, 0);




        } catch (IOException e) {
            // Error
        }
    }

    boolean musicFondo=true;

    public void updateGame(){

        if(m_Playing) time += System.currentTimeMillis() - lastTime;
        if(m_Playing) timeMeteor += 100;

        if(m_Playing && slowly) timeSlowly+=100;
        if(!portada && m_Playing && !slowly && level>=5) timeSlow +=100;
        if(!portada && m_Playing) timeNegro +=100;
        if(vida<=2 && !portada && m_Playing)timeV += 100;
        if(!power && !portada && m_Playing)timeU +=100;
        if(!x2 && !portada && m_Playing && level>=4)time2x += 100;
        if(m_Playing && (timeMusic%25==0 || timeMusic==0) && !power)  m_SoundPool.play(m_fondo_sound,volumen,volumen,0, 0, 0);
        if(m_Playing) timeMusic ++;
        if(power){

            if(m_Playing) {

                timePow += 100;
                musicR++;
                if (musicR == 11){
                    m_SoundPool.play(m_rock_sound,volumen,volumen,0, 10, 2);

                }

            }


        }
        if(timeSlow==30000){
            salSlow=true;
        }
        if(timeSlow==45000 && !slowly){
            salSlow=false;
            timeSlow=15000;
        }


        if(x2 && m_Playing){
            time2xA += 100;
        }
        lastTime = System.currentTimeMillis();

        if(timeMeteor==800){
            m1.volver();
            m2.volver2();
            m3.volver();
            m4.volver2();
            m5.volver();
            m6.volver2();

        }

        if(shoot && time > 750){
            angleLaser=angle;
            laser=true;
            anglesLaser(angleLaser);
            if(level<3) {
                positionLaser.setY((float) position.getY() + player2.getHeight() / 2);
                positionLaser.setX((float) position.getX() + player2.getWidth() / 2 - 10);
                m_SoundPool.play(m_laser_sound, volumen, volumen, 0, 0, 1);
            } else {

                positionLaser.setY((float)position.getY() + player2.getHeight() / 2 );
                positionLaser.setX((float)position.getX() + player2.getWidth() / 2 - 2*blueLaser2.getWidth());
                positionLaser2.setY((float) position.getY() + player2.getHeight() / 2 );
                positionLaser2.setX((float)position.getX() + player2.getWidth() / 2 + blueLaser2.getWidth());

                m_SoundPool.play(m_laser_sound, volumen, volumen, 0, 0, 1);
                m_SoundPool.play(m_laser2_sound, volumen, volumen, 0, 0, 1);
            }
            time = 0;
        }
        if(right){
            angle += DELTAANGLE;
            if (angle > 360) angle = 10;
        }
        if(left){
            angle -= DELTAANGLE;
            if (angle < 0) angle = 350;
        }
        if(acc){
            angles(angle);
            position = position.add(velocity.scale(1.1));
        }

        if(timeV > 15000) salVida=true;

        if(timeNegro==25000 && level>=2){
            agujeroAct=true;
            agujeroNegro.posicionRand();
            if(vida>0)m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 0);

        }

        if(!musicFondo) m_SoundPool.pause(m_fondo_sound);

        if(timePow>25000){

            timeU=0;
            timeMusic=24;
            timePow=0;
            volR=0;
            musicR=0;
            power=false;
            musicFondo=true;

        }
        if(timePow==24000){
            m_SoundPool.stop(m_rock_sound);
            m_SoundPool.play(m_up_sound, volumen, volumen, 0, 0, 1);
        }


        if(time2xA>12000){
            time2x=0;
            time2xA=0;
            x2=false;
        }

        agujeroNegro.angle++;
        if(level>=4) agujeroNegro.update();



        if(agujeroAct && agujeroNegro.choque(position,player2.getWidth(),player2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            position.setX((float) (Math.random()*m_ScreenWidth-10));
            position.setY((float)Math.random()*m_ScreenHeight-2*L2.getHeight());
            if(level<4){
                agujeroAct=false;
                timeNegro = (3000);
            }
            m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 2);

        }

        if(agujeroAct && agujeroNegro.choqueS(estrella.x,estrella.y,star2.getWidth(),star2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            estrella.x = (float) (Math.random()*m_ScreenWidth-10);
            estrella.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (20000);
            }
            m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 3);

        }
        if(agujeroAct && agujeroNegro.choqueS(globo2x.x,globo2x.y,Globo2x2.getWidth(),Globo2x2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            globo2x.x = (float) (Math.random()*m_ScreenWidth-10);
            globo2x.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (15000);
            }
            if(vida>0)m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 2);

        }
        if(agujeroAct && agujeroNegro.choqueS(globoP.x,globoP.y,GloboPower2.getWidth(),GloboPower2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            globoP.x = (float) (Math.random()*m_ScreenWidth-10);
            globoP.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (15000);
            }
            if(vida>0)m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 2);

        }
        if(agujeroAct && agujeroNegro.choqueS(globoSlow.x,globoSlow.y,slow2.getWidth(),slow2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            globoSlow.x = (float) (Math.random()*m_ScreenWidth-10);
            globoSlow.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (15000);
            }
            if(vida>0)m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 2);

        }
        if(agujeroAct && agujeroNegro.choqueS(globoV.x,globoV.y,GloboVida2.getWidth(),GloboVida2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){

            globoV.x = (float) (Math.random()*m_ScreenWidth-10);
            globoV.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (15000);
            }

            if(vida>0)m_SoundPool.play(m_tele_sound,volumen,volumen,0, 0, 2);

        }


        meteoroAgujero(m1);
        meteoroAgujero(m2);
        meteoroAgujero(m3);
        meteoroAgujero(m4);
        meteoroAgujero(m5);
        meteoroAgujero(m6);


        if(timeNegro==35000){
            agujeroNegro.posicionRand();
        }

        if(timeNegro>=45000){
            agujeroAct=false;
            timeNegro=0;
        }


        if(time2x>35000) sal2x=true;

        if(timeU > 25000) salPower=true;

        estrella.update();
        if (timeMeteor>=750) {
            m1.update();
            m2.update();
            m3.update();
            m4.update();
            m5.update();
            m6.update();
        }
        m1.salio();
        m2.salio2();
        m3.salio();
        m4.salio2();
        m5.salio();
        m6.salio2();

        if (position.getX() > m_ScreenWidth)
            position.setX(1);
        if (position.getY() > m_ScreenHeight)
            position.setY(1);

        if (position.getX() < 0)
            position.setX(m_ScreenWidth);
        if (position.getY() < 0)
            position.setY(m_ScreenHeight);


        if(!acc){
            angles(angle);
            position = position.add(velocity.scale(0.6));
        }
        positionLaser = positionLaser.add(velocityLaser.scale(3));
        positionLaser2 = positionLaser2.add(velocityLaser.scale(3));




        if(estrella.choque(position,player2.getWidth(),player2.getHeight(),star2.getWidth(),star2.getHeight()) && vida>0){
            m_SoundPool.play(m_star_sound, volumen, volumen, 0, 0, 1);


            if(x2) unit+=2;
            else unit++;
            if (level<5)experience++;
            editor.putInt("Level",level);
            editor.putInt("Exp",experience);
            editor.commit();
            Marcador();
        }






        if(level >=3 && m1.choqueM(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }else if( m1.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorG2.getWidth(),meteorG2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }
        if(level >=3 && m2.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }else if( m2.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorG2.getWidth(),meteorG2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }
        if( m3.choqueM(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }
        if(level>=2 && m4.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }else if( m4.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }
        if( m5.choqueM(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }
        if(level>=2 && m6.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }else if( m6.choqueM2(positionLaser,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight())){
            positionLaser.setX(-10000);
            positionLaser.setY(-10000);
        }

        if( m1.choqueM(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }
        if(level >=3 && m2.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }else if(m2.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorG2.getWidth(),meteorG2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }
        if( m3.choqueM(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }
        if(level>=2 && m4.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }else if(m4.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }
        if( m5.choqueM(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }
        if(level>=2 && m6.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteor2.getWidth(),meteor2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }else if(level>=2 && m6.choqueM2(positionLaser2,blueLaser2.getWidth()+5,blueLaser2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight())){
            positionLaser2.setX(-10000);
            positionLaser2.setY(-10000);
        }


        m1.salio();
        m2.salio2();
        m3.salio();
        m4.salio2();
        m5.salio();
        m6.salio2();


        if(level>=3 && m1.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
           if(vida>1) m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }else if(m1.choque(position,player2.getWidth(),player2.getHeight(),meteorG2.getWidth(),meteorG2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }
        if(level>=3 && m2.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }else if(m2.choque(position,player2.getWidth(),player2.getHeight(),meteorG2.getWidth(),meteorG2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }
        if( m3.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
            if(vida>1)if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }
        if(level>=2 && m4.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        } else if(m4.choque(position,player2.getWidth(),player2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }
        if( m5.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }
        if(level>=2 && m6.choque(position,player2.getWidth(),player2.getHeight(),meteor2.getWidth(),meteor2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }else if( m6.choque(position,player2.getWidth(),player2.getHeight(),meteorM2.getWidth(),meteorM2.getHeight()) && !power){
            if(vida>1)m_SoundPool.play(m_dead_sound, volumen, volumen, 0, 0, 1);
            vida=vida-1;
        }





        if(salVida) {
            globoV.update();
            if(globoV.choque(position,player2.getWidth(),player2.getHeight(),GloboVida2.getWidth(),GloboVida2.getHeight()) && vida>0){
                m_SoundPool.play(m_globo_sound, volumen, volumen, 0, 0, 1);

                salVida=false;
                timeV=0;
                vida+=1;

            }
        }
        if(salPower) {
            globoP.update();
            if(globoP.choque(position,player2.getWidth(),player2.getHeight(),GloboPower2.getWidth(),GloboPower2.getHeight()) && vida>0) {
                m_SoundPool.pause(m_fondo_sound);
                m_SoundPool.play(m_up_sound, volumen, volumen, 0, 0, 1);
                musicR=0;
                musicFondo=false;
                timeMusic=-100;
                salPower=false;
                timeU=0;
                if(!power) power=true;
            }
        }

        if (sal2x){
            globo2x.update();
            if(globo2x.choque(position,player2.getWidth(),player2.getHeight(),Globo2x2.getWidth(),Globo2x2.getHeight()) && vida>0){

                m_SoundPool.play(m_globo_sound, volumen, volumen, 0, 0, 1);

                time2x=0;
                if(!x2) x2=true;
                sal2x=false;
            }


        }
        if (salSlow){
            globoSlow.update();
            if(globoSlow.choque(position,player2.getWidth(),player2.getHeight(),slow2.getWidth(),slow2.getHeight()) && vida>0){

                m_SoundPool.play(m_globo_sound, volumen, volumen, 0, 0, 1);

                timeSlow=0;
                if(!slowly) slowly=true;
                salSlow=false;
            }


        }

        if(timeSlowly>=13000){
            slowly=false;
            globoP.setVelocidadNormalXY();
            globoV.setVelocidadNormalXY();
            globo2x.setVelocidadNormalXY();
            m1.setVelocidadNormal();
            m2.setVelocidadNormal();
            m3.setVelocidadNormal();
            m4.setVelocidadNormal();
            m5.setVelocidadNormal();
            m6.setVelocidadNormal();
            estrella.setVelocidadNormalXY();
            agujeroNegro.setVelocidadNormalXY();
            timeSlowly=0;
            timeSlow=0;

        }
        if(slowly){
            globoP.setVelocidadLentaXY();
            globoV.setVelocidadLentaXY();
            globo2x.setVelocidadLentaXY();
            m1.setVelocidadLenta();
            m2.setVelocidadLenta();
            m3.setVelocidadLenta();
            m4.setVelocidadLenta();
            m5.setVelocidadLenta();
            m6.setVelocidadLenta();
            estrella.setVelocidadLentaXY();
            agujeroNegro.setVelocidadLentaXY();
        }



    }

    public void drawGame() {
        // Prepara para trazar
        if (m_Holder.getSurface().isValid()) {
            m_Canvas = m_Holder.lockCanvas();
            Naves();
            m_Canvas.drawColor(Color.argb(255, 0, 0, 30));

            m_Canvas.drawBitmap(mutableBMP2(background),0,0,null);

            Matrix at1= new Matrix();
            Matrix at2= new Matrix();

            at1.setTranslate((float)position.getX() + player2.getWidth() / 2 + (5*background.getWidth()/1080),(float) position.getY() + player2.getHeight() / 2 + (10*player2.getHeight()/player.getHeight()));
            at2.setTranslate((float)position.getX() +(5*background.getWidth()/1080),(float) position.getY() + player2.getHeight() / 2 + (10*player2.getHeight()/player.getHeight()));
            at1.preRotate(angle+90, -5*background.getWidth()/1080, -10);
            at2.preRotate(angle+90, player2.getWidth() / 2 -5*background.getWidth()/1080, -10);
            m_Paint.setTextSize(60);
            if (acc) {
                if(skin==1 || skin==3 || skin==4){
                    at1.setTranslate((float)position.getX() + player2.getWidth() / 2 - mutableBMP(speed).getWidth()/2 ,(float) position.getY() + player2.getHeight() / 2 );
                    at1.preRotate(angle+90, mutableBMP(speed).getWidth()/2 , 0);

                    m_Canvas.drawBitmap(speed2, at1, null);
                } else if(skin==2){
                    at2.setTranslate((float)position.getX() +(7*background.getWidth()/1080),(float) position.getY() + player2.getHeight() / 2 + (10*player2.getHeight()/player.getHeight()));
                    at2.preRotate(angle+90, player2.getWidth() / 2 -7*background.getWidth()/1080, -10);
                    at1.setTranslate((float)position.getX() + player2.getWidth() - speed2.getWidth() - (7*player2.getWidth()/player.getWidth()),(float) position.getY() + mutableBMP(player).getHeight() / 2 + (10*player2.getHeight()/player.getHeight()) );
                    at1.preRotate(angle+90, -13*(m_ScreenWidth/1080), -10);
                    m_Canvas.drawBitmap(speed2, at1, null); //- (10*player2.getWidth()/player.getWidth())
                    m_Canvas.drawBitmap(speed2, at2, null);

                }else if(skin==0){
                    m_Canvas.drawBitmap(speed2, at1, null);
                    m_Canvas.drawBitmap(speed2, at2, null);
                }

            }

            if (salVida){
                m_Canvas.drawBitmap(GloboVida2,globoV.x, globoV.y,null);
            }

            if (salPower){
                m_Canvas.drawBitmap(GloboPower2,globoP.x, globoP.y,null);
            }

            if (sal2x){
                m_Canvas.drawBitmap(Globo2x2,globo2x.x, globo2x.y,null);
            }
            if (salSlow){
                m_Canvas.drawBitmap(slow2,globoSlow.x, globoSlow.y,null);
            }


              if(agujeroAct){
                  atAgujero.setTranslate(agujeroNegro.x,agujeroNegro.y);
                  atAgujero.preRotate(agujeroNegro.angle,agujeroN2.getWidth()/2,agujeroN2.getHeight()/2);

                  m_Canvas.drawBitmap(agujeroN2,atAgujero,null);
            }

            m_Paint.setColor(Color.argb(255, 255, 255, 255));
            m_Paint.setTextSize(70);

            at.setTranslate((long)(float) position.getX(),(long)(float) position.getY());
            at.preRotate(angle,player2.getWidth()/2,player2.getHeight()/2);

            atL.setTranslate((long)(float) positionLaser.getX(),(long)(float) positionLaser.getY());
            atL.preRotate(angleLaser+90, blueLaser2.getWidth() / 2, -5);


            if(level>=3){
                if(skin==0){
                    atL.setTranslate((long)(float) positionLaser.getX(),(long)(float) positionLaser.getY());
                    atL.preRotate(angleLaser+90,  48  ,-5 );
                    atL2.setTranslate((long)(float) positionLaser2.getX(),(long)(float) positionLaser2.getY());
                    atL2.preRotate(angleLaser+90,   -24 , -5);
                }
                if(skin==1 || skin==2 || skin==3 || skin==4){
                    atL.setTranslate((long)(float) positionLaser.getX(),(long)(float) positionLaser.getY());
                    atL.preRotate(angleLaser+90, player2.getWidth() / 2 - 15  ,-5 );
                    atL2.setTranslate((long)(float) positionLaser2.getX(),(long)(float) positionLaser2.getY());
                    atL2.preRotate(angleLaser+90,   - player2.getWidth() / 2 + 36 , -5);
                    }
            }

            m_Canvas.drawBitmap(blueLaser2,atL,null);
            if(level>=3)m_Canvas.drawBitmap(blueLaser2,atL2,null);


             Marcador();

            m_Canvas.drawBitmap(mutableBMP(numberU),m_ScreenWidth-mutableBMP(numberU).getWidth()-5,m_ScreenHeight/50,null);
            m_Canvas.drawBitmap(mutableBMP(numberD),m_ScreenWidth-mutableBMP(numberD).getWidth()-mutableBMP(numberU).getWidth()-5,m_ScreenHeight/50,null);
            m_Canvas.drawBitmap(mutableBMP(numberC),m_ScreenWidth-mutableBMP(numberC).getWidth()-mutableBMP(numberD).getWidth()-mutableBMP(numberU).getWidth()-5,m_ScreenHeight/50,null);
            m_Canvas.drawBitmap(mutableBMP(top),5,m_ScreenHeight/50,null);
            m_Canvas.drawBitmap(mutableBMP(numberUS),15+mutableBMP(numberCS).getWidth()+mutableBMP(numberDS).getWidth(),m_ScreenHeight/20,null);
            m_Canvas.drawBitmap(mutableBMP(numberDS),15+mutableBMP(numberCS).getWidth(),m_ScreenHeight/20,null);
            m_Canvas.drawBitmap(mutableBMP(numberCS),15,m_ScreenHeight/20,null);
            if(x2 && slowly){
                m_Canvas.drawBitmap(mutableBMP(x2ac),25+top.getWidth(),m_ScreenHeight/50,null);
                m_Canvas.drawBitmap(slow2,30+top.getWidth()+mutableBMP(x2ac).getWidth()+5,m_ScreenHeight/50,null);
            }
            else if(x2) m_Canvas.drawBitmap(mutableBMP(x2ac),25+top.getWidth(),m_ScreenHeight/50,null);
            else if(slowly) m_Canvas.drawBitmap(slow2,25+top.getWidth(),m_ScreenHeight/50,null);


            if(volumen==0) m_Canvas.drawBitmap(mute2,m_ScreenWidth/2+m_ScreenWidth/4-mute2.getWidth()/2 ,m_ScreenHeight/70,null);
            else if(volumen==1) m_Canvas.drawBitmap(nomute2,m_ScreenWidth/2+m_ScreenWidth/4-mute2.getWidth()/2,m_ScreenHeight/70,null);


            m_Canvas.drawBitmap(mutableBMP(star),estrella.x,estrella.y,null);
            if(vida>0){
                m_Canvas.drawBitmap(player2,at,null);
                if(modoControl==1) {
                    m_Canvas.drawBitmap(mutableBMP3(L), mutableBMP3(L).getWidth() / 2, 7 * m_ScreenHeight / 8 - mutableBMP3(L).getHeight() / 2, null);
                    m_Canvas.drawBitmap(mutableBMP3(R), 2 * mutableBMP3(L).getWidth(), 7 * m_ScreenHeight / 8 - mutableBMP3(L).getHeight() / 2, null);
                    m_Canvas.drawBitmap(ACC2, m_ScreenWidth - ACC2.getWidth() - LAS2.getWidth() - 10, m_ScreenHeight * 7 / 8 - ACC2.getHeight() / 2, null);
                    m_Canvas.drawBitmap(LAS2, m_ScreenWidth - LAS2.getWidth() - 10, m_ScreenHeight * 7 / 8 - 3 * LAS2.getHeight() / 2, null);
                }else {
                    m_Canvas.drawBitmap(L2, 0 , m_ScreenHeight / 2 - L2.getHeight() / 2, null);
                    m_Canvas.drawBitmap(R2, (float) (m_ScreenWidth-R2.getWidth()),  m_ScreenHeight / 2 - R2.getHeight() / 2, null);
                    m_Canvas.drawBitmap(ACC2, m_ScreenWidth/2 - ACC2.getWidth()/2, m_ScreenHeight * 7 / 8 - ACC2.getHeight() / 2, null);
                    m_Canvas.drawBitmap(LAS2, m_ScreenWidth - LAS2.getWidth()-10, m_ScreenHeight * 7 / 8 - LAS2.getHeight()/2 , null);
                }
                Bitmap botonPP;
                if(!m_Playing) botonPP = play2;
                else botonPP = pause2;
                m_Canvas.drawBitmap(botonPP,m_ScreenWidth/2-pause.getWidth()/2,10,null);
            }

            if(power){
                if(timePow<22000 ||(timePow>22500 && timePow<22750) || (timePow>23000 && timePow<23500) || (timePow>24000 && timePow<24250)
                        || (timePow>24500 && timePow<24625)
                        || (timePow>24750 && timePow<24875))m_Canvas.drawBitmap(playerPower2,at,null);

            }

            if(vida<=0){
                sound++;
                if(sound==1)m_SoundPool.play(m_lose_sound, volumen, volumen, 0, 0, 1);
                position.setY(-40);
                m_Canvas.drawBitmap(againorback2,m_ScreenWidth/2-againorback2.getWidth()/2,m_ScreenHeight/2-againorback2.getHeight()/2,null);
                m_Canvas.drawBitmap(gameOver2,m_ScreenWidth/2-gameOver2.getWidth()/2,m_ScreenHeight/2-3*gameOver2.getHeight(),null);
            }
            if (timeMeteor>=750) {
                if(level<3)m_Canvas.drawBitmap(meteor2, m1.x, m1.y, null);
                else m_Canvas.drawBitmap(meteorG2, m1.x, m1.y, null);
                if(level<3)m_Canvas.drawBitmap(meteor2, m2.x, m2.y, null);
                else m_Canvas.drawBitmap(meteorG2, m2.x, m2.y, null);
                m_Canvas.drawBitmap(meteor2, m3.x, m3.y, null);
                if(level<2)m_Canvas.drawBitmap(meteor2, m4.x, m4.y, null);
                else m_Canvas.drawBitmap(meteorM2, m4.x, m4.y, null);
                m_Canvas.drawBitmap(meteor2, m5.x, m5.y, null);
                if(level<2)m_Canvas.drawBitmap(meteor2, m6.x, m6.y, null);
                else m_Canvas.drawBitmap(meteorM2, m6.x, m6.y, null);
            }

            for (int i = 0; i < vida; i++) {
                 m_Canvas.drawBitmap(corazon2, 15+(i*corazon2.getWidth()),
                         m_ScreenHeight/20 + mutableBMP(numberCS).getHeight()+corazon2.getHeight()/2, null);
            }


            m_Holder.unlockCanvasAndPost(m_Canvas);
        }
    }

    public void drawPortada(){

        if (m_Holder.getSurface().isValid()) {
            m_Canvas = m_Holder.lockCanvas();


            Marcador();
            Naves();
            position.setX(m_ScreenWidth/2-player.getWidth()/2);
            position.setY(m_ScreenHeight-4*player.getHeight());


            at.setTranslate((long)(float) position.getX(),(long)(float) position.getY());
            at.preRotate(angle,player.getWidth()/2,player.getHeight()/2);

            m_Canvas.drawBitmap(pp2, 0 , 0, null);
            if(ayuda) m_Canvas.drawBitmap(mutableBMP2(menuAyuda),0,0,null);
             m_Canvas.drawBitmap(star2,estrella.x,estrella.y,null);
            if(!ayuda){
                m_Canvas.drawBitmap(ayudaBoton2, m_ScreenWidth-ayudaBoton2.getWidth()-15, (float) (1.5*starlevel2.getHeight()), null);
                m_Canvas.drawBitmap(player2,at,null);
            }
            m_Canvas.drawBitmap(meteor2, m1.x, m1.y, null);
            m_Canvas.drawBitmap(meteor2, m2.x, m2.y, null);
            m_Canvas.drawBitmap(meteor2, m3.x, m3.y, null);
            m_Canvas.drawBitmap(meteor2, m4.x, m4.y, null);
            m_Canvas.drawBitmap(meteor2, m5.x, m5.y, null);
            m_Canvas.drawBitmap(meteor2, m6.x, m6.y, null);


            m_Canvas.drawBitmap(top2,5,m_ScreenHeight/50,null);

            m_Canvas.drawBitmap(mutableBMP(numberUS),15+mutableBMP(numberCS).getWidth()+mutableBMP(numberDS).getWidth(),m_ScreenHeight/20,null);

            m_Canvas.drawBitmap(mutableBMP(numberDS),15+mutableBMP(numberCS).getWidth(),m_ScreenHeight/20,null);

            m_Canvas.drawBitmap(mutableBMP(numberCS),15,m_ScreenHeight/20,null);


            if(level>=2 && !ayuda) {
                m_Canvas.drawBitmap(L2, m_ScreenWidth / 2 - 2 * L2.getWidth(), (int) position.getY(), null);
                m_Canvas.drawBitmap(R2, m_ScreenWidth / 2 + R2.getWidth(), (int) position.getY(), null);
                 }
            if(volumen==0) m_Canvas.drawBitmap(mute2,m_ScreenWidth/2+m_ScreenWidth/4-mute2.getWidth()/2 ,m_ScreenHeight/70,null);
            else if(volumen==1) m_Canvas.drawBitmap(nomute2,m_ScreenWidth/2+m_ScreenWidth/4-mute2.getWidth()/2,m_ScreenHeight/70,null);


            m_Paint.setColor(Color.argb(255,255,200,0));
            m_Canvas.drawRect((m_ScreenWidth/2)- expbar2.getWidth()/2+(5*m_ScreenWidth/background.getWidth()),16,(m_ScreenWidth/2)-expbar2.getWidth()/2 +(5*m_ScreenWidth/background.getWidth()) +(experience*expbar2.getWidth()/(level*200)),15+expbar2.getHeight(),m_Paint);
            if (level==5){
            //    m_Paint.setColor(Color.argb(255,255,0,0));
                m_Paint.setColor(Color.argb(255,0,0,255));
                m_Canvas.drawRect((m_ScreenWidth/2)-expbar2.getWidth()/2 +(5*m_ScreenWidth/background.getWidth()),16,(m_ScreenWidth/2)-expbar2.getWidth()/2+(5*m_ScreenWidth/background.getWidth())+(experience*expbar2.getWidth()/(level*200)),15+expbar2.getHeight(),m_Paint);

            }

            m_Canvas.drawBitmap(expbar2,(m_ScreenWidth/2)-expbar2.getWidth()/2,15,null);
            m_Canvas.drawBitmap(starlevel2,m_ScreenWidth- starlevel2.getWidth() -10,15,null);
            if(modoControl==0)m_Canvas.drawBitmap(mandoClassic2,10,2*starlevel2.getHeight(),null);
            else m_Canvas.drawBitmap(mandoAlt2,10,2*starlevel2.getHeight(),null);

            m_Paint.setColor(Color.argb(255,255,255,255));
            m_Paint.setTextSize(m_ScreenWidth*45/1080);
            if(level<5)m_Canvas.drawText(experience +"/" + level*200, m_ScreenWidth/2-(45*m_ScreenHeight/1080), 16+expbar2.getHeight()+(45*m_ScreenHeight/1920), m_Paint);


            m_Holder.unlockCanvasAndPost(m_Canvas);

        }

    }

    public boolean checkForUpdate() {

        // Es momento de actualizar el cuadro
        if(m_NextFrameTime <= System.currentTimeMillis()){
            // Una décima de segundo ha pasado

            // Establece cuando la próxima actualización será disparada
            m_NextFrameTime =System.currentTimeMillis() + MILLIS_IN_A_SECOND / FPS;

            // Devuelve verdadero para que la actualización y las funciones
            // de trazado sean ejecutadas
            return true;
        }

        return false;
    }

    public void restart(){
        sound=0;
        vida=3;
        unit=0;
        dec=0;
        cent=0;
        timeMeteor=0;
        position.setX(m_ScreenWidth/2-player.getWidth()/2);
        position.setY(m_ScreenHeight-4*player.getHeight());
        m1.y=m2.y=m3.y=m4.y=m5.y=m6.y=m_ScreenHeight;
        m1.x=m2.x=m3.x=m4.x=m5.x=m6.x=m_ScreenWidth;
        angle=270;
        timeV=0;
        timeU=0;
        time2x=0;
        timePow=0;
        time2x=0;
        timeSlowly=0;
        timeSlow=0;
        salSlow=false;
        slowly=false;
        power=false;
        sal2x=false;
        salVida=false;
        salPower=false;
        x2=false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //empiezas el juego
        if(motionEvent.getAction()==MotionEvent.ACTION_UP && portada && !ayuda) {
            if(motionEvent.getY()> m_ScreenHeight/4 + play.getHeight() && level<2) {
                m_Playing = true;
                portada = false;
                restart();
            } else if(motionEvent.getY()<m_ScreenHeight*0.7 && motionEvent.getY()>m_ScreenHeight/4 + play.getHeight()&& !ayuda){
                m_Playing = true;
                portada = false;
                restart();
            }
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN && motionEvent.getY()< m_ScreenHeight/50 + play.getHeight()
                && motionEvent.getX()>m_ScreenWidth/2+m_ScreenWidth/4-mute2.getWidth() && motionEvent.getX()<m_ScreenWidth/2+m_ScreenWidth/4+1.5*mute2.getWidth()){
            if(volumen==0){

                volumen=1;
                if(power) m_SoundPool.stop(m_rock_sound);
            }
            else if(volumen==1){
                volumen=0;
                if(power) m_SoundPool.play(m_rock_sound,volumen,volumen,0,0,2);

            }
        }
        //cambias la nave
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN && portada && level>=2 && !ayuda) {
            if(motionEvent.getY()>m_ScreenHeight*0.7) {
                if(motionEvent.getX()<m_ScreenWidth/2) skin--;
                else skin++;

                if(skin>level-1) skin=0;
                if(skin<0) skin=level-1;
                Naves();
            }
        }

        if(motionEvent.getAction()==MotionEvent.ACTION_UP && portada && motionEvent.getX()< 2*mandoAlt.getHeight() && motionEvent.getY()<2.5*starlevel2.getHeight()+ mandoClassic2.getHeight()
                && motionEvent.getY()>=2*starlevel2.getHeight() && !ayuda) {
            if(modoControl==1) modoControl=0;
            else if(modoControl==0) modoControl=1;
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP && portada && motionEvent.getX()>m_ScreenWidth-starlevel2.getWidth()*1.5 && motionEvent.getY()<1.5*starlevel2.getHeight()+ mandoClassic2.getHeight()
                && motionEvent.getY()>=starlevel2.getHeight() ) {
            ayuda=true;
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP && ayuda && motionEvent.getX()>m_ScreenWidth/4 && motionEvent.getY()>m_ScreenHeight/2) {
            ayuda=false;
        }
        //Regresa a portada
        if (motionEvent.getAction()==MotionEvent.ACTION_UP
                && vida<=0 && motionEvent.getY() > m_ScreenHeight/2-againorback.getHeight()/2 && motionEvent.getY() < m_ScreenHeight/2+againorback.getHeight()/2
                && motionEvent.getX()>= m_ScreenWidth/2-againorback.getWidth()/2 && motionEvent.getX() <= m_ScreenWidth/2-againorback.getWidth()/20) {
            portada=true;
        }
        //pausar el juego
        if (motionEvent.getAction()==MotionEvent.ACTION_UP && motionEvent.getY() > 10 && motionEvent.getY() < 10+play.getHeight()+10
                && motionEvent.getX()>= m_ScreenWidth/2-play.getWidth() && motionEvent.getX() <= m_ScreenWidth/2+play.getWidth() && !portada) {
            if (!m_Playing) {
                m_SoundPool.autoResume();
                resume();
            } else if (m_Playing) {
                m_SoundPool.autoPause();
                pause();
            }
        }
        //perdiste y reiniciar el juego
        if (motionEvent.getAction()==MotionEvent.ACTION_UP && vida<=0 && motionEvent.getY() > m_ScreenHeight/2-againorback.getHeight()/2 && motionEvent.getY() < m_ScreenHeight/2+againorback.getHeight()/2
                && motionEvent.getX()>= m_ScreenWidth/2+againorback.getWidth()/20 && motionEvent.getX() <= m_ScreenWidth/2+againorback.getWidth()/2) {
            restart();
        }

// get pointer index from the event object
        int pointerIndex = motionEvent.getActionIndex();

        // get pointer ID
        int pointerId = motionEvent.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = motionEvent.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:{
                {// We have a new pointer. Lets add it to the list of pointers
                    mActivePointers.clear();
                    PointF f = new PointF();
                    f.x = motionEvent.getX(pointerIndex);
                    f.y = motionEvent.getY(pointerIndex);
                    mActivePointers.put(pointerId, f);
                    break;
                }
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = motionEvent.getX(pointerIndex);
                f.y = motionEvent.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:{

            }
            case MotionEvent.ACTION_POINTER_UP:
            {
                PointF d = new PointF();
                d.x = motionEvent.getX(pointerIndex);
                d.y = motionEvent.getY(pointerIndex);
                mActivePointers2.put(pointerId, d);
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                break;
            }
        }
        invalidate();






        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            PointF point = mActivePointers.valueAt(i);

            if (point.y > m_ScreenHeight*7/8-3*LAS2.getHeight()/2 && point.y < m_ScreenHeight*7/8-3*LAS2.getHeight()/2 + LAS2.getHeight()
                    && point.x >= m_ScreenWidth-LAS2.getWidth()-10 && point.x <= m_ScreenWidth && vida>0 && modoControl==1) {
                    shoot = true;
                    if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                        shoot=false;
                        right=false;
                        left=false;
                        acc = false;
                    }
            }else if (point.y > m_ScreenHeight-2*LAS2.getHeight() && point.y < m_ScreenHeight
                    && point.x >= m_ScreenWidth-2*LAS2.getWidth() && point.x <= m_ScreenWidth && vida>0 && modoControl==0) {
                shoot = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    shoot=false;
                    right=false;
                    left=false;
                    acc = false;
                }
            }

            if (point.x >= 1.7*mutableBMP3(L).getWidth() && point.x <= 4*mutableBMP3(L).getWidth()
                    && point.y <= 7*m_ScreenHeight/8+mutableBMP3(L).getHeight() && point.y>7*m_ScreenHeight/8- 1.5*mutableBMP3(L).getHeight()
                    && modoControl==1 && !left) {
                right = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }

            }else if (point.x > m_ScreenWidth/2 + m_ScreenWidth/8 && point.y <= m_ScreenHeight- 2*LAS2.getHeight()
                    && point.y>  m_ScreenHeight/20 + pause2.getHeight()+5 && !left && modoControl==0) {
                right = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }

            }

            if (point.x < 1.7*mutableBMP3(L).getWidth() && point.y <= 7*m_ScreenHeight/8+mutableBMP3(L).getHeight()
                    && point.y>7*m_ScreenHeight/8- 1.5*mutableBMP3(L).getHeight() && !right) {
                left = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }
            }else if (point.x < m_ScreenWidth/2 - m_ScreenWidth/8 && point.y <= m_ScreenHeight- 2*LAS2.getHeight()
                    && point.y>  m_ScreenHeight/20 + pause2.getHeight()+5 && !right && modoControl==0) {
                left = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }
            }


            if (point.y > (m_ScreenHeight * 7) / 8 - ACC2.getHeight() / 2 && point.y < m_ScreenHeight
                    && point.x >= m_ScreenWidth-ACC2.getWidth()- LAS2.getWidth()-10 && point.x <= m_ScreenWidth-LAS2.getWidth()-10 && modoControl==1) {
                acc = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }
            }else if (point.y > m_ScreenHeight-2*ACC2.getHeight() && point.y < m_ScreenHeight
                    && point.x >= m_ScreenWidth/2-ACC2.getWidth() && point.x <= m_ScreenWidth/2+ACC2.getWidth() && modoControl==0) {
                acc = true;
                if(maskedAction==MotionEvent.ACTION_UP || maskedAction==MotionEvent.ACTION_POINTER_UP){
                    acc = false;
                    right=false;
                    left=false;
                    shoot=false;
                }
            }



        }


        return true;
        }

    public void Marcador() {


       if (level==5)experience=level*100;

       if(experience>level*200){
            experience=0;
          if(level<5) level++;

           editor.putInt("Level",level);
           editor.putInt("Exp",experience);
           editor.commit();

       }



       if(unit>unitS && dec>=decS && cent>=centS){
           unitS=unit;
       }
       if(cent>centS){
           centS=cent;
       }
       if(dec>decS){
           decS=dec;
       }
       if (unit > 9) {

           dec++;
           unit = 0;
       }

       if (dec > 9) {

           cent++;
           dec = 0;
       }

       if (unitS > 9) {

           decS++;
           unitS = 0;
       }

       if (decS > 9) {

           centS++;
           decS = 0;
       }


       editor.putInt("UnitH", unitS);
       editor.putInt("DecH", decS);
       editor.putInt("CentH", centS);
       editor.commit();


       if(level==1) starlevel=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.starlevel1);
       if(level==2) starlevel=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.starlevel2);
       if(level==3) starlevel=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.starlevel3);
       if(level==4) starlevel=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.starlevel4);
       if(level==5) starlevel=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.starlevel5);


        if(unit==0) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(unit==1) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(unit==2) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(unit==3) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(unit==4) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(unit==5) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(unit==6) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(unit==7) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(unit==8) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(unit==9) numberU=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

        if(unitS==0) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(unitS==1) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(unitS==2) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(unitS==3) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(unitS==4) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(unitS==5) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(unitS==6) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(unitS==7) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(unitS==8) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(unitS==9) numberUS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

        if(dec==0) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(dec==1) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(dec==2) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(dec==3) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(dec==4) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(dec==5) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(dec==6) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(dec==7) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(dec==8) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(dec==9) numberD=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

        if(decS==0) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(decS==1) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(decS==2) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(decS==3) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(decS==4) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(decS==5) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(decS==6) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(decS==7) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(decS==8) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(decS==9) numberDS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

        if(cent==0) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(cent==1) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(cent==2) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(cent==3) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(cent==4) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(cent==5) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(cent==6) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(cent==7) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(cent==8) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(cent==9) numberC=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

        if(centS==0) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cero);
        if(centS==1) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.uno);
        if(centS==2) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.dos);
        if(centS==3) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.tres);
        if(centS==4) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cuatro);
        if(centS==5) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.cinco);
        if(centS==6) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.seis);
        if(centS==7) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.siete);
        if(centS==8) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.ocho);
        if(centS==9) numberCS=BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.nueve);

    starlevel2=mutableBMP(starlevel);

       editor.putInt("SKIN",skin);
       editor.commit();


    }

    public void Naves(){

        editor.putInt("SKIN",skin);
        editor.commit();
        if(skin==0){
            player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navelouis);
            playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navelouis2);
        }
        else if(skin==1){
            player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navevzl);
            playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navevzl2);
        }
        else if(skin==2){
            player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navedouble);
            playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navedouble2);
        }
        else if(skin==4){
            player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navepeque2);
            playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.navepeque);
        }
        else if(skin==3){
            player= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.omega);
            playerPower= BitmapFactory.decodeResource(getResources(), e.equipo.probando.R.drawable.omega2);
        }

        playerPower2=mutableBMP(playerPower);
        player2=mutableBMP(player);
    }

    public void angles(float angle){
        if (angle == 0 || angle == 360 || angle == 180)
            velocity = new Vector2D(5, 0);
        if (angle == 10 || angle == 170 || angle == 190 || angle == 350)
            velocity = new Vector2D(4.92, 0.86);
        if (angle == 20 || angle == 160 || angle == 200 || angle == 340)
            velocity = new Vector2D(4.69, 1.71);
        if (angle == 30 || angle == 150 || angle == 210 || angle == 330)
            velocity = new Vector2D(4.33, 2.5);
        if (angle == 40 || angle == 140 || angle == 220 || angle == 320)
            velocity = new Vector2D(3.83, 3.21);
        if (angle == 50 || angle == 130 || angle == 230 || angle == 310)
            velocity = new Vector2D(3.21, 3.83);
        if (angle == 60 || angle == 120 || angle == 240 || angle == 300)
            velocity = new Vector2D(2.5, 4.34);
        if (angle == 70 || angle == 110 || angle == 250 || angle == 290)
            velocity = new Vector2D(1.71, 4.69);
        if (angle == 80 || angle == 100 || angle == 260 || angle == 280)
            velocity = new Vector2D(0.86, 4.92);
        if (angle == 90 || angle == 270) velocity = new Vector2D(0, 5);
        if (angle == 1 || angle == 179 || angle == 181 || angle == 359)
            velocity = new Vector2D(4.99, 0.1);
        if (angle == 2 || angle == 178 || angle == 182 || angle == 358)
            velocity = new Vector2D(4.99, 0.17);
        if (angle == 3 || angle == 177 || angle == 183 || angle == 357)
            velocity = new Vector2D(4.99, 0.26);
        if (angle == 4 || angle == 176 || angle == 184 || angle == 356)
            velocity = new Vector2D(4.98, 0.34);
        if (angle == 5 || angle == 175 || angle == 185 || angle == 355)
            velocity = new Vector2D(4.98, 0.43);
        if (angle == 6 || angle == 174 || angle == 186 || angle == 354)
            velocity = new Vector2D(4.97, 0.54);
        if (angle == 7 || angle == 173 || angle == 187 || angle == 353)
            velocity = new Vector2D(4.96, 0.65);
        if (angle == 8 || angle == 172 || angle == 188 || angle == 352)
            velocity = new Vector2D(4.95, 0.76);
        if (angle == 9 || angle == 171 || angle == 189 || angle == 351)
            velocity = new Vector2D(4.94, 0.82);
        if (angle == 11 || angle == 169 || angle == 191 || angle == 349)
            velocity = new Vector2D(4.9, 0.95);
        if (angle == 12 || angle == 168 || angle == 192 || angle == 348)
            velocity = new Vector2D(4.89, 1.24);
        if (angle == 13 || angle == 167 || angle == 193 || angle == 347)
            velocity = new Vector2D(4.87, 1.12);
        if (angle == 14 || angle == 166 || angle == 194 || angle == 346)
            velocity = new Vector2D(4.85, 1.2);
        if (angle == 15 || angle == 165 || angle == 195 || angle == 345)
            velocity = new Vector2D(4.82, 1.3);
        if (angle == 16 || angle == 164 || angle == 196 || angle == 344)
            velocity = new Vector2D(4.8, 1.37);
        if (angle == 17 || angle == 163 || angle == 197 || angle == 343)
            velocity = new Vector2D(4.78, 1.46);
        if (angle == 18 || angle == 162 || angle == 198 || angle == 342)
            velocity = new Vector2D(4.76, 1.54);
        if (angle == 19 || angle == 161 || angle == 199 || angle == 341)
            velocity = new Vector2D(4.72, 1.62);
        if (angle == 21 || angle == 159 || angle == 201 || angle == 339)
            velocity = new Vector2D(4.66, 1.79);
        if (angle == 22 || angle == 158 || angle == 202 || angle == 338)
            velocity = new Vector2D(4.63, 1.87);
        if (angle == 23 || angle == 157 || angle == 203 || angle == 337)
            velocity = new Vector2D(4.6, 1.95);
        if (angle == 24 || angle == 156 || angle == 204 || angle == 336)
            velocity = new Vector2D(4.56, 2.03);
        if (angle == 25 || angle == 155 || angle == 205 || angle == 335)
            velocity = new Vector2D(4.53, 2.1);
        if (angle == 26 || angle == 154 || angle == 206 || angle == 334)
            velocity = new Vector2D(4.49, 2.19);
        if (angle == 27 || angle == 153 || angle == 207 || angle == 333)
            velocity = new Vector2D(4.45, 2.27);
        if (angle == 28 || angle == 152 || angle == 208 || angle == 332)
            velocity = new Vector2D(4.41, 2.35);
        if (angle == 29 || angle == 151 || angle == 209 || angle == 331)
            velocity = new Vector2D(4.37, 2.42);
        if (angle == 31 || angle == 149 || angle == 211 || angle == 329)
            velocity = new Vector2D(4.28, 2.57);
        if (angle == 32 || angle == 148 || angle == 212 || angle == 328)
            velocity = new Vector2D(4.24, 2.64);
        if (angle == 33 || angle == 147 || angle == 213 || angle == 327)
            velocity = new Vector2D(4.19, 2.72);
        if (angle == 34 || angle == 146 || angle == 214 || angle == 326)
            velocity = new Vector2D(4.14, 2.79);
        if (angle == 35 || angle == 145 || angle == 215 || angle == 325)
            velocity = new Vector2D(4.09, 2.86);
        if (angle == 36 || angle == 144 || angle == 216 || angle == 324)
            velocity = new Vector2D(4.04, 2.93);
        if (angle == 37 || angle == 143 || angle == 217 || angle == 323)
            velocity = new Vector2D(3.99, 3.00);
        if (angle == 38 || angle == 142 || angle == 218 || angle == 322)
            velocity = new Vector2D(3.94, 3.07);
        if (angle == 39 || angle == 141 || angle == 219 || angle == 321)
            velocity = new Vector2D(3.89, 3.14);
        if (angle == 41 || angle == 139 || angle == 221 || angle == 319)
            velocity = new Vector2D(3.77, 3.28);
        if (angle == 42 || angle == 138 || angle == 222 || angle == 318)
            velocity = new Vector2D(3.71, 3.34);
        if (angle == 43 || angle == 137 || angle == 223 || angle == 317)
            velocity = new Vector2D(3.65, 3.4);
        if (angle == 44 || angle == 136 || angle == 224 || angle == 316)
            velocity = new Vector2D(3.59, 3.46);
        if (angle == 45 || angle == 135 || angle == 225 || angle == 315)
            velocity = new Vector2D(3.53, 3.53);
        if (angle == 46 || angle == 134 || angle == 226 || angle == 314)
            velocity = new Vector2D(3.47, 3.59);
        if (angle == 47 || angle == 133 || angle == 227 || angle == 313)
            velocity = new Vector2D(3.4, 3.65);
        if (angle == 48 || angle == 132 || angle == 228 || angle == 312)
            velocity = new Vector2D(3.34, 3.71);
        if (angle == 49 || angle == 131 || angle == 229 || angle == 311)
            velocity = new Vector2D(3.28, 3.77);
        if (angle == 51 || angle == 129 || angle == 231 || angle == 309)
            velocity = new Vector2D(3.14, 3.88);
        if (angle == 52 || angle == 128 || angle == 232 || angle == 308)
            velocity = new Vector2D(3.07, 3.94);
        if (angle == 53 || angle == 127 || angle == 233 || angle == 307)
            velocity = new Vector2D(3.00, 3.99);
        if (angle == 54 || angle == 126 || angle == 234 || angle == 306)
            velocity = new Vector2D(2.93, 4.04);
        if (angle == 55 || angle == 125 || angle == 235 || angle == 305)
            velocity = new Vector2D(2.86, 4.09);
        if (angle == 56 || angle == 124 || angle == 236 || angle == 304)
            velocity = new Vector2D(2.8, 4.14);
        if (angle == 57 || angle == 123 || angle == 237 || angle == 303)
            velocity = new Vector2D(2.72, 4.19);
        if (angle == 58 || angle == 122 || angle == 238 || angle == 302)
            velocity = new Vector2D(2.64, 4.24);
        if (angle == 59 || angle == 121 || angle == 239 || angle == 301)
            velocity = new Vector2D(2.57, 4.29);
        if (angle == 61 || angle == 119 || angle == 241 || angle == 299)
            velocity = new Vector2D(2.42, 4.37);
        if (angle == 62 || angle == 118 || angle == 242 || angle == 298)
            velocity = new Vector2D(2.34, 4.41);
        if (angle == 63 || angle == 117 || angle == 243 || angle == 297)
            velocity = new Vector2D(2.26, 4.45);
        if (angle == 64 || angle == 116 || angle == 244 || angle == 296)
            velocity = new Vector2D(2.18, 4.49);
        if (angle == 65 || angle == 115 || angle == 245 || angle == 295)
            velocity = new Vector2D(2.1, 4.53);
        if (angle == 66 || angle == 114 || angle == 246 || angle == 294)
            velocity = new Vector2D(2.03, 4.56);
        if (angle == 67 || angle == 113 || angle == 247 || angle == 293)
            velocity = new Vector2D(1.95, 4.6);
        if (angle == 68 || angle == 112 || angle == 248 || angle == 292)
            velocity = new Vector2D(1.87, 4.63);
        if (angle == 69 || angle == 111 || angle == 249 || angle == 291)
            velocity = new Vector2D(1.79, 4.66);
        if (angle == 71 || angle == 109 || angle == 251 || angle == 289)
            velocity = new Vector2D(1.62, 4.72);
        if (angle == 72 || angle == 108 || angle == 252 || angle == 288)
            velocity = new Vector2D(1.54, 4.75);
        if (angle == 73 || angle == 107 || angle == 253 || angle == 287)
            velocity = new Vector2D(1.46, 4.78);
        if (angle == 74 || angle == 106 || angle == 254 || angle == 286)
            velocity = new Vector2D(1.38, 4.8);
        if (angle == 75 || angle == 105 || angle == 255 || angle == 285)
            velocity = new Vector2D(1.3, 4.82);
        if (angle == 76 || angle == 104 || angle == 156 || angle == 284)
            velocity = new Vector2D(1.2, 4.85);
        if (angle == 77 || angle == 103 || angle == 157 || angle == 283)
            velocity = new Vector2D(1.12, 4.87);
        if (angle == 78 || angle == 102 || angle == 158 || angle == 282)
            velocity = new Vector2D(1.03, 4.89);
        if (angle == 79 || angle == 101 || angle == 259 || angle == 281)
            velocity = new Vector2D(0.95, 4.9);
        if (angle == 81 || angle == 99 || angle == 261 || angle == 279)
            velocity = new Vector2D(0.78, 4.93);
        if (angle == 82 || angle == 98 || angle == 262 || angle == 278)
            velocity = new Vector2D(0.69, 4.95);
        if (angle == 83 || angle == 97 || angle == 263 || angle == 277)
            velocity = new Vector2D(0.6, 4.96);
        if (angle == 84 || angle == 96 || angle == 264 || angle == 276)
            velocity = new Vector2D(0.52, 4.97);
        if (angle == 85 || angle == 95 || angle == 265 || angle == 275)
            velocity = new Vector2D(0.43, 4.98);
        if (angle == 86 || angle == 94 || angle == 266 || angle == 274)
            velocity = new Vector2D(0.17, 4.99);
        if (angle == 87 || angle == 93 || angle == 267 || angle == 273)
            velocity = new Vector2D(0.26, 4.99);
        if (angle == 88 || angle == 92 || angle == 268 || angle == 272)
            velocity = new Vector2D(0.17, 4.99);
        if (angle == 89 || angle == 91 || angle == 269 || angle == 271)
            velocity = new Vector2D(0.1, 4.99);


        if (angle <90) {
            velocity = new Vector2D(velocity.getX() * 8*mutableBMP(background).getWidth()/1080, velocity.getY()*8*mutableBMP(background).getWidth()/1080);
        }
        if (angle >= 90 && angle < 180) {
            velocity = new Vector2D(velocity.getX() * -8*mutableBMP(background).getWidth()/1080, velocity.getY()*8*mutableBMP(background).getWidth()/1080);
        }
        if (angle >= 180 && angle <= 270) {
            velocity = new Vector2D(velocity.getX() * -8*mutableBMP(background).getWidth()/1080, velocity.getY() * -8*mutableBMP(background).getWidth()/1080);
        }
        if (angle > 270 && angle <= 360) {
            velocity = new Vector2D(velocity.getX()*8*mutableBMP(background).getWidth()/1080, velocity.getY() * -8*mutableBMP(background).getWidth()/1080);
        }

    }

    public void anglesLaser(float angle){
        if (angle == 0 || angle == 360 || angle == 180)
            velocityLaser = new Vector2D(5, 0);
        if (angle == 10 || angle == 170 || angle == 190 || angle == 350)
            velocityLaser = new Vector2D(4.92, 0.86);
        if (angle == 20 || angle == 160 || angle == 200 || angle == 340)
            velocityLaser = new Vector2D(4.69, 1.71);
        if (angle == 30 || angle == 150 || angle == 210 || angle == 330)
            velocityLaser = new Vector2D(4.33, 2.5);
        if (angle == 40 || angle == 140 || angle == 220 || angle == 320)
            velocityLaser = new Vector2D(3.83, 3.21);
        if (angle == 50 || angle == 130 || angle == 230 || angle == 310)
            velocityLaser = new Vector2D(3.21, 3.83);
        if (angle == 60 || angle == 120 || angle == 240 || angle == 300)
            velocityLaser = new Vector2D(2.5, 4.34);
        if (angle == 70 || angle == 110 || angle == 250 || angle == 290)
            velocityLaser = new Vector2D(1.71, 4.69);
        if (angle == 80 || angle == 100 || angle == 260 || angle == 280)
            velocityLaser = new Vector2D(0.86, 4.92);
        if (angle == 90 || angle == 270) velocityLaser = new Vector2D(0, 5);

        if (angle == 1 || angle == 179 || angle == 181 || angle == 359)
            velocityLaser = new Vector2D(4.99, 0.1);
        if (angle == 2 || angle == 178 || angle == 182 || angle == 358)
            velocityLaser = new Vector2D(4.99, 0.17);
        if (angle == 3 || angle == 177 || angle == 183 || angle == 357)
            velocityLaser = new Vector2D(4.99, 0.26);
        if (angle == 4 || angle == 176 || angle == 184 || angle == 356)
            velocityLaser = new Vector2D(4.98, 0.34);
        if (angle == 5 || angle == 175 || angle == 185 || angle == 355)
            velocityLaser = new Vector2D(4.98, 0.43);
        if (angle == 6 || angle == 174 || angle == 186 || angle == 354)
            velocityLaser = new Vector2D(4.97, 0.54);
        if (angle == 7 || angle == 173 || angle == 187 || angle == 353)
            velocityLaser = new Vector2D(4.96, 0.65);
        if (angle == 8 || angle == 172 || angle == 188 || angle == 352)
            velocityLaser = new Vector2D(4.95, 0.76);
        if (angle == 9 || angle == 171 || angle == 189 || angle == 351)
            velocityLaser = new Vector2D(4.94, 0.82);
        if (angle == 11 || angle == 169 || angle == 191 || angle == 349)
            velocityLaser = new Vector2D(4.9, 0.95);
        if (angle == 12 || angle == 168 || angle == 192 || angle == 348)
            velocityLaser = new Vector2D(4.89, 1.24);
        if (angle == 13 || angle == 167 || angle == 193 || angle == 347)
            velocityLaser = new Vector2D(4.87, 1.12);
        if (angle == 14 || angle == 166 || angle == 194 || angle == 346)
            velocityLaser = new Vector2D(4.85, 1.2);
        if (angle == 15 || angle == 165 || angle == 195 || angle == 345)
            velocityLaser = new Vector2D(4.82, 1.3);
        if (angle == 16 || angle == 164 || angle == 196 || angle == 344)
            velocityLaser = new Vector2D(4.8, 1.37);
        if (angle == 17 || angle == 163 || angle == 197 || angle == 343)
            velocityLaser = new Vector2D(4.78, 1.46);
        if (angle == 18 || angle == 162 || angle == 198 || angle == 342)
            velocityLaser = new Vector2D(4.76, 1.54);
        if (angle == 19 || angle == 161 || angle == 199 || angle == 341)
            velocityLaser = new Vector2D(4.72, 1.62);
        if (angle == 21 || angle == 159 || angle == 201 || angle == 339)
            velocityLaser = new Vector2D(4.66, 1.79);
        if (angle == 22 || angle == 158 || angle == 202 || angle == 338)
            velocityLaser = new Vector2D(4.63, 1.87);
        if (angle == 23 || angle == 157 || angle == 203 || angle == 337)
            velocityLaser = new Vector2D(4.6, 1.95);
        if (angle == 24 || angle == 156 || angle == 204 || angle == 336)
            velocityLaser = new Vector2D(4.56, 2.03);
        if (angle == 25 || angle == 155 || angle == 205 || angle == 335)
            velocityLaser = new Vector2D(4.53, 2.1);
        if (angle == 26 || angle == 154 || angle == 206 || angle == 334)
            velocityLaser = new Vector2D(4.49, 2.19);
        if (angle == 27 || angle == 153 || angle == 207 || angle == 333)
            velocityLaser = new Vector2D(4.45, 2.27);
        if (angle == 28 || angle == 152 || angle == 208 || angle == 332)
            velocityLaser = new Vector2D(4.41, 2.35);
        if (angle == 29 || angle == 151 || angle == 209 || angle == 331)
            velocityLaser = new Vector2D(4.37, 2.42);
        if (angle == 31 || angle == 149 || angle == 211 || angle == 329)
            velocityLaser = new Vector2D(4.28, 2.57);
        if (angle == 32 || angle == 148 || angle == 212 || angle == 328)
            velocityLaser = new Vector2D(4.24, 2.64);
        if (angle == 33 || angle == 147 || angle == 213 || angle == 327)
            velocityLaser = new Vector2D(4.19, 2.72);
        if (angle == 34 || angle == 146 || angle == 214 || angle == 326)
            velocityLaser = new Vector2D(4.14, 2.79);
        if (angle == 35 || angle == 145 || angle == 215 || angle == 325)
            velocityLaser = new Vector2D(4.09, 2.86);
        if (angle == 36 || angle == 144 || angle == 216 || angle == 324)
            velocityLaser = new Vector2D(4.04, 2.93);
        if (angle == 37 || angle == 143 || angle == 217 || angle == 323)
            velocityLaser = new Vector2D(3.99, 3.00);
        if (angle == 38 || angle == 142 || angle == 218 || angle == 322)
            velocityLaser = new Vector2D(3.94, 3.07);
        if (angle == 39 || angle == 141 || angle == 219 || angle == 321)
            velocityLaser = new Vector2D(3.89, 3.14);
        if (angle == 41 || angle == 139 || angle == 221 || angle == 319)
            velocityLaser = new Vector2D(3.77, 3.28);
        if (angle == 42 || angle == 138 || angle == 222 || angle == 318)
            velocityLaser = new Vector2D(3.71, 3.34);
        if (angle == 43 || angle == 137 || angle == 223 || angle == 317)
            velocityLaser = new Vector2D(3.65, 3.4);
        if (angle == 44 || angle == 136 || angle == 224 || angle == 316)
            velocityLaser = new Vector2D(3.59, 3.46);
        if (angle == 45 || angle == 135 || angle == 225 || angle == 315)
            velocityLaser = new Vector2D(3.53, 3.53);
        if (angle == 46 || angle == 134 || angle == 226 || angle == 314)
            velocityLaser = new Vector2D(3.47, 3.59);
        if (angle == 47 || angle == 133 || angle == 227 || angle == 313)
            velocityLaser = new Vector2D(3.4, 3.65);
        if (angle == 48 || angle == 132 || angle == 228 || angle == 312)
            velocityLaser = new Vector2D(3.34, 3.71);
        if (angle == 49 || angle == 131 || angle == 229 || angle == 311)
            velocityLaser = new Vector2D(3.28, 3.77);
        if (angle == 51 || angle == 129 || angle == 231 || angle == 309)
            velocityLaser = new Vector2D(3.14, 3.88);
        if (angle == 52 || angle == 128 || angle == 232 || angle == 308)
            velocityLaser = new Vector2D(3.07, 3.94);
        if (angle == 53 || angle == 127 || angle == 233 || angle == 307)
            velocityLaser = new Vector2D(3.00, 3.99);
        if (angle == 54 || angle == 126 || angle == 234 || angle == 306)
            velocityLaser = new Vector2D(2.93, 4.04);
        if (angle == 55 || angle == 125 || angle == 235 || angle == 305)
            velocityLaser = new Vector2D(2.86, 4.09);
        if (angle == 56 || angle == 124 || angle == 236 || angle == 304)
            velocityLaser = new Vector2D(2.8, 4.14);
        if (angle == 57 || angle == 123 || angle == 237 || angle == 303)
            velocityLaser = new Vector2D(2.72, 4.19);
        if (angle == 58 || angle == 122 || angle == 238 || angle == 302)
            velocityLaser = new Vector2D(2.64, 4.24);
        if (angle == 59 || angle == 121 || angle == 239 || angle == 301)
            velocityLaser = new Vector2D(2.57, 4.29);
        if (angle == 61 || angle == 119 || angle == 241 || angle == 299)
            velocityLaser = new Vector2D(2.42, 4.37);
        if (angle == 62 || angle == 118 || angle == 242 || angle == 298)
            velocityLaser = new Vector2D(2.34, 4.41);
        if (angle == 63 || angle == 117 || angle == 243 || angle == 297)
            velocityLaser = new Vector2D(2.26, 4.45);
        if (angle == 64 || angle == 116 || angle == 244 || angle == 296)
            velocityLaser = new Vector2D(2.18, 4.49);
        if (angle == 65 || angle == 115 || angle == 245 || angle == 295)
            velocityLaser = new Vector2D(2.1, 4.53);
        if (angle == 66 || angle == 114 || angle == 246 || angle == 294)
            velocityLaser = new Vector2D(2.03, 4.56);
        if (angle == 67 || angle == 113 || angle == 247 || angle == 293)
            velocityLaser = new Vector2D(1.95, 4.6);
        if (angle == 68 || angle == 112 || angle == 248 || angle == 292)
            velocityLaser = new Vector2D(1.87, 4.63);
        if (angle == 69 || angle == 111 || angle == 249 || angle == 291)
            velocityLaser = new Vector2D(1.79, 4.66);
        if (angle == 71 || angle == 109 || angle == 251 || angle == 289)
            velocityLaser = new Vector2D(1.62, 4.72);
        if (angle == 72 || angle == 108 || angle == 252 || angle == 288)
            velocityLaser = new Vector2D(1.54, 4.75);
        if (angle == 73 || angle == 107 || angle == 253 || angle == 287)
            velocityLaser = new Vector2D(1.46, 4.78);
        if (angle == 74 || angle == 106 || angle == 254 || angle == 286)
            velocityLaser = new Vector2D(1.38, 4.8);
        if (angle == 75 || angle == 105 || angle == 255 || angle == 285)
            velocityLaser = new Vector2D(1.3, 4.82);
        if (angle == 76 || angle == 104 || angle == 156 || angle == 284)
            velocityLaser = new Vector2D(1.2, 4.85);
        if (angle == 77 || angle == 103 || angle == 157 || angle == 283)
            velocityLaser = new Vector2D(1.12, 4.87);
        if (angle == 78 || angle == 102 || angle == 158 || angle == 282)
            velocityLaser = new Vector2D(1.03, 4.89);
        if (angle == 79 || angle == 101 || angle == 259 || angle == 281)
            velocityLaser = new Vector2D(0.95, 4.9);
        if (angle == 81 || angle == 99 || angle == 261 || angle == 279)
            velocityLaser = new Vector2D(0.78, 4.93);
        if (angle == 82 || angle == 98 || angle == 262 || angle == 278)
            velocityLaser = new Vector2D(0.69, 4.95);
        if (angle == 83 || angle == 97 || angle == 263 || angle == 277)
            velocityLaser = new Vector2D(0.6, 4.96);
        if (angle == 84 || angle == 96 || angle == 264 || angle == 276)
            velocityLaser = new Vector2D(0.52, 4.97);
        if (angle == 85 || angle == 95 || angle == 265 || angle == 275)
            velocityLaser = new Vector2D(0.43, 4.98);
        if (angle == 86 || angle == 94 || angle == 266 || angle == 274)
            velocityLaser = new Vector2D(0.17, 4.99);
        if (angle == 87 || angle == 93 || angle == 267 || angle == 273)
            velocityLaser = new Vector2D(0.26, 4.99);
        if (angle == 88 || angle == 92 || angle == 268 || angle == 272)
            velocityLaser = new Vector2D(0.17, 4.99);
        if (angle == 89 || angle == 91 || angle == 269 || angle == 271)
            velocityLaser = new Vector2D(0.1, 4.99);

        if (angle <90) {
            velocityLaser = new Vector2D(velocityLaser.getX() * 8*mutableBMP(background).getWidth()/1080, velocityLaser.getY()*8*mutableBMP(background).getWidth()/1080);
        }
        if (angle >= 90 && angle < 180) {
            velocityLaser = new Vector2D(velocityLaser.getX() * -8*mutableBMP(background).getWidth()/1080, velocityLaser.getY()*8*mutableBMP(background).getWidth()/1080);
        }
        if (angle >= 180 && angle < 270) {
            velocityLaser = new Vector2D(velocityLaser.getX() * -8*mutableBMP(background).getWidth()/1080, velocityLaser.getY() * -8*mutableBMP(background).getWidth()/1080);
        }
        if (angle >= 270 && angle <= 360) {
            velocityLaser = new Vector2D(velocityLaser.getX()*8*mutableBMP(background).getWidth()/1080, velocityLaser.getY() * -8*mutableBMP(background).getWidth()/1080);
        }

    }

    public Bitmap mutableBMP(Bitmap immutableBmp){
        Bitmap mutableBitmap;
        mutableBitmap= Bitmap.createScaledBitmap(immutableBmp, (int) ((m_ScreenWidth*immutableBmp.getWidth())/(background.getWidth()*0.95)),(m_ScreenHeight*immutableBmp.getHeight())/background.getHeight(),false);

        return mutableBitmap;
    }
    public Bitmap mutableBMP3(Bitmap immutableBmp){
        Bitmap mutableBitmap;
        mutableBitmap= Bitmap.createScaledBitmap(immutableBmp, (int) ((m_ScreenWidth*immutableBmp.getWidth())/(background.getWidth()*0.95)),(m_ScreenHeight*immutableBmp.getHeight())/background.getHeight(),false);

        return mutableBitmap;
    }

    public Bitmap mutableBMP2(Bitmap immutableBmp){
        Bitmap mutableBitmap;
        mutableBitmap= Bitmap.createScaledBitmap(immutableBmp,(m_ScreenWidth*immutableBmp.getWidth())/background.getWidth(),(m_ScreenHeight*immutableBmp.getHeight())/background.getHeight(),false);

        return mutableBitmap;
    }

    public void meteoroAgujero(Meteor met1){
        if(agujeroAct && agujeroNegro.choqueS(met1.x,met1.y,meteor2.getWidth(),meteor2.getHeight(),agujeroN2.getWidth(),agujeroN2.getHeight())){
            met1.x = (float) (Math.random()*m_ScreenWidth-10);
            met1.y = (float)Math.random()*m_ScreenHeight-2*L2.getHeight();
            if(level<4){
                agujeroAct=false;
                timeNegro = (20000);
            }

        }
    }

}


