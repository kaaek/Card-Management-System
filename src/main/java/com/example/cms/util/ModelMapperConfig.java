package com.example.cms.util;

import com.example.cms.dto.transaction.TransactionResponseDTO;
import com.example.cms.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Transaction.class, TransactionResponseDTO.class).addMappings(mapper ->
                mapper.map(src -> src.getCard().getId(), TransactionResponseDTO::setCardId)
        );

        return modelMapper;
    }
}

