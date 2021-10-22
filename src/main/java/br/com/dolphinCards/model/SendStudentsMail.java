package br.com.dolphinCards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendStudentsMail {
    private String id;

    private String email;

    private String name;

    public SendStudentsMail objectFieldsToSendStudentsMail(Object sendStudentsMailObject) {
        Object[] fields = (Object[]) sendStudentsMailObject;
        SendStudentsMail sendStudentsMail = new SendStudentsMail((String) fields[0], (String) fields[1], (String) fields[2]);

        return sendStudentsMail;
    }
}
