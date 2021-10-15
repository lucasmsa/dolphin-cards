package br.com.dolphinCards.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.dolphinCards.model.Discipline;
import br.com.dolphinCards.model.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DisciplineDTO {
    private String id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudentDTO creator; 

    public DisciplineDTO(Discipline discipline, StudentDTO studentDTO, boolean listingDisciplines) {
        this.id = discipline.getId();
        this.name = discipline.getName();
        if (!listingDisciplines) this.creator = studentDTO;
    }
}
