package b100.blockrenderer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
	
	public static ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE).order(ByteOrder.nativeOrder());
	
	public static void resetBuffer(ByteBuffer buffer) {
		buffer.limit(buffer.capacity());
		buffer.position(0);
	}
	
	public static void putImageInBuffer(ByteBuffer buffer, BufferedImage image) {
		resetBuffer(buffer);
		
		int[] rgb = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
		
		for(int i=0; i < rgb.length; i++) {
			int c = rgb[i];
			
			byte r = (byte) ((c >> 16) & 0xff);
			byte g = (byte) ((c >> 8) & 0xff);
			byte b = (byte) ((c >> 0) & 0xff);
			byte a = (byte) ((c >> 24) & 0xff);
			
			buffer.put(r);
			buffer.put(g);
			buffer.put(b);
			buffer.put(a);
		}
		
		buffer.limit(buffer.position());
		buffer.flip();
	}
	
	public static int setupTexture(BufferedImage image) {
		return setupTexture(0, image);
	}
	
	public static int setupTexture(int texture, BufferedImage image) {
		if(texture == 0) {
			texture = glGenTextures();
		}
		glBindTexture(GL_TEXTURE_2D, texture);
		
		putImageInBuffer(buffer, image);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		return texture;
	}
	
}
