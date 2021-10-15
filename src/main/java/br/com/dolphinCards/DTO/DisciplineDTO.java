package br.com.dolphinCards.DTO;

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

    private StudentDTO creator; 

    public DisciplineDTO(Discipline discipline, StudentDTO studentDTO) {
        this.id = discipline.getId();
        this.name = discipline.getName();
        this.creator = studentDTO;
    }
}
