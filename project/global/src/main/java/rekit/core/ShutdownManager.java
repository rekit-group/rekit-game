package rekit.core;

import java.util.ArrayList;
import java.util.List;

import org.fuchss.tools.lambda.VoidFunctionWithException;

/**
 *
 * The shutdown manager of the system.
 *
 * @author Dominik Fuchss
 *
 */
public final class ShutdownManager {

	private ShutdownManager() {
		throw new IllegalAccessError();
	}

	private static boolean RUNNING = true;

	public static boolean isRunning() {
		return ShutdownManager.RUNNING;
	}

	private static final List<VoidFunctionWithException> HANDLERS = new ArrayList<>();

	public static synchronized void registerObserver(VoidFunctionWithException shutdownHandler) {
		ShutdownManager.HANDLERS.add(shutdownHandler);
	}

	public static synchronized void shutdown() {
		ShutdownManager.RUNNING = false;
		ShutdownManager.HANDLERS.forEach(ShutdownManager::execute);
		// If handlers finished. Kill VM.
		System.exit(0);
	}

	private static void execute(VoidFunctionWithException handler) {
		try {
			handler.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
