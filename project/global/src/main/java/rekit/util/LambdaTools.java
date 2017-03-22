package rekit.util;

import java.util.function.Consumer;
import java.util.function.Function;

import rekit.config.GameConf;

public class LambdaTools {
	private LambdaTools() {
	}

	@FunctionalInterface
	public interface RunnableWithException<E extends Throwable> {
		void apply() throws E;
	}

	@FunctionalInterface
	public interface FunctionWithException<I, O, E extends Throwable> {
		O apply(I i) throws E;
	}

	@FunctionalInterface
	public interface ConsumerWithException<I, E extends Throwable> {
		void apply(I i) throws E;
	}

	public static final <I, O, E extends Throwable> Function<I, O> tryCatch(Class<E> exType, FunctionWithException<I, O, E> in) {
		return i -> {
			try {
				return in.apply(i);
			} catch (Throwable t) {
				if (!exType.isInstance(t)) {
					throw new RuntimeException(t);
				}
				GameConf.GAME_LOGGER.error(t.getMessage());
				return null;
			}
		};
	}

	public static final <E extends Throwable> Runnable tryCatch(Class<E> exType, RunnableWithException<E> in) {
		return () -> {
			try {
				in.apply();
			} catch (Throwable t) {
				if (!exType.isInstance(t)) {
					throw new RuntimeException(t);
				}
				GameConf.GAME_LOGGER.error(t.getMessage());
			}
		};
	}

	public static final <I, E extends Throwable> Consumer<I> tryCatch(Class<E> exType, ConsumerWithException<I, E> in) {
		return i -> {
			try {
				in.apply(i);
			} catch (Throwable t) {
				if (!exType.isInstance(t)) {
					throw new RuntimeException(t);
				}
				GameConf.GAME_LOGGER.error(t.getMessage());
			}
		};
	}

}
