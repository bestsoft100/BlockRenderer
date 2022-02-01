package b100.blockrenderer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

public class Renderer {
	
	public final Main main;
	
	public float backgroundRed = 0.5f;
	public float backgroundGreen = 0.8f;
	public float backgroundBlue = 1.0f;
	public float backgroundAlpha = 0.0f;
	
	public boolean perspective = false;
	public boolean light = false;
	public RenderType renderType = RenderType.ITEM;
	
	public CubeRenderer cubeRenderer = new CubeRenderer();
	public ItemRenderer itemRenderer = new ItemRenderer();
	
	public Renderer(Main main) {
		this.main = main;
	}
	
	public void render() {
		glClearColor(backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.1f);
		glEnable(GL_TEXTURE_2D);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		if(perspective) {
			GLU.gluPerspective(70.0f, 1.0f, 0.01f, 10.0f);
		}else {
			glOrtho(-1, 1, -1, 1, -10, 10);
		}
		
		double scale = 1.20;
		scale = 1.75;
		
		glScaled(scale, scale, scale);
		
		glRotated(22.5, 1, 0, 0);
		glRotated(45, 0, 1, 0);
//		glRotated(main.timer.elapsedTime * 45, 0, 1, 0);
		glTranslated(-0.5, -0.5, -0.5);
//		glTranslated(0, Math.sin(main.timer.elapsedTime * 3) * 0.1, 0);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glBegin(GL_TRIANGLES);
		if(renderType == RenderType.BLOCK) {
			cubeRenderer.renderSide(Side.BOTTOM, 2, !light);
			cubeRenderer.renderSide(Side.NORTH, 3, !light);
			cubeRenderer.renderSide(Side.EAST, 3, !light);
			cubeRenderer.renderSide(Side.SOUTH, 3, !light);
			cubeRenderer.renderSide(Side.WEST, 3, !light);
			cubeRenderer.renderSide(Side.TOP, 0, !light);
		}
		
		if(renderType == RenderType.ITEM) {
//			itemRenderer.renderItem(67, main.textureAtlas.textureResolution);
			itemRenderer.renderItem(14 * 16 + 1, main.textureAtlas.textureResolution, !light);
		}
		glEnd();
	}
	
}
