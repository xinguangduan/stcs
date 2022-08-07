package org.stcs.server.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@Slf4j
public class MongoConfig implements EnvironmentAware {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context,
                                                       BeanFactory beanFactory) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(beanFactory.getBean(MongoCustomConversions.class));
        // Don't save _class to
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        log.info(":::spring.data.mongodb.uri:{}", env.getProperty("spring.data.mongodb.uri"));
        return mappingConverter;
    }

    @Setter
    @Getter
    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }
}