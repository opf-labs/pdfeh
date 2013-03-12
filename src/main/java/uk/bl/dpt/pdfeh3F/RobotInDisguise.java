package uk.bl.dpt.pdfeh3F;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;

public class RobotInDisguise {

	private String xsl;
	private String result; // from preflight
	private String robot;

	public RobotInDisguise(String result) {
		this.result = result;
		try {
			InputStream is = RobotInDisguise.class.getClassLoader().getResourceAsStream("pdfBoxPreflightValildator.xsl");
			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer);
			xsl = writer.toString();			
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
	
	public String getXsl() {
		return xsl;
	}
}
