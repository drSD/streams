package uk.pay4later.stream.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.pay4later.stream.IStreamSorter;

public class JSONStreamSorter implements IStreamSorter<JSONObject> {

	private static final Logger LOGGER = Logger.getLogger(JSONStreamSorter.class.getName());

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<JSONObject> comparator) throws IOException {
		Validate.notNull(in, "The input stream is null.");
		Validate.notNull(out, "The output stream is null.");
		Validate.notNull(comparator, "The comparator is null.");
		
		final JSONParser jsonParser = new JSONParser();
		final InputStreamReader inputReader = new InputStreamReader(in);
		final OutputStreamWriter outputWriter = new OutputStreamWriter(out);
		try {
			final JSONArray jsonRecords = (JSONArray) jsonParser.parse(inputReader);

			Collections.sort(jsonRecords, comparator);

			jsonRecords.writeJSONString(outputWriter);
			outputWriter.flush();

		} catch (ParseException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<JSONObject> comparator, InputStream schema)
			throws IOException {
		Validate.notNull(schema, "The schema is null");
		Validate.validState(JSONUtils.isJSONStructureValid(in, schema));
		in.reset(); // Reset the input stream to its initial position
		sort(in, out, comparator);
	}
}
