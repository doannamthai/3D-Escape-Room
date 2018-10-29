package game.GUI;

import static GameGraphic.CoordsAndData.HEIGHT;
import static GameGraphic.CoordsAndData.WIDTH;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.nanovg.NanoVGGL3.nvgDelete;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import game.Toolbox.Utils;

public class Screen2D {
	
	private static long screen;
	private static NVGColor colour;
    private ByteBuffer fontBuffer;
    private static final String FONT_NAME = "BOLD";
    private Items items;


    /**
     * Initialize NanoVG
     */
	public Screen2D() {
    	screen = nvgCreate( NVG_STENCIL_STROKES);
        if (screen == NULL) {
            try {
				throw new Exception("Could not init nanovg");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        try {
			fontBuffer = Utils.ioResourceToByteBuffer("fonts/OpenSans-Bold.ttf", 150 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
        int font = nvgCreateFontMem(screen, FONT_NAME, fontBuffer, 0);
        if (font == -1) {
            try {
				throw new Exception("Could not add font");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        colour = NVGColor.create();
        items = new Items(screen, colour);
    }

	public Items getItem() {
		Items item = new Items(this.items);
		return item;
	}

		
	public void cleanup() {
        nvgDelete(screen);
    }



}
