package uk.pay4later.stream.xml;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.Validate;

/**
 * Utility class for manipulating XML streams.
 * 
 * @author Simone Di Cola
 *
 */
public class XMLUtils {

	private static final Logger LOGGER = Logger.getLogger(XMLUtils.class.getName());

	/**
	 * Validates the {@link InputStream}'s structure according to provided XML
	 * schema.
	 * 
	 * @param xml
	 *             the XML stream to validate.
	 * @param xmlSchema
	 *             the XML schema.
	 * @return true if well formed, false otherwise.
	 */
	public static boolean isXMLStructureValid(InputStream xml, InputStream xmlSchema) {
		Validate.notNull(xml, "The provided xml is null");
		Validate.notNull(xmlSchema, "The provided xml schema is null");

		try {
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final Schema schema = factory.newSchema(new StreamSource(xmlSchema));
			final Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
			return true;
		} catch (Exception exp) {
			LOGGER.log(Level.SEVERE, exp.getMessage(), exp);
			return false;
		}
	}
}
