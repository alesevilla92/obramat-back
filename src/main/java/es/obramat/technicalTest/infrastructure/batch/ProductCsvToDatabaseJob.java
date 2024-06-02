package es.obramat.technicalTest.infrastructure.batch;

import es.obramat.technicalTest.domain.model.Product;
import es.obramat.technicalTest.infrastructure.persistence.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductCsvToDatabaseJob {

    public static final Logger log = LoggerFactory.getLogger(ProductCsvToDatabaseJob.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("file:products.csv")
    private Resource inputFeed;

    @Bean(name = "insertIntoDbFromCsvJob")
    public Job insertIntoDbFromCsvJob(Step step1, Step step2) {

        var name = "Products Import Job";
        var builder = new JobBuilder(name, jobRepository);

        return builder.start(step1).build();
    }

    @Bean
    public Step step1(ItemReader<Product> reader,
                      ItemWriter<Product> writer,
                      ItemProcessor<Product, Product> processor,
                      PlatformTransactionManager txManager) {

        String name = "INSERT CSV RECORDS To DB Step";
        StepBuilder builder = new StepBuilder(name, jobRepository);
        return builder
                .<Product, Product>chunk(5, txManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Product> reader(
            LineMapper<Product> lineMapper) {
        var itemReader = new FlatFileItemReader<Product>();
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(inputFeed);
        itemReader.setLinesToSkip(1); // Ignore the headers
        return itemReader;
    }

    @Bean
    public DefaultLineMapper<Product> lineMapper(LineTokenizer tokenizer,
                                                 FieldSetMapper<Product> fieldSetMapper) {
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public BeanWrapperFieldSetMapper<Product> fieldSetMapper() {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<Product>();
        fieldSetMapper.setTargetType(Product.class);
        return fieldSetMapper;
    }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames("name", "description", "price");
        return tokenizer;
    }

    @Bean
    public ItemWriter<Product> jdbcItemWriter() {
        return items -> items.forEach(item -> {
            log.info("Inserting product {}", item.getName());
            Product databaseItem = productRepository.save(item);
            log.info("Inserted product {} with ID {}", databaseItem.getName(), databaseItem.getId());
        });
    }

    @Bean
    public ItemProcessor<Product, Product> productItemProcessor() {
        return item -> item;
    }
}