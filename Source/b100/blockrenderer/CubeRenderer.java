package b100.blockrenderer;
import static org.lwjgl.opengl.GL11.*;

public class CubeRenderer {
	
	public void renderSide(Side side, int textureIndex, boolean light) {
		renderSide(side, textureIndex, 0, 0, 0, 1, 1, 1, light);
	}
	
	public void renderSide(Side side, int textureIndex, double x, double y, double z, double w, double h, double d, boolean light) {
		int tileX = textureIndex % 16;
		int tileY = textureIndex / 16;
		
		float uv_s = 1.0f / 16.0f;
		
		float u0 = tileX * uv_s;
		float v0 = tileY * uv_s;
		
		float u1 = u0 + uv_s;
		float v1 = v0 + uv_s;
		
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
		
		if(side == Side.TOP) {
			glColor3d(brightnessTop, brightnessTop, brightnessTop);
//			glColor3d(0.5, 1.0, 0.0);
			
			drawTopFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
		if(side == Side.BOTTOM) {
			glColor3d(brightnessBottom, brightnessBottom, brightnessBottom);
//			glColor3d(1.0, 0.5, 1.0);
			
			drawBottomFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
		if(side == Side.NORTH) {
			glColor3d(brightnessNorthSouth, brightnessNorthSouth, brightnessNorthSouth);
//			glColor3d(1.0, 1.0, 0.5);
			
			drawNorthFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
		if(side == Side.SOUTH) {
			glColor3d(brightnessNorthSouth, brightnessNorthSouth, brightnessNorthSouth);
//			glColor3d(0.5, 0.5, 1.0);
			
			drawSouthFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
		if(side == Side.EAST) {
			glColor3d(brightnessEastWest, brightnessEastWest, brightnessEastWest);
//			glColor3d(1.0, 0.5, 0.5);
			
			drawEastFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
		if(side == Side.WEST) {
			glColor3d(brightnessEastWest, brightnessEastWest, brightnessEastWest);
//			glColor3d(0.5, 1.0, 1.0);
			
			drawWestFace(x, y, z, w, h, d, u0, v0, u1, v1);
		}
	}
	
	public void drawTopFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(0, 1, 0);
		
		glTexCoord2d(u0, v0);
		glVertex3d(x, y + h, z);
		glTexCoord2d(u0, v1);		
		glVertex3d(x, y + h, z + d);
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y + h, z);
		
		glTexCoord2d(u1, v1);
		glVertex3d(x + w, y + h, z + d);
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y + h, z);
		glTexCoord2d(u0, v1);
		glVertex3d(x, y + h, z + d);
	}
	
	public void drawBottomFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(0, -1, 0);
		
		glTexCoord2d(u0, v0);
		glVertex3d(x, y, z);
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y, z);
		glTexCoord2d(u0, v1);		
		glVertex3d(x, y, z + d);
		
		glTexCoord2d(u1, v1);
		glVertex3d(x + w, y, z + d);
		glTexCoord2d(u0, v1);
		glVertex3d(x, y, z + d);
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y, z);
	}
	
	public void drawNorthFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(0, 0, -1);
		
		glTexCoord2d(u1, v1);
		glVertex3d(x, y, z);
		glTexCoord2d(u1, v0);		
		glVertex3d(x, y + h, z);
		glTexCoord2d(u0, v1);
		glVertex3d(x + w, y, z);
		
		glTexCoord2d(u0, v0);
		glVertex3d(x + w, y + h, z);
		glTexCoord2d(u0, v1);
		glVertex3d(x + w, y, z);
		glTexCoord2d(u1, v0);
		glVertex3d(x, y + h, z);
	}
	
	public void drawSouthFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(0, 0, 1);
		
		glTexCoord2d(u0, v1);
		glVertex3d(x, y, z + d);
		glTexCoord2d(u1, v1);
		glVertex3d(x + w, y, z + d);
		glTexCoord2d(u0, v0);		
		glVertex3d(x, y + h, z + d);
		
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y + h, z + d);
		glTexCoord2d(u0, v0);
		glVertex3d(x, y + h, z + d);
		glTexCoord2d(u1, v1);
		glVertex3d(x + w, y, z + d);
	}
	
	public void drawEastFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(1, 0, 0);
		
		glTexCoord2d(u1, v1);
		glVertex3d(x + w, y, z);
		glTexCoord2d(u1, v0);		
		glVertex3d(x + w, y + h, z);
		glTexCoord2d(u0, v1);
		glVertex3d(x + w, y, z + d);
		
		glTexCoord2d(u0, v0);
		glVertex3d(x + w, y + h, z + d);
		glTexCoord2d(u0, v1);
		glVertex3d(x + w, y, z + d);
		glTexCoord2d(u1, v0);
		glVertex3d(x + w, y + h, z);
	}
	
	public void drawWestFace(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glNormal3d(-1, 0, 0);
		
		glTexCoord2d(u0, v1);
		glVertex3d(x, y, z);
		glTexCoord2d(u1, v1);
		glVertex3d(x, y, z + d);
		glTexCoord2d(u0, v0);		
		glVertex3d(x, y + h, z);
		
		glTexCoord2d(u1, v0);
		glVertex3d(x, y + h, z + d);
		glTexCoord2d(u0, v0);
		glVertex3d(x, y + h, z);
		glTexCoord2d(u1, v1);
		glVertex3d(x, y, z + d);
	}
	
	public void drawFaceBase(double x, double y, double z, double w, double h, double d, double u0, double v0, double u1, double v1) {
		glTexCoord2d(u0, v0);
		glVertex3d(x, y, z);
		glTexCoord2d(u0, v0);		
		glVertex3d(x, y, z);
		glTexCoord2d(u0, v0);
		glVertex3d(x, y, z);
		
		glTexCoord2d(u1, v1);
		glVertex3d(x, y, z);
		glTexCoord2d(u0, v0);
		glVertex3d(x, y, z);
		glTexCoord2d(u0, v0);
		glVertex3d(x, y, z);
	}
		
}
