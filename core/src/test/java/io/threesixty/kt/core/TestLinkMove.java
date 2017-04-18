package io.threesixty.kt.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

//import com.nhl.link.move.BaseRowAttribute;
//import com.nhl.link.move.RowAttribute;
//import com.nhl.link.move.RowReader;
//import com.nhl.link.move.connect.URIConnector;
//import com.nhl.link.move.extractor.Extractor;
//import com.nhl.link.move.extractor.model.MutableExtractorModel;
//import com.nhl.link.move.runtime.csv.CsvExtractorFactory;

public class TestLinkMove {

//	@Test
//	public void testExtract() throws Exception {
//		URIConnector connector = new URIConnector(this.getClass().getResource("/source-persons.csv").toURI());
//
//		MutableExtractorModel model = new MutableExtractorModel("testExtractorConfig");
//		model.setConnectorId("01");
//		model.setAttributes(
//				new BaseRowAttribute(Integer.class, "Id", "Id", 0), 
//				new BaseRowAttribute(String.class, "Name", "Name", 1), 
//				new BaseRowAttribute(Integer.class, "Age", "Age", 2));
//		
//		Map<String, ?> parameters = new HashMap<>();
//		
//		Extractor extractor = new CsvExtractorFactory().createExtractor(connector, model);
//		
//		RowReader reader = extractor.getReader(parameters);
//	
//		reader.forEach(action -> {
//			for (RowAttribute attr : action.attributes()) {
//				System.out.println(action.get(attr));
//			}
//		});
//	}
}
