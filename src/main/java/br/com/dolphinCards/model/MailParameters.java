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
@ApiModel(value = "Mail Parameters", description = "The model for a Mail Parameter which will be used to call the external API")
public class MailParameters {
    private String name;

    private String email;

    private Long flash_cards_for_the_day;
}
