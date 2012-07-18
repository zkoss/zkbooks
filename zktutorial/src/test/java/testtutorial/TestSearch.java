package testtutorial;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.operation.ClickAgent;
import org.zkoss.zats.mimic.operation.InputAgent;
import org.zkoss.zats.mimic.operation.SelectAgent;
import org.zkoss.zul.Label;

public class TestSearch {

	@BeforeClass
	public static void init() {
		Zats.init("./src/main/webapp"); // can load by configuration file
	}
	
	@Test
	public void test() {
		DesktopAgent desktop = Zats.newClient().connect("/search.zul");

		//enter keyword and click button
		String keyword = "java";
		ComponentAgent keywordBox = desktop.query("#keywordBox");
		keywordBox.as(InputAgent.class).type(keyword);
		desktop.query("#searchButton").as(ClickAgent.class).click();
		
		//find all listitems
		List<ComponentAgent> searchResult = desktop.queryAll("listitem");
		for (ComponentAgent listitem : searchResult){
			//select a listitem
			listitem.as(SelectAgent.class).select();
			String bookName = desktop.query("#nameLabel").as(Label.class).getValue();
			Assert.assertTrue(bookName.toLowerCase().contains(keyword));
		}
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}
}
