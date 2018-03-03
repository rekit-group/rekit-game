package rekit.util;

import org.fuchss.tools.lambda.ConsumerWithException;
import org.fuchss.tools.lambda.FunctionWithException;
import org.fuchss.tools.lambda.LambdaConvert;
import org.fuchss.tools.lambda.ProducerWithException;
import org.fuchss.tools.lambda.VoidFunctionWithException;

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
		return LambdaConvert.wrap(in, e -> GameConf.GAME_LOGGER.fatal(e.getMessage())).apply(i);
	}

	/**
	 * Invoke a {@link VoidFunctionWithException}.
	 *
	 * @param in
	 *            the function
	 */

	public static void invoke(VoidFunctionWithException in) {
		LambdaConvert.wrap(in, e -> GameConf.GAME_LOGGER.fatal(e.getMessage())).execute();
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
		LambdaConvert.wrap(in, e -> GameConf.GAME_LOGGER.fatal(e.getMessage())).accept(i);

	}

	/**
	 * Invoke a {@link ProducerWithException}.
	 *
	 * @param in
	 *            the function
	 * @param <O>
	 *            the out type
	 * @return the return value of the Producer or {@code null} on failure
	 */
	public static <O> O invoke(ProducerWithException<O> in) {
		return LambdaConvert.wrap(in, e -> GameConf.GAME_LOGGER.fatal(e.getMessage())).produce();
	}

}
