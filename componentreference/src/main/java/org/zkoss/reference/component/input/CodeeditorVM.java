package org.zkoss.reference.component.input;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * Backs the data-binding section of input/codeeditor.zul: the editor content is
 * two-way bound to {@link #getSource()}, and the deploy command parses whatever
 * the user typed so the page can report something the binding actually did.
 */
public class CodeeditorVM {
	private String source = "<beans>\n"
			+ "\t<bean id=\"dataSource\" class=\"com.example.PooledDataSource\">\n"
			+ "\t\t<property name=\"maxActive\" value=\"20\"/>\n"
			+ "\t</bean>\n"
			+ "</beans>";
	private String status = "not deployed yet";

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatus() {
		return status;
	}

	@Command
	@NotifyChange("status")
	public void deploy() {
		try {
			DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(source)));
			status = "deployed - " + source.length() + " chars of well-formed XML";
		} catch (Exception e) {
			status = "rejected - " + e.getMessage();
		}
	}
}
