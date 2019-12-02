package com.ecnu.meethere.common.config;

import com.ecnu.meethere.common.idgenerator.IdGenerator;
import com.ecnu.meethere.common.idgenerator.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {
    @Bean
    public IdGenerator idGenerator(@Value("${snowflake.dataCenterId:0}") int dataCenterId,
                                   @Value("${snowflake.workerId:0}") int workerId
                                            ) {
        return new SnowflakeIdGenerator(dataCenterId, workerId);
    }
}
