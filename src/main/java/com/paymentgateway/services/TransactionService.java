package com.paymentgateway.services;

import com.paymentgateway.domain.transaction.Transaction;
import com.paymentgateway.domain.user.User;
import com.paymentgateway.dtos.TransactionDTO;
import com.paymentgateway.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {
  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository repository;

  @Autowired
  private RestTemplate restTemplate;

  public void createTransaction(TransactionDTO transaction) throws Exception {
      User sender = this.userService.findUserById(transaction.senderId());
      User receiver = this.userService.findUserById(transaction.receiverId());

      userService.validateTransaction(sender, transaction.value());

      boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
      if(!isAuthorized) {
        throw new Exception("Transação não autorizada");
      }

      Transaction newTransaction = new Transaction();
      newTransaction.setAmount(transaction.value());
      newTransaction.setSender(sender);
      newTransaction.setReceiver(receiver);
      newTransaction.setTimestamp(LocalDateTime.now());

      sender.setBalance(sender.getBalance().subtract(transaction.value()));
      receiver.setBalance(receiver.getBalance().add(transaction.value()));

      this.repository.save(newTransaction);
      this.userService.saveUser(sender);
      this.userService.saveUser(receiver);
  }

  public boolean authorizeTransaction(User sender, BigDecimal value) {
   ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/87369035-ed43-421a-a09b-3b7f1d294b9d", Map.class);
   if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
     String message = (String) authorizationResponse.getBody().get("message");
     return "Autorizado".equalsIgnoreCase(message);
   } else return false;
  }

}
