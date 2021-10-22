package br.com.dolphinCards.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Send Student Mail", description = "Necessary data to be fetched from the database to send to the external API")
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
