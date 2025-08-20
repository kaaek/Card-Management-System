package com.areeba.cms.config;

import com.areeba.cms.transaction.dto.TransactionResponseDTO;
import com.areeba.cms.transaction.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//
//        modelMapper.typeMap(Transaction.class, TransactionResponseDTO.class).addMappings(mapper ->
//                mapper.map(src -> src.getCard().getId(), TransactionResponseDTO::setCardId)
//        );
//
//        return modelMapper;
//    }
    // TODO: replace the above for records

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)                    // optional
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}

