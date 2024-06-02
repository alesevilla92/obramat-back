package es.obramat.technicalTest.infrastructure.config;

import es.obramat.technicalTest.application.mappers.OrderMapper;
import es.obramat.technicalTest.application.mappers.ProductMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class CustomMapperConfig {

    @Bean
    public OrderMapper orderMapper() {
        return Mappers.getMapper(OrderMapper.class);
    }

    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }
}
