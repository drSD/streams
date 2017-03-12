package uk.pay4later.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;

public interface IStreamSorter<T> {

	/**
	 * Sorts elements of the {@link InputStream} by a field according to the
	 * provided {@link Comparator}. The result is written to the provided
	 * {@link OutputStream}.
	 * 
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @param comparator
	 *            the {@link Comparator}'s implementation
	 * @throws IOException
	 *             if streams cannot be read
	 */
	void sort(InputStream in, OutputStream out, Comparator<T> comparator) throws IOException;

	/**
	 * Sorts elements of a well-formed {@link InputStream} by a field according
	 * to the provided {@link Comparator}. The result is written to the provided
	 * {@link OutputStream}.
	 * 
	 * @param in
	 *             the input stream
	 * @param out
	 *             the output stream
	 * @param comparator
	 *             the {@link Comparator}'s implementation
	 * @param schema
	 *             the schema describing the structure of the
	 *            {@link InputStream} in.
	 * @throws IOException
	 *              if streams cannot be read
	 */
	void sort(InputStream in, OutputStream out, Comparator<T> comparator, InputStream schema) throws IOException;

}
