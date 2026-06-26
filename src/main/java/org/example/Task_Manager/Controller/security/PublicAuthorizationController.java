package org.example.Task_Manager.Controller.security;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.WorkerDTO;
import org.example.Task_Manager.Sevice.WorkerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PublicAuthorizationController {

    private final WorkerService workerService;

    public PublicAuthorizationController( WorkerService workerService) {
        this.workerService = workerService;

    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "authorization/login";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model){
        model.addAttribute("worker", new WorkerDTO());
        return "authorization/register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("worker") @Valid CreateWorkerDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "authorization/register";
        }

       workerService.register(dto);

        return "redirect:/login";
    }
}
