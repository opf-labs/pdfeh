package uk.bl.dpt.pdfeh3F;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class RuleParser {

	  SAXBuilder builder = new SAXBuilder();
	  File xmlFile;
	  String xmlFilename;
	  InputStream xmlInputStream = null;
	  
	  public RuleParser(String filePath) {
		  this.xmlFilename = filePath;
	  }
	  
	  public RuleParser(InputStream inputStream) {
		  this.xmlInputStream = inputStream;
	  }
	  
	  public Map<String, Rule> parse() {
		  
		  Map<String, Rule> rules = new HashMap<String, Rule>();
		  try {
			Document document = null;
			if(xmlInputStream!=null) {
				document = (Document) builder.build(xmlInputStream);
			} else {
				xmlFile = new File(xmlFilename);
				document = (Document) builder.build(xmlFile);
			}
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren();

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);
				String code = node.getAttributeValue("code");
				String name = node.getAttributeValue("name");
				boolean shouldFail = ("true".equals(node.getAttributeValue("shouldFail")) ? true : false);
				boolean shouldWarn = ("true".equals(node.getAttributeValue("shouldWarn")) ? true : false);;
//				System.out.println("Node : " + node.getName());
//				System.out.println("Name : " + name);
//				System.out.println("Code : " + code);
//				System.out.println("Should fail : " + shouldFail);
//				System.out.println("Should warn : " + shouldWarn);
				
				Rule rule = new Rule(code, name, name, shouldFail, shouldWarn);
				rules.put(rule.getErrorCode() + " " + rule.getErrorLabel(), rule);
			}
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		  
		  return rules;
	  }
	
}
