package uk.bl.dpt.pdfeh3F;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileDataSource;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;

public class PDFValidator {
	
	File source;
	StringBuilder resultBuilder;

	public PDFValidator(File source) {
		this.source = source;
		resultBuilder = new StringBuilder();
	}

	public String getResult() {
		return resultBuilder.toString();
	}
	
	public void validate() {
		long startTime = System.currentTimeMillis();

		ValidationResult result = null;
		String pdfType = null;

		try {
			FileDataSource fd = new FileDataSource(source);
			PreflightParser parser = new PreflightParser(fd);
			try {
				parser.parse();
				PreflightDocument document = parser.getPreflightDocument();
				document.validate();
				pdfType = document.getSpecification().getFname();
				result = document.getResult();
				document.close();
			} catch (SyntaxValidationException e) {
				result = e.getResult();
			}
		} catch (Exception e) {
			resultBuilder.append("<preflight name=\""
					+ source.getAbsolutePath() + "\">\n");
			resultBuilder.append("     <executionTimeMS>"
					+ (System.currentTimeMillis() - startTime)
					+ "</executionTimeMS>\n");
			resultBuilder.append("     <isValid type=\"" + pdfType
					+ "\">false</isValid>\n");
			resultBuilder.append("     <epicFail>\n");
			resultBuilder.append("          <message>" + e.getMessage()
					+ "</message>\n");
			// TODO catch stack trace here too?
            resultBuilder.append("          <stackTrace>\n");
            ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stackTrace));
            //stackTrace.flush();
            BufferedReader trace = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stackTrace.toByteArray()))); 
            try {
				while(trace.ready()) {
					resultBuilder.append(trace.readLine()+"\n");
				}
			} catch (IOException e1) {
			}
         	resultBuilder.append("          </stackTrace>\n");
			resultBuilder.append("     </epicFail>\n");
			resultBuilder.append("</preflight>\n");
		}

		if (result == null) {
			return;
		}

		if (result.isValid()) {
			resultBuilder.append("<preflight name=\""
					+ source.getAbsolutePath() + "\">\n");
			resultBuilder.append("     <executionTimeMS>"
					+ (System.currentTimeMillis() - startTime)
					+ "</executionTimeMS>\n");
			resultBuilder.append("     <isValid type=\"" + pdfType
					+ "\">true</isValid>\n");
			resultBuilder.append("</preflight>\n");
			return;
		} else {

			ArrayList<ValidationError> errorList = new ArrayList<ValidationError>();
			ArrayList<Integer> errorCount = new ArrayList<Integer>();

			List<ValidationError> errors = result.getErrorsList();
			while (!errors.isEmpty()) {
				ValidationError error = result.getErrorsList().get(0);
				boolean found = false;
				for (int i = 0; i < errorList.size(); i++) {
					ValidationError e2 = errorList.get(i);
					if (e2.getErrorCode().equals(error.getErrorCode())
							& e2.getDetails().equals(error.getDetails())) {
						found = true;
						errorCount.set(i, errorCount.get(i) + 1);
						break;
					}
				}
				if (!found) {
					errorList.add(error);
					errorCount.add(1);
				}
				errors.remove(0);
			}

			resultBuilder.append("<preflight name=\""
					+ source.getAbsolutePath() + "\">\n");
			resultBuilder.append("     <executionTimeMS>"
					+ (System.currentTimeMillis() - startTime)
					+ "</executionTimeMS>\n");
			resultBuilder.append("     <isValid type=\"" + pdfType
					+ "\">false</isValid>\n");
			for (int i = 0; i < errorList.size(); i++) {
				ValidationError error = errorList.get(i);
				int count = errorCount.get(i);
				resultBuilder.append("     <error>\n");
				resultBuilder.append("          <count>" + count + "</count>\n");
				resultBuilder.append("          <code>" + error.getErrorCode()
						+ "</code>\n");
				resultBuilder.append("          <details>" + error.getDetails()
						+ "</details>\n");
				resultBuilder.append("     </error>\n");
			}
			resultBuilder.append("</preflight>\n");

			return;
		}
	}
}
