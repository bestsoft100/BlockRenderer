package b100.blockrenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextureAtlas {
	
	private static final Color missingTexColor1 = new Color(214, 127, 255);
	private static final Color missingTexColor2 = new Color(107, 63, 127);
	
	public final int textureResolution;
	
	public final int width;
	public final int height;
	
	public BufferedImage image;
	
	public TextureAtlas(int textureResolution, int width, int height) {
		this.textureResolution = textureResolution;
		this.width = width;
		this.height = height;
	}
	
	public TextureAtlas createDefaultAtlas() {
		this.image = new BufferedImage(textureResolution * width, textureResolution * height, BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();
		
		for(int i=0; i < width; i++) {
			for(int j=0; j < height; j++) {
				g.setColor(missingTexColor1);
				g.fillRect(i * textureResolution, j * textureResolution, textureResolution, textureResolution);
				
				g.setColor(missingTexColor2);
				g.fillRect(i * textureResolution, j * textureResolution, textureResolution, 1);
				g.fillRect(i * textureResolution, j * textureResolution, 1, textureResolution);
			}
		}
		
		return this;
	}
	
	public TextureAtlas setImage(BufferedImage image) {
		if(image.getWidth() != getImageWidth() || image.getHeight() != getImageHeight()) {
			throw new RuntimeException("Invalid image size "+image.getWidth()+" x "+image.getHeight()+"! Must be "+getImageWidth()+" x "+getImageHeight());
		}
		
		this.image = image;
		
		return this;
	}
	
	public int getImageWidth() {
		return textureResolution * width;
	}
	
	public int getImageHeight() {
		return textureResolution * height;
	}
	
}
