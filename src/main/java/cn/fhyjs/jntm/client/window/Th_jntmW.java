package cn.fhyjs.jntm.client.window;

import cn.fhyjs.jntm.Jntm;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Th_jntmW extends Thread{
    private DisplayMode Displaymode;
    @Override
    public void run(){
        try {
            setup();   // get window ready to go
            loop();
        } catch(Throwable e){
            Jntm.logger.error(e);
        } finally {
            // Terminate GLFW
            Destroy();
        }
    }
    private void Destroy(){
        Display.destroy();
    }
    private void setup() throws LWJGLException {
        Displaymode = new DisplayMode(600, 600);
        Display.setDisplayMode(Displaymode);
        Display.create();
    }
    private void loop() throws InterruptedException {
        Thread.sleep(10000);
    }
}
