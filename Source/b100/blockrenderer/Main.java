package b100.blockrenderer;

import static b100.blockrenderer.Utils.*;
import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import b100.utils.FileUtils;

public class Main implements Runnable{
	
	public static void main(String[] args) {
		new Main();
	}
	
	public JFrame frame;
	public Canvas canvas;
	public Renderer renderer;
	public TextureAtlas textureAtlas;
	public Timer timer = new Timer();
	
	private volatile boolean running = false;
	
	public int tex;
	
	public Main() {
		Thread thread = new Thread(this, "Main");
		thread.start();
		
	}
	
	public void init() throws Exception{
		frame = new JFrame("JFrame");
		canvas = new Canvas();
		
		canvas.setBackground(Color.black);
		canvas.setPreferredSize(new Dimension(512, 512));
		
		frame.add(canvas);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setVisible(true);
		
		Display.setParent(canvas);
		Display.create();
		
		renderer = new Renderer(this);

		textureAtlas = new TextureAtlas(16, 16, 16).setImage(ImageIO.read(new File("terrain.png")));
//		textureAtlas = new TextureAtlas(32, 16, 16).setImage(ImageIO.read(new File("items.png")));
		tex = Utils.setupTexture(textureAtlas.image);
		
	}
	
	public void export() throws Exception{
		System.out.println("Exporting Image...");
		int w = 16384;
		int h = 16384;
		
		//Generate Framebuffer
		int framebuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		
		//Setup Color Texture
		int colortex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, colortex);
		
		resetBuffer(buffer);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colortex, 0);
		
		//Setup Depth Texture
		int depthtex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, depthtex);
		
		resetBuffer(buffer);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, w, h, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthtex, 0);
		
		//Render
		GL11.glViewport(0, 0, w, h);
		glBindTexture(GL_TEXTURE_2D, tex);
		renderer.render();
		glBindTexture(GL_TEXTURE_2D, colortex);
		
		//Get Texture
		resetBuffer(buffer);
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		buffer.flip();
		buffer.position(0);
		buffer.limit(buffer.capacity());
		
		//Fill Image
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		int c = 0;
		for(int i=0; i < image.getWidth(); i++) {
			for(int j=0; j < image.getHeight(); j++) {
				byte r = buffer.get(c++);
				byte g = buffer.get(c++);
				byte b = buffer.get(c++);
				byte a = buffer.get(c++);
				
				int color = 0;
				
				color += (r << 16);
				color += (g << 8);
				color += (b << 0);
				color += (a << 24);
				
				image.setRGB(j, (h - i) - 1, color);
			}
		}
		
		//Save Image
		System.out.println("Saving Image...");
		File file = new File("render.png").getAbsoluteFile();
		FileUtils.createNewFile(file);
		ImageIO.write(image, "png", file);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		glDeleteFramebuffers(framebuffer);
		glDeleteTextures(colortex);
		System.out.println("Done!");
	}
	
	public void stop() {
		System.out.println("Stop!");
		Display.destroy();
		frame.dispose();
	}

	public void run() {
		try {
			init();
		}catch (Exception e) {
			throw new RuntimeException("Init", e);
		}
		
		try {
			running = true;
			while(running) {
				Display.update();
				timer.update();
				
				if(Display.isCloseRequested()) running = false;
				if(Display.wasResized()) GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
				if(!frame.isVisible()) running = false;
				
				while(Keyboard.next()) {
					if(Keyboard.getEventKeyState()) {
						keyPressed(Keyboard.getEventKey());
					}
				}
				
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
				glBindTexture(GL_TEXTURE_2D, tex);
				renderer.render();
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		stop();
	}
	
	public void keyPressed(int key) {
		System.out.println("KeyPressed: "+key);
		if(key == Keyboard.KEY_ESCAPE) running = false;
		if(key == Keyboard.KEY_F2) {
			try {
				export();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
