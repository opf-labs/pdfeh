package uk.bl.dpt.pdfagain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	// INPUT PATH
	private final String path = "/home/pcliff/Projects/SPRUCE/wrapup/output";

	// OUTPUT FILES
	private File nodefile = new File("/home/pcliff/Projects/SPRUCE/wrapup/nodes.csv");
	private File relsfile = new File("/home/pcliff/Projects/SPRUCE/wrapup/rels.csv");

	private DocumentBuilderFactory factory;
	private XPathFactory xpathFactory;
	private List<PreflightReport> reports;
	private Map<String, GraphNode> graphnodes;
	private List<HasRelationship> hasrels;
	
	public static void main(String[] args) {
		Main mi = new Main();
		try {
			mi.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void go() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		nodefile.delete();
		relsfile.delete();
		
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		xpathFactory = XPathFactory.newInstance();

		reports = new ArrayList<PreflightReport>();
		graphnodes = new HashMap<String, GraphNode>();
		hasrels = new ArrayList<HasRelationship>();

		File dir = new File(path);
		for (File f : dir.listFiles()) {
			System.out.println("Loading " + f.getAbsolutePath());
			parse(f);
			System.out.println("Parsed: " + reports.size());
		}

		for (PreflightReport r : reports) {
			GraphNode itemNode = new GraphNode();
			itemNode.id = genId(r.name);
			itemNode.name = r.name;
			itemNode.type = "ITEM";
			graphnodes.put(itemNode.id, itemNode);

			for (Error e : r.errors) {
				// Populate error nodes
				GraphNode errorNode = new GraphNode();
				errorNode.id = e.code;
				errorNode.name = e.code;
				errorNode.desc = e.description;
				errorNode.type = "ERROR";
				if (!graphnodes.containsKey(errorNode.id)) {
					graphnodes.put(errorNode.id, errorNode);
				}
				// Create relationships
				HasRelationship rel = new HasRelationship();
				rel.sid = itemNode.id;
				rel.tid = errorNode.id;
				hasrels.add(rel);
			}

		}
		int i = 0;
		FileUtils.writeStringToFile(nodefile, "id,type,label,description" + "\n", true);
		for (String key : graphnodes.keySet()) {
			i++;
			GraphNode n = graphnodes.get(key);
			FileUtils.writeStringToFile(nodefile, n.toString() + "\n", true);
		}
		System.out.println("Created " + i + " nodes");
		i = 0;
		FileUtils.writeStringToFile(relsfile, "Source,Target" + "\n", true);
		for (HasRelationship rel : hasrels) {
			i++;
			FileUtils.writeStringToFile(relsfile, rel.toString() + "\n", true);
		}
		System.out.println("Created " + i + " relationships");

	}

	private String genId(String name) {
		name = name.replace(" ", "");
		name = name.toLowerCase();
		return name;
	}

	private void parse(File f) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException {
		DocumentBuilder builder;
		Document doc = null;
		builder = factory.newDocumentBuilder();
		doc = builder.parse(f);

		String name = getName(doc);
		String valid = getValid(doc);
		List<Error> errors = getErrors(doc);

		PreflightReport r = new PreflightReport();
		r.errors = errors;
		r.name = name;
		r.valid = valid;

		reports.add(r);
	}

	private List<Error> getErrors(Document doc)
			throws XPathExpressionException {
		List<Error> errors = new ArrayList<Error>();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr = null;
		expr = xpath.compile("//error/*");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		// Cast the result to a DOM NodeList
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength() - 1; i++) {
			Node cnode = nodes.item(i);
			Node dnode = nodes.item(i + 1);
			Error e = new Error();
			e.code = cnode.getChildNodes().item(0).getNodeValue();
			e.description = dnode.getChildNodes().item(0).getNodeValue();
			errors.add(e);
			i++;
		}
		return errors;
	}

	private String getValid(Document doc) throws XPathExpressionException {
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr = null;

		expr = xpath.compile("//preflight/isValid/text()");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		// Cast the result to a DOM NodeList
		NodeList nodes = (NodeList) result;

		String value = nodes.item(0).getNodeValue();
		return value;
	}

	private String getName(Document doc) throws XPathExpressionException {
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr = null;

		expr = xpath.compile("//@name");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		// Cast the result to a DOM NodeList
		NodeList nodes = (NodeList) result;

		String name = nodes.item(0).getNodeValue();
		return name;
	}
}
