package uk.bl.dpt.pdfeh3F;

import java.util.HashMap;
import java.util.Map;

import uk.bl.dpt.dpdfdgy.RuleParser;
import uk.bl.dpt.dpdfdgy.Rules;

public class Rules {
	
	//TODO Might be better to keep the Map hidden and answer to getRules() with a list, etc.
	//TODO Save Rules file?
	//TODO Load Rules by a file - hardcoded or (better) passed from GUI
	
	private Map<String, Rule> rules;

	public Rules() {
		rules = new HashMap<String, Rule>();
		//TODO replace with real read of rules XML
		initRules();
	}
	
	public Map<String, Rule> getRules() {
		return rules;
	}
	
	private void initDummyRules() {
		rules.put("2.4.3: Invalid Colour Space", new Rule("2.4.3", "Invalid Colour Space", "", true, true));
		rules.put("3.1.3: Invalid Font Definition", new Rule("3.1.3", "Invalid Font Definition", "", false, false));
		rules.put("7.11: Error on Metadata", new Rule("7.11", "Error On Metadata", "", true, true));
	}
	
	private void initRules() {
		String xmlFilename = Rules.class.getClassLoader().getResource("PdfErrorPolicy.xml").getFile();
		RuleParser ruleParser = new RuleParser(xmlFilename);
		rules = ruleParser.parse();
		
	}
}
