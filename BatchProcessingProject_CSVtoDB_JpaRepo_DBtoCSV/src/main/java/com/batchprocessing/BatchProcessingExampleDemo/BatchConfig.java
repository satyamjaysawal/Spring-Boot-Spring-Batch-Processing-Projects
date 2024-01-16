package com.batchprocessing.BatchProcessingExampleDemo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private  DataSource dataSource;

    //step-1 : ItemiReader


    @Bean
	public ItemReader<Product> reader() {
		FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
		//1. Load file name + location
		reader.setResource(new ClassPathResource("products.csv"));
		//2. Read Data Line by Line
		reader.setLineMapper(new DefaultLineMapper<Product>() {{
			//3. tokenize data, provide names
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setDelimiter(DELIMITER_COMMA);
				setNames("prodId","prodCode","prodCost");
			}});

			//4. convert to object
			setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(Product.class);
			}});
		}});
		return reader;
	}

    //Step-2: Item Processor
    @Bean
    public ItemProcessor<Product, Product> processor(){
        //Configure and return the ItemProcessor
        return (item)->{
            //Your processing logic here
            double cost= item.getProdCost();
            item.setProdGst(cost * 12/100.0);
            item.setProdDisc(cost * 20/100.0);

            //Return the processed item
            return item;
        };
    }
  
    //Step-3: Item Writer
//    @Bean
//	public ItemWriter<Product> dbWriter() {
//		JdbcBatchItemWriter<Product> dbWriter = new JdbcBatchItemWriter<>();
//        dbWriter.setSql("INSERT INTO products (prod_id, prod_code, prod_cost, prod_gst, prod_disc) VALUES (:prodId, :prodCode, :prodCost, :prodGst, :prodDisc)");
//        dbWriter.setDataSource(dataSource);
//        dbWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//		return dbWriter;
//	}

    @Bean
    public ItemWriter<Product> dbWriter(ProductRepository productRepository) {
        return items -> productRepository.saveAll(items);
    }

    // CSV file writer bean
    @Bean
    public ItemWriter<Product> csvFileWriter() {
        FlatFileItemWriter<Product> writer = new FlatFileItemWriter<>();

        // Set the output resource
        writer.setResource(new FileSystemResource("OutputFiles/updatedproducts.csv"));

        // Set the header callback
        writer.setHeaderCallback(new ProductCsvHeaderCallback());

        // Set line aggregator and field extractor
        writer.setLineAggregator(new DelimitedLineAggregator<Product>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<Product>() {{
                setNames(new String[]{"prodId", "prodCode", "prodCost", "prodGst", "prodDisc"});
            }});
        }});

        return writer;
    }




    @Bean
    public CompositeItemWriter<Product> compositeWriter(ItemWriter<Product> dbWriter, ItemWriter<Product> csvFileWriter) {
        CompositeItemWriter<Product> compositeWriter = new CompositeItemWriter<>();
        compositeWriter.setDelegates(Arrays.asList(dbWriter, csvFileWriter));
        return compositeWriter;
    }

    //Step-4: JobExecutionListener
    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("ON STARTUP => " + jobExecution.getStatus());
                System.out.println("ON STARTUP => " + new Date());
                System.out.println("Job ID: " + jobExecution.getJobId());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("Job ID: " + jobExecution.getJobId());
                System.out.println("ON END => " + jobExecution.getStatus());
                System.out.println("ON END => " + new Date());

            }
        };
    }



    //Step-5: Define the Step
    @Bean
    public Step stepA(ItemReader<Product> reader, ItemProcessor<Product, Product> processor,
                      CompositeItemWriter<Product> compositeWriter) {
        return stepBuilderFactory.get("stepA")
                .<Product, Product>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(compositeWriter)
                .build();
    }

    // //Step-6: Define the Job
      @Bean
    public Job jobA(Step stepA, JobExecutionListener listener) {
        return jobBuilderFactory.get("jobA")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepA)
                .build();
    }


    class ProductCsvHeaderCallback implements org.springframework.batch.item.file.FlatFileHeaderCallback {
        @Override
        public void writeHeader(Writer writer) throws IOException {
            writer.write("prodId,prodCode,prodCost,prodGst,prodDisc");
        }


    }
}
