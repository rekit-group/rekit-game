package rekit.util;

import java.util.function.Consumer;
import java.util.function.Function;

import rekit.config.GameConf;

/**
 * This class contains several methods to work with Lambdas.
 *
 * @author Dominik Fuchss
 *
 */
public final class LambdaUtil {
	/**
	 * Prevent instantiation.
	 */
	private LambdaUtil() {
	}

	/**
	 * This interface represents a {@link Runnable} with {@link Exception}.
	 *
	 * @author Dominik Fuchss
	 *
	 */
	@FunctionalInterface
	public interface RunnableWithException {
		/**
		 * Invoke function.
		 *
		 * @throws Exception
		 *             may thrown
		 */
		void apply() throws Exception;
	}

	/**
	 * This interface represents a {@link Function} with {@link Exception}.
	 *
	 * @author Dominik Fuchss
	 *
	 * @param <I>
	 *            the in type
	 * @param <O>
	 *            the out type
	 */
	@FunctionalInterface
	public interface FunctionWithException<I, O> {
		/**
		 * Invoke function.
		 *
		 * @param i
		 *            the input
		 * @return the output
		 * @throws Exception
		 *             may thrown
		 */
		O apply(I i) throws Exception;
	}

	/**
	 * This interface represents a {@link Consumer} with {@link Exception}.
	 *
	 * @author Dominik Fuchss
	 *
	 * @param <I>
	 *            the in type
	 */
	@FunctionalInterface
	public interface ConsumerWithException<I> {
		/**
		 * Invoke function.
		 *
		 * @param i
		 *            the input
		 * @throws Exception
		 *             may thrown
		 */
		void apply(I i) throws Exception;
	}

	/**
	 * Invoke a {@link FunctionWithException}.
	 *
	 * @param in
	 *            the function
	 * @param i
	 *            the input
	 * @param <I>
	 *            the in type
	 * @param <O>
	 *            the out type
	 *
	 * @return the output or {@code null} (in error case)
	 */
	public static <I, O> O invoke(FunctionWithException<I, O> in, I i) {
		return LambdaUtil.tryCatch(in).apply(i);
	}

	/**
	 * Invoke a {@link RunnableWithException}.
	 *
	 * @param in
	 *            the function
	 */

	public static void invoke(RunnableWithException in) {
		LambdaUtil.tryCatch(in).run();
	}

	/**
	 * Invoke a {@link ConsumerWithException}.
	 *
	 * @param in
	 *            the function
	 * @param i
	 *            the input
	 * @param <I>
	 *            the in type
	 */
	public static <I> void invoke(ConsumerWithException<I> in, I i) {
		LambdaUtil.tryCatch(in).accept(i);

	}

	/**
	 * Surround a function with try-catch.
	 *
	 * @param in
	 *            the {@link FunctionWithException}
	 * @param <I>
	 *            the in type
	 * @param <O>
	 *            the out type
	 * @return a new {@link Function}
	 */
	public static <I, O> Function<I, O> tryCatch(FunctionWithException<I, O> in) {
		return i -> {
			try {
				return in.apply(i);
			} catch (Exception e) {
				GameConf.GAME_LOGGER.fatal(e.getMessage());
				return null;
			}
		};
	}

	/**
	 * Surround a runnable with try-catch.
	 *
	 * @param in
	 *            the {@link RunnableWithException}
	 *
	 * @return a new {@link Runnable}
	 */
	public static Runnable tryCatch(RunnableWithException in) {
		return () -> {
			try {
				in.apply();
			} catch (Exception e) {
				GameConf.GAME_LOGGER.fatal(e.getMessage());
			}
		};

	}

	/**
	 * Surround a consumer with try-catch.
	 *
	 * @param in
	 *            the {@link ConsumerWithException}
	 * @param <I>
	 *            the in type
	 * @return a new {@link Consumer}
	 */
	public static <I> Consumer<I> tryCatch(ConsumerWithException<I> in) {
		return i -> {
			try {
				in.apply(i);
			} catch (Exception e) {
				GameConf.GAME_LOGGER.fatal(e.getMessage());
			}
		};

	}

	/**
	 * Get a {@link Consumer} which sends all to the void.
	 * 
	 * @param <I>
	 *            the input type
	 * @return a {@link Consumer} which sends all to the void
	 */
	public static <I> Consumer<I> destroy() {
		return t -> {
		};
	}

}
