package uk.bl.dpt.pdfeh3F;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;

public class RobotInDisguise {

	private String xsl;
	private String result; // from preflight
	private String robot;

	public RobotInDisguise(String result) {
		this.result = result;
		try {
			this.xsl = FileUtils.readFileToString(new File(
					"resources/pdfBoxPreflightValidator.xsl"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void transform() {
		OutputStream outputStream = new ByteArrayOutputStream();
		InputStream xslStream = new ByteArrayInputStream(xsl.getBytes());
		InputStream resStream = new ByteArrayInputStream(result.getBytes());

		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Source xslt = new StreamSource(xslStream);
			Transformer transformer = factory.newTransformer(xslt);
			Source text = new StreamSource(resStream);
			transformer.transform(text, new StreamResult(outputStream));
			robot = outputStream.toString();
		} catch (TransformerException e) {
			robot = "Transform failed " + e.getMessage();
		}
	}

	public String getResult() {
		return robot;
	}
}
