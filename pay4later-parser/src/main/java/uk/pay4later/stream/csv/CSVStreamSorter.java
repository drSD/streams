package uk.pay4later.stream.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;

import uk.pay4later.stream.IStreamSorter;

public class CSVStreamSorter implements IStreamSorter<CSVRecord> {

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<CSVRecord> comparator) throws IOException {
		Validate.notNull(in, "The input stream is null.");
		Validate.notNull(out, "The output stream is null.");
		Validate.notNull(comparator, "The comparator is null.");
		
		final Reader inputStreamReader = new BufferedReader(new InputStreamReader(in));
		final List<CSVRecord> csvRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(inputStreamReader).getRecords();

		csvRecords.sort(comparator);

		final Writer outputStreamWriter = new BufferedWriter(new OutputStreamWriter(out));
		final CSVPrinter cvsPrinter = CSVFormat.RFC4180.print(outputStreamWriter);
		cvsPrinter.printRecords(csvRecords);
		cvsPrinter.flush();
	}

	@Override
	public void sort(InputStream in, OutputStream out, Comparator<CSVRecord> comparator, InputStream schema)
			throws IOException {
		Validate.notNull(schema, "The schema is null");
		Validate.validState(CSVUtils.isCSVStructureValid(in, schema));
		in.reset(); // Reset the input stream to its initial position
        sort(in,out,comparator);	
	}
}
