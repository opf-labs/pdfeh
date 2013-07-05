package uk.bl.dpt.pdfagain;

public class HasRelationship {
	public String sid;
	public String tid;
	public String toString() {
		String s = sid;
		s = s.replace(",", "");
		String t = tid;
		t.replace(",", "");
		return s+","+t;
	}
}
