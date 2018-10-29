package game.GUI;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil;

import game.Texture.Texture;
import game.Toolbox.Utils;

import static org.lwjgl.system.MemoryUtil.NULL;
import static GameGraphic.CoordsAndData.*;
import static game.Toolbox.Transformation.*;

public class Items{

    private final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private long screen;
	private static NVGColor colour;
	private ByteBuffer fontBuffer;
	private static final String FONT_NAME = "BOLD";
    private static int counter;
    private static Texture texture = new Texture("textures/blue.jpg");


    public Items(long screen, NVGColor colour) {
    	this.screen = screen;
    	this.colour = colour;
    }
    
    public Items(Items item) {
    	this.screen = item.screen;
    	this.colour = item.colour;
    }
    
    public void toolBar() {
        nvgBeginFrame(screen, WIDTH, HEIGHT, 1);
    	nvgBeginPath(screen);
        nvgRect(screen, transformX(-1f), transformY(1f), WIDTH, transformHeight(0.1f));
        nvgFillColor(screen, rgba(38, 38, 38, 150, colour));
        nvgFill(screen);
        

        // Lower ribbon
        nvgBeginPath(screen);
        nvgRect(screen, transformX(-1f), transformY(0.9f), WIDTH, transformHeight(0.01f));
        nvgFillColor(screen, rgba(107, 107, 107, 200, colour));
        nvgFill(screen);
    
        // Clicks Text
        nvgFontSize(screen, 40.0f);
        nvgFontFace(screen, FONT_NAME);
        nvgTextAlign(screen, NVG_ALIGN_CENTER | NVG_ALIGN_TOP);
        nvgFillColor(screen, rgba(0xe6, 0xea, 0xed, 255, colour));
        nvgText(screen, transformX(-0.85f), transformY(0.99f), "Escape Room");

        // Render hour text
        nvgFontSize(screen, 40.0f);
        nvgFontFace(screen, FONT_NAME);
        nvgTextAlign(screen, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgFillColor(screen, rgba(0xe6, 0xea, 0xed, 255, colour));
        nvgText(screen, transformX(0.8f), transformY(0.99f), dateFormat.format(new Date()));
        nvgEndFrame(screen);

        restoreInitialState();
    }
    
    public void computerScreen() {
    	nvgBeginFrame(screen, WIDTH, HEIGHT, 1);
    	nvgBeginPath(screen);
        nvgRect(screen, transformX(-1f), transformY(0.89f), WIDTH, transformHeight(1.89f));
        nvgFillColor(screen, rgba(255, 255, 255, 200, colour));
        nvgFill(screen);
    	nvgBeginPath(screen);
        nvgRect(screen, transformX(-0.7f), transformY(0.7f), transformWidth(1.4f), transformHeight(1.4f));
        nvgFillColor(screen, rgba(45, 78, 132, 250, colour));
        nvgFill(screen);
        nvgEndFrame(screen);
        restoreInitialState();
    }
    
    public void textGameOver() {
    	nvgBeginFrame(screen, WIDTH, HEIGHT, 1);
    	nvgBeginPath(screen);
    	nvgFontSize(screen, 200f);
        nvgFontFace(screen, FONT_NAME);
        nvgTextAlign(screen, NVG_ALIGN_CENTER | NVG_ALIGN_TOP);
        nvgFillColor(screen, rgba(0xe6, 0xea, 0xed, 255, colour));
        nvgText(screen, transformX(0f), transformY(0f), "GAME OVER");
        nvgEndFrame(screen);
        restoreInitialState();
    }
    
    public void turnOnButton() {
    	nvgBeginFrame(screen, WIDTH, HEIGHT, 1);
    	nvgBeginPath(screen);
        nvgRect(screen, transformX(turnOnButton[0]), transformY(turnOnButton[1]), turnOnButton[2], turnOnButton[3]);
        nvgFillColor(screen, rgba(0, 0, 0, 250, colour));
        nvgFill(screen);
        //NVGPaint imgPaint;
        //nvgImagePattern(screen, 0f, 1f, 100f, 100f, 0.0f/180.0f*NVG_PI, texture.getTexTureID(), 200f, imgPaint);
		//nvgBeginPath(screen);
		//nvgRoundedRect(screen, 500f, 500f, 500f, 500f, 5);
		//nvgFillPaint(screen, imgPaint);
		//nvgFill(screen);
        nvgEndFrame(screen);
        restoreInitialState();

    }
    
   
    private static NVGColor rgba(int r, int g, int b, int a, NVGColor colour) {
        colour.r(r / 255.0f);
        colour.g(g / 255.0f);
        colour.b(b / 255.0f);
        colour.a(a / 255.0f);

        return colour;
    }
    
	
	private void restoreInitialState() {
		// Restore state
		glEnable(GL_DEPTH_TEST);
	    glEnable(GL_STENCIL_TEST);
	    //glEnable(GL_CULL_FACE);
	   	//glCullFace(GL_BACK);
	   //glEnable(GL_BLEND);
	   //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}