package com.batchprocessing.BatchProcessingExampleDemo;

import java.io.IOException;
import java.io.Writer;

public class ProductCsvHeaderCallback implements org.springframework.batch.item.file.FlatFileHeaderCallback {
    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write("prodId,prodCode,prodCost,prodGst,prodDisc");
    }
}
