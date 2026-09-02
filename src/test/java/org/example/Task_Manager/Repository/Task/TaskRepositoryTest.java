package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.example.Task_Manager.specification.TaskSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Stream;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;


    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        // Заполняем тестовую БД начальными данными
        taskRepository.save(new Task("Купить молоко", null, null, Status.NEW, Priority.MEDIUM));
        taskRepository.save(new Task("Купить хлеб", null, null, Status.IN_PROGRESS, Priority.HIGH));
        taskRepository.save(new Task("Помыть машину", null, null, Status.NEW, Priority.LOW));
    }

    static Stream<Arguments> filterProvider() {
        return Stream.of(
                Arguments.of(Status.NEW, "Купить", 1),         // "Купить молоко"
                Arguments.of(Status.NEW, "машину", 1),        // "Помыть машину"
                Arguments.of(Status.IN_PROGRESS, "хлеб", 1),   // "Купить хлеб"
                Arguments.of(Status.IN_PROGRESS, "молоко", 0)  // Ничего не найдено
        );
    }

    @ParameterizedTest
    @MethodSource("filterProvider")
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

    @ParameterizedTest
    @MethodSource("filterProvider")
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
    @ParameterizedTest
    @MethodSource("filterProvider")
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
