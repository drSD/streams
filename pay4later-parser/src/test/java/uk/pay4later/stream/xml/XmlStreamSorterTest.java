package uk.pay4later.stream.xml;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

import org.jdom2.Element;
import org.junit.Before;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import uk.pay4later.stream.AbstractStreamSorterTest;

public class XmlStreamSorterTest extends AbstractStreamSorterTest<Element> {

	private static final String XML_DIR = "XML/";
	private static final String XML_FILE_EXT = ".xml";
	private static final String XML_SCHEMA_EXT = ".xsd";

	private Diff xmlDiff;
	
	public XmlStreamSorterTest() {
		super(XML_DIR, XML_FILE_EXT, XML_SCHEMA_EXT);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		streamSorter = new XMLStreamSorter();
		comparator = Comparator.<Element>comparingInt(obj -> Integer.valueOf(String.valueOf(obj.getChildText(field))))
				.reversed();
	}


	@Override
	public void testInputStreamStructuralValidation() throws IOException {
		final InputStream malformedXML = new ByteArrayInputStream("<users><user></user></users>".getBytes());
		assertFalse(XMLUtils.isXMLStructureValid(malformedXML, schemaInputStream));
	}

	@Override
	public void testSortByAnInvalidField() throws IOException {
		field = "USER ID";
		streamSorter.sort(inputStream, outputStream, comparator);
	}

	@Override
	public void testSortInputStream() throws IOException {
		streamSorter.sort(inputStream, outputStream, comparator);
		xmlDiff = DiffBuilder.compare(expectedInputStream).withTest(outputStream.toString()).ignoreComments()
				.ignoreWhitespace().build();
		assertFalse("The resulting XML is not correctly sorted", xmlDiff.hasDifferences());
	}

	@Override
	public void testSortInputStreamWithSchema() throws IOException {
		streamSorter.sort(inputStream, outputStream, comparator, schemaInputStream);
		xmlDiff = DiffBuilder.compare(expectedInputStream).withTest(outputStream.toString()).ignoreComments()
				.ignoreWhitespace().build();
		assertFalse("The resulting XML is not correctly sorted", xmlDiff.hasDifferences());
	}

}
