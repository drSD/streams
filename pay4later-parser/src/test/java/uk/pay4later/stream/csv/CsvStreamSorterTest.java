package uk.pay4later.stream.csv;

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;

import uk.pay4later.stream.AbstractStreamSorterTest;

public class CsvStreamSorterTest extends AbstractStreamSorterTest<CSVRecord> {

	private static final String CSV_DIR = "CSV/";
	private static final String CSV_FILE_EXT = ".csv";
	private static final String CSV_SCHEMA_EXT = ".csvs";
	private static final String INVALID_CSV_URI = "users_invalid" + CSV_FILE_EXT;

	private List<String> actualCSVRecords;
	private List<String> expectedCSVRecords;
	
	public CsvStreamSorterTest() {
		super(CSV_DIR, CSV_FILE_EXT, CSV_SCHEMA_EXT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		streamSorter = new CSVStreamSorter();
		comparator = (op1, op2) -> Integer.valueOf(op2.get(field)).compareTo(Integer.valueOf(op1.get(field)));
	}

	@Override
	public void testInputStreamStructuralValidation() throws IOException {
		InputStream invalidCSVStream = loadResourceToByteArrayInputStream(INVALID_CSV_URI);
		assertFalse(CSVUtils.isCSVStructureValid(invalidCSVStream,schemaInputStream));
	}

	@Override
	public void testSortByAnInvalidField() throws IOException {
		field = "User-ID";
		streamSorter.sort(inputStream, outputStream, comparator);
	}

	@Override
	public void testSortInputStream() throws IOException {
		field = "User ID";
		streamSorter.sort(inputStream, outputStream, comparator);
		parseCSVRecords(actualCSVRecords, expectedCSVRecords);
		Assert.assertEquals(expectedCSVRecords, actualCSVRecords);
	}

	@Override
	public void testSortInputStreamWithSchema() throws IOException {
		field = "User ID";
		streamSorter.sort(inputStream, outputStream, comparator, schemaInputStream);
		parseCSVRecords(actualCSVRecords, expectedCSVRecords);
		Assert.assertEquals(expectedCSVRecords, actualCSVRecords);
	}

	private void parseCSVRecords(List<String> actualCSVRecords, List<String> expectedCSVRecords) throws IOException {

		final Reader actualCSVReader = new BufferedReader(outputStreamToInputStreamReader(outputStream));
		final Reader expectedCSVReader = new BufferedReader(new InputStreamReader(expectedInputStream));

		actualCSVRecords = CSVFormat.RFC4180.parse(actualCSVReader).getRecords().stream().map(Object::toString)
				.map(StringUtils::deleteWhitespace).collect(Collectors.toList());

		expectedCSVRecords = CSVFormat.RFC4180.parse(expectedCSVReader).getRecords().stream().map(Object::toString)
				.map(StringUtils::deleteWhitespace).collect(Collectors.toList());
	}

}
