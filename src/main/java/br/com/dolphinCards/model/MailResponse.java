package br.com.dolphinCards.model;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Mail Response", description = "The model for a Mail Response from the external API")
public class MailResponse {
    private String message;

    private Integer status;
}
