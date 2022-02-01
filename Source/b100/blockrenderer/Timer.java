package b100.blockrenderer;

public class Timer {
	
	public long lastTick = System.currentTimeMillis();
	public double delta = 0.0;
	public double elapsedTime = 0.0;
	
	public void update() {
		long now = System.currentTimeMillis();
		delta = (now - lastTick) / 1000.0;
		elapsedTime += delta;
		lastTick = now;
	}
	
}
