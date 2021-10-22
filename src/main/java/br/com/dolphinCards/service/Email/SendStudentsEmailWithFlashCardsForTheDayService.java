package br.com.dolphinCards.service.Email;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.dolphinCards.adapter.EmailSenderAdapter;
import br.com.dolphinCards.errors.Exceptions;
import br.com.dolphinCards.model.MailParameters;
import br.com.dolphinCards.model.MailResponse;
import br.com.dolphinCards.model.SendStudentsMail;
import br.com.dolphinCards.repository.DisciplinesRepository;
import br.com.dolphinCards.repository.FlashCardsRepository;
import br.com.dolphinCards.repository.StudentRepository;

@Service
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

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                return new Exceptions("Interrupted Exception from mail sending delay", 500).throwException();
            }
        }

        return this.mailSenderException != null ? mailSenderException : ResponseEntity.ok().body(new MailResponse("All e-mails were sent successfully ㊣", 200));
    }

}
