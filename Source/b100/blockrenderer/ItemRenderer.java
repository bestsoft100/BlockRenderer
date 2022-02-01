package b100.blockrenderer;
import static org.lwjgl.opengl.GL11.*;

public class ItemRenderer {
	
	public ItemRenderer() {
		
	}
	
	public void renderItem(int textureIndex, int res, boolean light) {
		double brightnessTop = 1.0;
		double brightnessEastWest = 1.0;
		double brightnessNorthSouth = 1.0;
		double brightnessBottom = 1.0;
		
		if(light) {
			brightnessTop = 1.0;
			brightnessEastWest = 0.5;
			brightnessNorthSouth = 0.75;
			brightnessBottom = 0.25;
		}
		
		int tileX = textureIndex % 16;
		int tileY = textureIndex / 16;
		
		float uv_s = 1.0f / 16.0f;
		
		float u0 = tileX * uv_s;
		float v0 = tileY * uv_s;
		
		float u1 = u0 + uv_s;
		float v1 = v0 + uv_s;
		
		float tileWidth = 1.0f / 16.0f;
		float itemWidth = 1.0f / 16.0f;
		float wh = itemWidth / 2.0f;
		
		float w1 = 0.5f - wh;
		float w2 = 0.5f + wh;
		
//		w = 1.0f;
		float pixelSize = 1.0f / res;

		glColor3d(brightnessNorthSouth, brightnessNorthSouth, brightnessNorthSouth);
		glNormal3d(0, 0, -1);
		
		glTexCoord2d(u0, v1);
		glVertex3d(0, 0, w1);
		glTexCoord2d(u0, v0);
		glVertex3d(0, 1, w1);
		glTexCoord2d(u1, v1);
		glVertex3d(1, 0, w1);
		
		glTexCoord2d(u1, v0);
		glVertex3d(1, 1, w1);
		glTexCoord2d(u1, v1);
		glVertex3d(1, 0, w1);
		glTexCoord2d(u0, v0);
		glVertex3d(0, 1, w1);

		glColor3d(brightnessNorthSouth, brightnessNorthSouth, brightnessNorthSouth);
		glNormal3d(0, 0, 1);
		
		glTexCoord2d(u0, v1);
		glVertex3d(0, 0, w2);
		glTexCoord2d(u1, v1);
		glVertex3d(1, 0, w2);
		glTexCoord2d(u0, v0);
		glVertex3d(0, 1, w2);
		
		glTexCoord2d(u1, v0);
		glVertex3d(1, 1, w2);
		glTexCoord2d(u0, v0);
		glVertex3d(0, 1, w2);
		glTexCoord2d(u1, v1);
		glVertex3d(1, 0, w2);

		glColor3d(brightnessTop, brightnessTop, brightnessTop);
		glNormal3d(0, 1, 0);
		for(int i=1; i <= res; i++) {
			float f1 = v1 - (i * (pixelSize)) * tileWidth;
			float f2 = f1 + tileWidth * pixelSize;
			
			glTexCoord2d(u0, f1);
			glVertex3d(0, pixelSize * i, w1);
			glTexCoord2d(u0, f2);
			glVertex3d(0, pixelSize * i, w2);
			glTexCoord2d(u1, f1);
			glVertex3d(1, pixelSize * i, w1);
			
			glTexCoord2d(u1, f2);
			glVertex3d(1, pixelSize * i, w2);
			glTexCoord2d(u1, f1);
			glVertex3d(1, pixelSize * i, w1);
			glTexCoord2d(u0, f2);
			glVertex3d(0, pixelSize * i, w2);
		}

		glColor3d(brightnessBottom, brightnessBottom, brightnessBottom);
		glNormal3d(0, -1, 0);
		for(int i=0; i < res; i++) {
			float f1 = v1 - ((i+1) * (pixelSize)) * tileWidth;
			float f2 = f1 + tileWidth * pixelSize;
			
			glTexCoord2d(u0, f1);
			glVertex3d(0, pixelSize * i, w1);
			glTexCoord2d(u1, f1);
			glVertex3d(1, pixelSize * i, w1);
			glTexCoord2d(u0, f2);
			glVertex3d(0, pixelSize * i, w2);
			
			glTexCoord2d(u1, f2);
			glVertex3d(1, pixelSize * i, w2);
			glTexCoord2d(u0, f2);
			glVertex3d(0, pixelSize * i, w2);
			glTexCoord2d(u1, f1);
			glVertex3d(1, pixelSize * i, w1);
		}

		glColor3d(brightnessEastWest, brightnessEastWest, brightnessEastWest);
		glNormal3d(-1, 0, 0);
		for(int i=1; i <= res; i++) {
			float f1 = u0 + ((i-1) * (pixelSize)) * tileWidth;
			float f2 = f1 + tileWidth * pixelSize;
			
			glTexCoord2d(f1, v1);
			glVertex3d(pixelSize * i, 0, w1);
			glTexCoord2d(f1, v0);
			glVertex3d(pixelSize * i, 1, w1);
			glTexCoord2d(f2, v1);
			glVertex3d(pixelSize * i, 0, w2);
			
			glTexCoord2d(f2, v0);
			glVertex3d(pixelSize * i, 1, w2);
			glTexCoord2d(f2, v1);
			glVertex3d(pixelSize * i, 0, w2);
			glTexCoord2d(f1, v0);
			glVertex3d(pixelSize * i, 1, w1);
		}

		glColor3d(brightnessEastWest, brightnessEastWest, brightnessEastWest);
		glNormal3d(1, 0, 0);
		for(int i=0; i < res; i++) {
			float f1 = u0 + ((i) * (pixelSize)) * tileWidth;
			float f2 = f1 + tileWidth * pixelSize;
			
			glTexCoord2d(f1, v1);
			glVertex3d(pixelSize * i, 0, w1);
			glTexCoord2d(f2, v1);
			glVertex3d(pixelSize * i, 0, w2);
			glTexCoord2d(f1, v0);
			glVertex3d(pixelSize * i, 1, w1);
			
			glTexCoord2d(f2, v0);
			glVertex3d(pixelSize * i, 1, w2);
			glTexCoord2d(f1, v0);
			glVertex3d(pixelSize * i, 1, w1);
			glTexCoord2d(f2, v1);
			glVertex3d(pixelSize * i, 0, w2);
		}
	}
	
}
