package br.com.dolphinCards.service.Email;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.dolphinCards.adapter.EmailSenderAdapter;
import br.com.dolphinCards.model.MailParameters;
import br.com.dolphinCards.model.MailResponse;
import br.com.dolphinCards.model.SendStudentsMail;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;

@Component
@EnableAsync
public class SendStudentsEmailWithFlashCardsForTheDayService {
    private StudentRepository studentRepository;
    private FlashCardsRepository flashCardsRepository;
    private ResponseEntity<?> mailSenderException = null;

    public SendStudentsEmailWithFlashCardsForTheDayService(StudentRepository studentRepository, 
                                   DisciplinesRepository disciplineRepository,
                                   FlashCardsRepository flashCardsRepository
                                ) { 
        this.studentRepository = studentRepository;
        this.flashCardsRepository = flashCardsRepository;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public ResponseEntity<?> run() {
        List<?> studentsObjects = studentRepository.findAllStudentEmails();
        for (Object studentObject : studentsObjects) {
            SendStudentsMail student = new SendStudentsMail().objectFieldsToSendStudentsMail(studentObject);
            Long flashCardsForTheDay = flashCardsRepository.countFlashCardsForTheDay(student.getId(), new Date());
            MailParameters mailParameters = new MailParameters(student.getName(), student.getEmail(), flashCardsForTheDay);
            ResponseEntity<?> emailSenderAdapter = new EmailSenderAdapter().forward(mailParameters);
            boolean emailSenderAdapterThrownException = emailSenderAdapter.getStatusCodeValue() == 400 || emailSenderAdapter.getStatusCodeValue() == 503;
            if (emailSenderAdapterThrownException) {
                this.mailSenderException = emailSenderAdapter;
                break;
            }
        }

        return this.mailSenderException != null ? mailSenderException : ResponseEntity.ok().body(new MailResponse("All e-mails were sent successfully ㊣", 200));
    }

}
