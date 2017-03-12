package uk.pay4later.stream.csv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import uk.gov.nationalarchives.csv.validator.api.java.CsvValidator;
import uk.gov.nationalarchives.csv.validator.api.java.FailMessage;
import uk.gov.nationalarchives.csv.validator.api.java.Substitution;

/**
 * Utility class for manipulating CSV data streams.
 * 
 * @author Simone Di Cola
 *
 */
public class CSVUtils {

	private static final Logger LOGGER = Logger.getLogger(CSVUtils.class.getName());

	/**
	 * Validates the {@link InputStream}'s structure according to provided CSV
	 * schema specified in CSV Schema Language. Language details at <a href="http://digital-preservation.github.io/csv-schema/csv-schema-1.1.html/"> csv-schema-language </a>.
	 * 
	 * @param csv
	 *             the CSV stream to validate.
	 * @param csvSchema
	 *             the CSV schema.
	 * @return true if well formed, false otherwise.
	 */
	public static boolean isCSVStructureValid(InputStream csv, InputStream csvSchema) {

		Validate.notNull(csv, "The provided csv is null");
		Validate.notNull(csvSchema, "The provided csv schema is null");
		
		final boolean failFast = true;
		final boolean trace = false;
		final Reader csvReader = new InputStreamReader(csv);
		final Reader csvSchemaReader = new InputStreamReader(csvSchema);

		final List<Substitution> pathSubtitution = new ArrayList<>();
		final List<FailMessage> messages = CsvValidator.validate(csvReader, csvSchemaReader, failFast, pathSubtitution,
				true, trace);
		if (messages.isEmpty()) {
			return true;
		} else {
			messages.forEach(m -> LOGGER.log(Level.SEVERE, m.getMessage()));
			return false;
		}
	}

}
