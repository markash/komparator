package io.threesixty.kt.core;

import io.threesixty.kt.core.reader.FileDataRecordReader;
import io.threesixty.kt.core.reader.ReaderConfiguration;
import io.threesixty.kt.core.result.ResultRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReaderConfiguration.class})
public class TestCompoundId {
    @Autowired
    private DataRecordConfiguration sourceInvoiceConfiguration;
    @Autowired
    private DataRecordConfiguration targetInvoiceConfiguration;
    @Autowired
    private AttributeMapping invoiceMapping;

    @Test
    public void testCompoundIdComparison() {
        FileDataRecordReader fileReader = new FileDataRecordReader();
        DataRecordSet sourcePersons = fileReader.read(sourceInvoiceConfiguration, this.getClass().getResourceAsStream("/source-invoice.csv"));
        DataRecordSet targetPersons = fileReader.read(targetInvoiceConfiguration, this.getClass().getResourceAsStream("/target-invoice.csv"));

        List<ResultRecord> results = new Comparison().compare(sourcePersons, targetPersons, invoiceMapping);
        results.forEach(r -> System.out.println("r = " + r));
    }
}
