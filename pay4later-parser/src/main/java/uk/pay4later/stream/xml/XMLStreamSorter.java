package uk.pay4later.stream.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import uk.pay4later.stream.IStreamSorter;

/**
 * Implementation of the {@link IStreamSorter} that deals with streams of XML
 * data. If an XML schema is set, the provided XML stream is validated against
 * it.
 */
public class XMLStreamSorter implements IStreamSorter<Element> {

	private static final Logger LOGGER = Logger.getLogger(XMLStreamSorter.class.getName());

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<Element> comparator) throws IOException {
		Validate.notNull(in, "The input stream is null.");
		Validate.notNull(out, "The output stream is null.");
		Validate.notNull(comparator, "The comparator is null.");
		try {
			Document document = new SAXBuilder().build(in);
			Element rootElement = document.detachRootElement();

			List<Element> children = new ArrayList<Element>(rootElement.getChildren());
			rootElement.removeContent();

			Collections.sort(children, comparator);

			Document newDocument = new Document(rootElement);
			rootElement.addContent(children);

			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(newDocument, out);
			out.flush();
		}

		catch (JDOMException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<Element> comparator, InputStream schema)
			throws IOException {
		Validate.notNull(schema, "The schema is null");
		Validate.validState(XMLUtils.isXMLStructureValid(in, schema));
		in.reset(); // Reset the input stream to its initial position
        sort(in,out,comparator);
	}
}
