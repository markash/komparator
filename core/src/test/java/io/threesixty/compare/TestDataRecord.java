package io.threesixty.compare;

import org.junit.Assert;
import org.junit.Test;

public class TestDataRecord {

    @Test
    public void testKeyPromotion() {

        DataRecord dataRecord = new DataRecord();
        dataRecord.addAttribute("Id", 1);
        dataRecord.addAttribute("Name", "Mark");

        Assert.assertTrue(dataRecord.getAttribute("Id").isPresent());
        Assert.assertTrue(!dataRecord.getAttribute("Id").get().isKey());
        Assert.assertTrue(dataRecord.getAttribute("Name").isPresent());
        Assert.assertTrue(!dataRecord.getAttribute("Name").get().isKey());

        dataRecord.promoteToKey("Id");

        Assert.assertTrue(dataRecord.getAttribute("Id").isPresent());
        Assert.assertTrue(dataRecord.getAttribute("Id").get().isKey());
        Assert.assertTrue(dataRecord.getAttribute("Name").isPresent());
        Assert.assertTrue(!dataRecord.getAttribute("Name").get().isKey());

        dataRecord.demoteFromKey("Id");

        Assert.assertTrue(dataRecord.getAttribute("Id").isPresent());
        Assert.assertTrue(!dataRecord.getAttribute("Id").get().isKey());
        Assert.assertTrue(dataRecord.getAttribute("Name").isPresent());
        Assert.assertTrue(!dataRecord.getAttribute("Name").get().isKey());
    }
}
