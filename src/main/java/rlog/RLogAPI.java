package rlog;

/**
 * Replaces the bruh-moment RLogAPI with a simple Syso rewrite
 * @author Pancake
 */
public class RLogAPI {

	public static void logDebug(String string) {
		System.out.println("[DEBUG] " + string);
	}

	public static void logError(Exception e, String string) {
		System.out.println("[ERROR] " + string);
		e.printStackTrace();
	}

}
