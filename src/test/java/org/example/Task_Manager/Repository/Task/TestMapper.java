package org.example.Task_Manager.Repository.Task;

import org.example.Task_Manager.DTO.tasks.TaskDTO;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestMapper {
    @Autowired
    private TaskMapper taskMapper;

    @Test
    void testToDTO() {
        // 1. ПОДГОТОВКА (Arrange): Создаем объект, который мы хотим «перевести»
        Task task = new Task();
        task.setDescription("Test task"); // Ставим какое-то значение

//         2. ДЕЙСТВИЕ (Act): Вызываем метод маппера
        AdminResponse dto = taskMapper.toAdminResponse(task);

//         3. ПРОВЕРКА (Assert): Сравниваем результат с ожиданием
        assertEquals("Test task", dto.getDescription());
}
}
