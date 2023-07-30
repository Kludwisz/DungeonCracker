package kludwisz.cracker;

public class ThreadHandler {
	private static Thread crackingThread = null;
	private static boolean resultsAvailable = false;
	
	public static void startRunning() {
		if (crackingThread == null || !crackingThread.isAlive()) {
			crackingThread = new Thread(new DungeonDataReverser());
			crackingThread.run();
		}
	}
	
	public static void stopRunning() {
		if (crackingThread != null && crackingThread.isAlive()) {
			resultsAvailable = false;
			crackingThread.interrupt();
		}
	}
	
	public static void updateResults() {
		
	}
}
