package br.com.dolphinCards.service.Email;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.adapter.EmailSenderAdapter;
import br.com.dolphinCards.model.MailParameters;
import br.com.dolphinCards.model.MailResponse;
import br.com.dolphinCards.model.SendStudentsMail;
import br.com.dolphinCards.model.Student;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service     
@EnableAsync
@RequiredArgsConstructor
public class SendStudentsEmailWithFlashCardsForTheDayService {
    private final EmailSenderAdapter emailSenderAdapter;
    private final StudentRepository studentRepository;
    private final FlashCardsRepository flashCardsRepository;
    private ResponseEntity<?> mailSenderException = null;

    @Scheduled(cron = "* 0 8 * * ?")
    public ResponseEntity<?> run() {        
        List<?> studentsObjects = studentRepository.findAllStudentEmails();

        for (Object studentObject : studentsObjects) {
            SendStudentsMail student = new SendStudentsMail().objectFieldsToSendStudentsMail(studentObject);
            Long flashCardsForTheDay = flashCardsRepository.countFlashCardsForTheDay(student.getId(), new Date());
            MailParameters mailParameters = new MailParameters(student.getName(), student.getEmail(), flashCardsForTheDay);
            ResponseEntity<?> emailSenderAdapterResponse = emailSenderAdapter.forward(mailParameters);
            boolean emailSenderAdapterThrownException = emailSenderAdapterResponse.getStatusCodeValue() == 400 || emailSenderAdapterResponse.getStatusCodeValue() == 503;
            if (emailSenderAdapterThrownException) {
                System.out.println("EmailSender thrown an exception: " + emailSenderAdapterResponse.getBody());
                this.mailSenderException = emailSenderAdapterResponse;
                break;
            }
        }

        return this.mailSenderException != null ? mailSenderException : ResponseEntity.ok().body(new MailResponse("All e-mails were sent successfully ㊣", 200));
    }

}
