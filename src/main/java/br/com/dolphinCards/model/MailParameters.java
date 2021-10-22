package br.com.dolphinCards.model;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailParameters {
    private String name;

    private String email;

    private Long flash_cards_for_the_day;
}
