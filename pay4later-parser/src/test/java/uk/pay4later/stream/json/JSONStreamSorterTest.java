package uk.pay4later.stream.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;

import uk.pay4later.stream.AbstractStreamSorterTest;

public class JSONStreamSorterTest extends AbstractStreamSorterTest<JSONObject> {

	private static final Logger LOGGER = Logger.getLogger(JSONStreamSorterTest.class.getName());

	private static final String JSON_DIR = "JSON/";
	private static final String JSON_FILE_EXT = ".json";
	private static final String JSON_SCHEMA_EXT = ".jsons";
	private static final String INVALID_JSON_URI = "users_invalid" + JSON_FILE_EXT;

	private JSONArray parsedExptectedStream;
	private JSONArray parsedActualStream;
	
	public JSONStreamSorterTest() {
		super(JSON_DIR, JSON_FILE_EXT, JSON_SCHEMA_EXT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		streamSorter = new JSONStreamSorter();
		comparator = Comparator.<JSONObject>comparingInt(obj -> Integer.valueOf(String.valueOf(obj.get(field))))
				.reversed();
	}

	@Override
	public void testInputStreamStructuralValidation() throws IOException {
		final InputStream invalidStream = loadResourceToByteArrayInputStream(INVALID_JSON_URI);
		JSONUtils.isJSONStructureValid(invalidStream, schemaInputStream);
		resetStreams();
	}

	@Override
	public void testSortByAnInvalidField() throws IOException {
		field = "user-id";
		streamSorter.sort(inputStream, outputStream, comparator);
		resetStreams();
	}

	@Override
	public void testSortInputStream() throws IOException {
		field = "user_id";
		streamSorter.sort(inputStream, outputStream, comparator);	
		parseStreamsToJSONArrays(parsedExptectedStream, parsedActualStream);	
		Assert.assertEquals(parsedExptectedStream, parsedActualStream);
	}

	@Override
	public void testSortInputStreamWithSchema() throws IOException {
		field = "user_id";
		streamSorter.sort(inputStream, outputStream, comparator,schemaInputStream); 
		parseStreamsToJSONArrays(parsedExptectedStream, parsedActualStream);
		Assert.assertEquals(parsedExptectedStream, parsedActualStream);
	}

	
	
	private void parseStreamsToJSONArrays(JSONArray parsedExptectedStream, JSONArray parsedActualStream) {
		try {
			final JSONParser jsonParser = new JSONParser();
			final Reader expectedStreamReader = new InputStreamReader(expectedInputStream);
			final Reader actualStreamReader = outputStreamToInputStreamReader(outputStream);

			parsedExptectedStream = (JSONArray) jsonParser.parse(expectedStreamReader);
			parsedActualStream = (JSONArray) jsonParser.parse(actualStreamReader);

		} catch (IOException | ParseException exp) {
			LOGGER.log(Level.SEVERE, exp.getMessage(), exp);
		}
	}
	
}
