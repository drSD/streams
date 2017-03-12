package uk.pay4later.stream.json;

import java.io.IOException;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;


/**
 * Utility class for manipulating JSON data streams.
 * 
 * @author Simone Di Cola
 *
 */
public class JSONUtils {

	private static final Logger LOGGER = Logger.getLogger(JSONUtils.class.getName());

	/**
	 * Validates the {@link InputStream}'s structure according to provided JSON
	 * schema. Language details at <a href="http://json-schema.org/"> json-schema.org </a>.
	 * 
	 * @param in
	 *             the JSON stream to validate.
	 * @param schema
	 *             the JSON schema.
	 * @return true if well formed, false otherwise.
	 */
	public static boolean isJSONStructureValid(InputStream in, InputStream schema) {
		Validate.notNull(in, "The provided JSON stream is null");
		Validate.notNull(schema, "The provided JSON schema is null");
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode inputJsonNode = mapper.readTree(IOUtils.toString(in));
			final JsonNode schemaJsonNode = mapper.readTree(IOUtils.toString(schema));

			final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
			final JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(schemaJsonNode);

			final ProcessingReport report = jsonSchema.validate(inputJsonNode);

			if (report.isSuccess()) {
				return true;
			} else {
				LOGGER.log(Level.FINER, "The JSON stream is invalid {0}", report);
				return false;
			}
		} catch (IOException | ProcessingException exp) {
			LOGGER.log(Level.SEVERE, exp.getMessage(), exp);
		}
		return false;
	}

}
