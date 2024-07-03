package vl.example.accountsmailsender.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vl.example.accountscommon.dto.AccountDTO;
import vl.example.accountscommon.dto.ClientDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class AccountRestControllerV1 {

//    private final JavaMailSender javaMailSender;

    @PostMapping
    public ResponseEntity<Integer> sendMail(@RequestBody List<AccountDTO> accountDTOs) {

        Map<ClientDTO, List<AccountDTO>> mails = accountDTOs.stream()
                .collect(Collectors.groupingBy(AccountDTO::getClient));

        for (Map.Entry<ClientDTO, List<AccountDTO>> entry : mails.entrySet()) {

            ClientDTO client = entry.getKey();
            List<AccountDTO> accounts = entry.getValue();

            StringBuilder text = new StringBuilder("Dear ").append(client.getName())
                    .append("\n")
                    .append(accounts.stream()
                            .map(account ->
                                    "Account: " + account.getNumber() +
                                            ", Coin : " + account.getCoin().getName() +
                                            ", Change : " + account.getCoin().getChange7d())
                            .collect(Collectors.joining("\n")))
                    .append("\nBest regards");

//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(client.getEmail());
//            mailMessage.setSubject("Action is required");
//            mailMessage.setText(text.toString());

//            javaMailSender.send(mailMessage);

            log.info("MAIL PROCESSOR. Mail processing: Mail to client {} has been sent", client.getName());
        }
        log.info("MAIL PROCESSOR. Mail processing: Data about {} accounts is processed", accountDTOs.size());

        return new ResponseEntity<>(accountDTOs.size(), HttpStatus.OK);
    }
}
