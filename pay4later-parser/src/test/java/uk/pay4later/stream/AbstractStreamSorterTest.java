package uk.pay4later.stream;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractStreamSorterTest<T> {

	private static final String TEST_FILE_SCHEMA = "users_schema";
	private static final String SOURCE_TEST_FILE = "users";
	private static final String SORTED_TEST_FILE = "sorted_users";

	private final String pathPrefix;
	private final String sourceStreamURI;
	private final String expectedSortedStreamURI;
	private final String inputStreamSchemaURI;

	protected InputStream inputStream;
	protected InputStream expectedInputStream;
	protected InputStream schemaInputStream;
	protected OutputStream outputStream;
	protected String field;
	protected Comparator<T> comparator;
	protected IStreamSorter<T> streamSorter;

	private final ClassLoader classLoader = getClass().getClassLoader();

	public AbstractStreamSorterTest(String folderName, String fileExt, String schemaFileExt) {
		pathPrefix = folderName;
		sourceStreamURI = SOURCE_TEST_FILE + fileExt;
		expectedSortedStreamURI = SORTED_TEST_FILE + fileExt;
		inputStreamSchemaURI = TEST_FILE_SCHEMA + schemaFileExt;
	}

	@Before
	public void setUp() throws Exception {
		inputStream = loadResourceToByteArrayInputStream(sourceStreamURI);
		schemaInputStream = loadResourceToByteArrayInputStream(inputStreamSchemaURI);
		expectedInputStream = loadResourceToByteArrayInputStream(expectedSortedStreamURI);
		outputStream = new ByteArrayOutputStream();
	}

	@Test(expected = NullPointerException.class)
	public void testSortMethodContract() throws IOException {
		streamSorter.sort(null, null, null);
	}

	@Test
	public abstract void testInputStreamStructuralValidation() throws IOException;

	@Test(expected = IllegalArgumentException.class)
	public abstract void testSortByAnInvalidField() throws IOException;

	@Test
	public abstract void testSortInputStream() throws IOException;

	@Test
	public abstract void testSortInputStreamWithSchema() throws IOException;

	/**
	 * Converts {@link File} to a {@link ByteArrayInputStream}.
	 * 
	 * @param file
	 *            the file to convert.
	 * @return the file content in a {@link ByteArrayInputStream}.
	 * @throws IOException
	 */
	protected ByteArrayInputStream loadResourceToByteArrayInputStream(String filePath) throws IOException {
		Validate.notBlank(filePath);
		final InputStream resource = classLoader.getResourceAsStream(pathPrefix + filePath);
		return new ByteArrayInputStream(IOUtils.toByteArray(resource));
	}

	/**
	 * Converts an {@link OutputStream} to an {@link InputStreamReader}.
	 * 
	 * @param outputStream
	 *            the {@link OutputStream}.
	 * @return the {@link BufferedReader}.
	 */
	protected Reader outputStreamToInputStreamReader(OutputStream outputStream) {
		Validate.notNull(outputStream);
		final byte[] outStreamContent = ((ByteArrayOutputStream) outputStream).toByteArray();
		return new BufferedReader(new InputStreamReader(IOUtils.toInputStream(outStreamContent.toString())));
	}

	/**
	 * Resets the streams under test to their initial position.
	 * 
	 * @throws IOException
	 */
	@After
	public void resetStreams() throws IOException {
		// Reset the input streams at the beginning
		inputStream.reset();
		schemaInputStream.reset();

		// Clear the output stream
		((ByteArrayOutputStream) outputStream).reset();
	}
}