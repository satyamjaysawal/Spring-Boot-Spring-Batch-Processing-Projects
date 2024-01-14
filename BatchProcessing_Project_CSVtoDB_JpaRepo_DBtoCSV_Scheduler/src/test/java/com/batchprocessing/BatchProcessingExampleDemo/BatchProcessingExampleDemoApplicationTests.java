package com.batchprocessing.BatchProcessingExampleDemo;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BatchProcessingExampleDemoApplicationTests {

    @Autowired
    private ItemReader<Product> reader;

    @Test
    public void testItemReader() throws Exception {
        // Read items from the reader
        Product item = null;
        do {
            item = reader.read();
            if (item != null) {
                // Print or assert the item values
                System.out.println(item);
            }
        } while (item != null);
    }
}
