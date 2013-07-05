package uk.bl.dpt.pdfagain;

public class GraphNode {
	public String id;
	public String type;
	public String name;
	public String desc = "-";

	public String toString() {
		String i = id;
		String t = type;
		String n = name;
		String d = desc;
		i = i.replace(",", "");
		t = t.replace(",", "");
		n = n.replace(",", "");
		d = d.replace(",", "");

		return i + "," + t + "," + n + "," + d;
	}
}
