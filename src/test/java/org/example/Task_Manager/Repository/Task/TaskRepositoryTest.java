package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.specification.TaskSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
@BeforeEach
//    void setUp(){
//        taskRepository.save(new Task("Купить молоко",null, Status.NEW, Priority.MEDIUM));
//        taskRepository.save(new Task("Купить хлеб",null, Status.IN_PROGRESS));
//        taskRepository.save(new Task("Помыть машину",null, Status.NEW));
//
//    }
@Test
@DisplayName("Должен фильтровать задачи по статусу NEW")
    void shouldFilterByStatus(){
        //Given
        Specification<Task> spec = TaskSpecifications.hasStatus(Status.NEW);

        //when
        List<Task> result = taskRepository.findAll(spec);

        //then
       assertThat(result).hasSize(2).
               allMatch(task -> task.getStatus() == Status.NEW).
               extracting(Task::getDescription).
               containsExactlyInAnyOrder("Купить молоко", "Помыть машину");

    }

    @Test
    @DisplayName("Должен находить задачи по ключевому слову игнорируя регистр")
    void shouldFilterByKeywordIgnoreCase() {
        // Given
        Specification<Task> spec = TaskSpecifications.hasKeyword("КУПИТЬ");

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting(Task::getDescription)
                .allSatisfy(desc -> assertThat(desc.toLowerCase()).contains("купить"));
    }

    @Test
    @DisplayName("Должен возвращать пустой список, если ничего не найдено")
    void shouldReturnEmptyListWhenNoMatches() {
        // Given
        Specification<Task> spec = Specification
                .where(TaskSpecifications.hasStatus(Status.IN_PROGRESS))
                .and(TaskSpecifications.hasKeyword("Машина"));

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result).isEmpty();
    }
}
