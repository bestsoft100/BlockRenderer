package b100.blockrenderer;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public class BlockRenderer {
	
	public FloatBuffer floatBuffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	public CubeRenderer blockRenderer = new CubeRenderer();
	public ItemRenderer itemRenderer = new ItemRenderer();
	
	public boolean pause = true;
	
	public int rotationX = 0, rotationY = 0;
	public boolean perspective = false;
	public float fov = 70.0f;
	public boolean light = false;
	public boolean lightBackground = true;
	public boolean cullFace = true;
	public boolean zoom = false;
	public RenderType renderType = RenderType.BLOCK;
	
	public BlockRenderer() {
		try {
			Display.setDisplayMode(new DisplayMode(1024, 1024));
			Display.setResizable(false);
			Display.create();
			Display.setTitle("Block Renderer");

			glEnable(GL_TEXTURE_2D);
			
			int tex = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, tex);
			
			TextureAtlas textureAtlas = new TextureAtlas(16, 16, 16);
			Utils.setupTexture(textureAtlas.image);
			
			long lastTick = System.currentTimeMillis();
			
			double elapsedTime = 0.0;
			
			boolean running = true;
			
			while(running) {
				
				long now = System.currentTimeMillis();
				double delta = (now - lastTick) / 1000.0;
				if(!pause) elapsedTime += delta;
				lastTick = now;
				
				Display.update();
				if(Display.wasResized()) {
					glViewport(0, 0, Display.getWidth(), Display.getHeight());
				}
				if(Display.isCloseRequested()) {
					running = false;
				}
				
				while(Keyboard.next()) {
					if(Keyboard.getEventKeyState()) {
						int key = Keyboard.getEventKey();
						if(key == Keyboard.KEY_ESCAPE) {
							running = false;
						}
						if(key == Keyboard.KEY_1) {
							elapsedTime = 0;
							rotationX = 0;
							rotationY = 0;
						}
						if(key == Keyboard.KEY_2) {
							elapsedTime = 0;
							rotationX = -450;
							rotationY = -300;
						}
						if(key == Keyboard.KEY_3) {
							elapsedTime = 0;
							rotationX = 450;
							rotationY = -300;
						}
						if(key == Keyboard.KEY_P) pause = !pause;
						if(key == Keyboard.KEY_F) perspective = !perspective;
						if(key == Keyboard.KEY_L) light = !light;
						if(key == Keyboard.KEY_B) lightBackground = !lightBackground;
						if(key == Keyboard.KEY_C) cullFace= !cullFace;
						if(key == Keyboard.KEY_Z) zoom= !zoom;
					}
				}
				
				if(perspective) {
					float mod = 10.0f;
					if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
						mod = 100.0f;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						mod = 1.0f;
					}
					
					if(Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
						fov += delta * mod;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
						fov -= delta * mod;
					}
				}
				
				int mx = Mouse.getDX();
				int my = Mouse.getDY();
				
				if(Mouse.isButtonDown(0)) {
					rotationX += mx;
					rotationY += my;
				}
				
				if(lightBackground) {
					glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
				}else {
					glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				}
				
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glEnable(GL_DEPTH_TEST);
				glEnable(GL_ALPHA_TEST);
				glAlphaFunc(GL_GREATER, 0.1f);
				if(cullFace) {
					glEnable(GL_CULL_FACE);
					glCullFace(GL_BACK);
				}else {
					glDisable(GL_CULL_FACE);
				}
				
				
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				
				if(perspective) {
					GLU.gluPerspective(fov, 1.0f, 0.01f, 10.0f);
				}else {
					glOrtho(-1, 1, -1, 1, -10, 10);
				}
				
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				
				glPushMatrix();
				
				if(perspective) {
					glTranslated(0, 0, -3);
				}
				
				double scale = 1.20;
				if(zoom) scale = 1.75;
				glScaled(scale, scale, scale);

				glRotated(rotationY * -0.1, 1, 0, 0);
				
//				glRotated(30.0, 1, 0, 0);
//				glRotated(-45.0, 0, 1, 0);
//				glRotated(elapsedTime * 60, 0, 1, 0);

				glRotated(rotationX * 0.1, 0, 1, 0);
				
				glTranslated(-0.5, -0.5, -0.5);

				if(light) {
					setupLighting();
					
				}else {
					glDisable(GL_LIGHTING);
				}
				
				glBegin(GL_TRIANGLES);
				
				if(renderType == RenderType.BLOCK) {
					blockRenderer.renderSide(Side.TOP, iconIndex(0, 10), 0, 0, 0, 1, 1, 1, !light);
					blockRenderer.renderSide(Side.NORTH, iconIndex(1, 10), 0, 0, 0, 1, 1, 1, !light);
					blockRenderer.renderSide(Side.EAST, iconIndex(1, 10), 0, 0, 0, 1, 1, 1, !light);
					blockRenderer.renderSide(Side.SOUTH, iconIndex(1, 10), 0, 0, 0, 1, 1, 1, !light);
					blockRenderer.renderSide(Side.WEST, iconIndex(1, 10), 0, 0, 0, 1, 1, 1, !light);
					blockRenderer.renderSide(Side.BOTTOM, iconIndex(2, 10), 0, 0, 0, 1, 1, 1, !light);
				}
				
				if(renderType == RenderType.ITEM) {
//					itemRenderer.renderItem(67);
				}
				
				glEnd();
				
				glPopMatrix();
			}
			
			System.out.println("Stop");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupLighting() {
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
//		glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		
		glLight(GL_LIGHT0, GL_POSITION, putFloats(0.0, 2.0, -1.0, 0.0));
		glLight(GL_LIGHT0, GL_DIFFUSE, putFloats(1.0, 1.0, 1.0, 1.0));
		glLight(GL_LIGHT0, GL_AMBIENT, putFloats(0.1, 0.1, 0.1, 1.0));
		glLight(GL_LIGHT0, GL_SPECULAR, putFloats(0.0, 0.0, 0.0, 1.0));
		
		glLight(GL_LIGHT1, GL_POSITION, putFloats(0.0, 2.0, 1.0, 0.0));
		glLight(GL_LIGHT1, GL_DIFFUSE, putFloats(1.0, 1.0, 1.0, 1.0));
		glLight(GL_LIGHT1, GL_AMBIENT, putFloats(0.1, 0.1, 0.1, 1.0));
		glLight(GL_LIGHT1, GL_SPECULAR, putFloats(0.0, 0.0, 0.0, 1.0));
		
		glShadeModel(GL_SMOOTH);
	}
	
	public static int iconIndex(int x, int y) {
		return y * 16 + x;
	}
	
	public FloatBuffer putFloats(double d1, double d2, double d3, double d4) {
		return putFloats((float)d1, (float)d2, (float)d3, (float)d4);
	}
	
	public FloatBuffer putFloats(float f1, float f2, float f3, float f4) {
		floatBuffer.position(0);
		floatBuffer.limit(floatBuffer.capacity());
		floatBuffer.put(f1);
		floatBuffer.put(f2);
		floatBuffer.put(f3);
		floatBuffer.put(f4);
		floatBuffer.limit(floatBuffer.position());
		floatBuffer.flip();
		return floatBuffer;
	}
	
}
