package com.webflux.jfgb.webflux.Domain;

import com.webflux.jfgb.webflux.Application.Models.DTO.HeadLineDTO;
import com.webflux.jfgb.webflux.Application.Models.DTO.SignatoriesDTO;
import com.webflux.jfgb.webflux.Application.Models.Enum.CustomerTypesEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "account")
public class BankAccount {
    @Id
    private String id;
    private String description;
    @Indexed(unique = true)
    private String number;
    private Double limitAccount;
    private String customerId;
    private CustomerTypesEnum customerType;
    private List<HeadLineDTO> headLineList;
    private List<SignatoriesDTO> signatories;
    public BankAccount(){
        headLineList = new ArrayList<>();
        signatories = new ArrayList<>();
    }
}