package com.webflux.jfgb.webflux.Application.Services.BankAccount;

import com.webflux.jfgb.webflux.Application.Models.DTO.CustomerDTO;
import com.webflux.jfgb.webflux.Application.Models.Enum.CustomerTypesEnum;
import com.webflux.jfgb.webflux.Domain.BankAccount;
import com.webflux.jfgb.webflux.Infrastructure.BankAccountRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.function.Function;
@Service
@Log4j2
public class BankAccountService implements IBankAccountService {

    private final WebClient webClient;
    public BankAccountService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    private static final Logger logger = LogManager.getLogger(BankAccountService.class);
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Flux<BankAccount> list() {
        logger.info("Listado de cuentas del banco");
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> register(BankAccount bankAccount) {
        logger.info("Registrando la cuenta: " + bankAccount);
        Function<Boolean, Mono<BankAccount>> exits = (Boolean b) -> {
            if (b) {
                return Mono.error(new Exception("Account bank already registered"));
            }

            if(bankAccount.getCustomerType().equals(CustomerTypesEnum.EMPRESARIAL) &&
                    bankAccount.getSignatories().size() >= 0 &&
                    bankAccount.getHeadLineList().size() > 0
            ){
                return bankAccountRepository.save(bankAccount);
            }

            if(!bankAccount.getCustomerType().equals(CustomerTypesEnum.EMPRESARIAL)){
                return bankAccountRepository.save(bankAccount);
            }

            else {
                return Mono.error(new Exception("Unauthorized Bank Account"));
            }
        };

        return bankAccountRepository.findByNumber(bankAccount.getNumber())
                .hasElement()
                .flatMap(exits);
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        logger.info("Cuenta encontrada con codigo: " + bankAccountRepository.findById(id));
        return bankAccountRepository.findById(id);
    }

    @Override
    public Mono<CustomerDTO> findByIdCustomer(Long ruc_dni) {
        //logger.info("Cuenta encontrada con codigo: " + bankAccountRepository.findById(id));
        CustomerDTO customer = new CustomerDTO();
        Mono<CustomerDTO> customerById = this.webClient.get().uri("/api/v1/customer/{id}", ruc_dni).retrieve().bodyToMono(CustomerDTO.class);
        return customerById.flatMap(x->{
            customer.setRuc_dni(x.getRuc_dni());
            customer.setName(x.getName());
            customer.setTypes(x.getTypes());
            Mono<CustomerDTO> cust = Mono.just(customer);
            return cust;
        });
    }

    @Override
    public Mono<BankAccount> updater(String id, BankAccount bankAccount) {
        logger.info("Actualizando la cuenta: " + bankAccount);
        return bankAccountRepository.findById(id)
                .flatMap(existingCredit -> {
                    existingCredit.setDescription(bankAccount.getDescription());
                    existingCredit.setLimitAccount(bankAccount.getLimitAccount());
                    existingCredit.setNumber(bankAccount.getNumber());
                    return bankAccountRepository.save(existingCredit);
                });
    }
}