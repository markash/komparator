package io.threesixty.compare;

import io.threesixty.compare.reader.FileDataRecordReader;
import io.threesixty.compare.reader.ReaderConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

        new Comparison()
                .compare(sourcePersons, targetPersons, invoiceMapping)
                .forEach(r -> System.out.println("r = " + r));
    }
}
