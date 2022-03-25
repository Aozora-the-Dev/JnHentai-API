package com.github.aozora.jnhentai.callbacks;

/**
 * https://stackoverflow.com/questions/15260596/generic-callback-in-java/15260988
 *
 * @param <T> the type of object that is passed to the callback function.
 */
public interface JnHentaiCallback<T> {
	/**
	 * @param t the object to obtained for the callback
	 */
	void call(T t);
}