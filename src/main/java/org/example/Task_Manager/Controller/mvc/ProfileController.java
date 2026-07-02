package org.example.Task_Manager.Controller.mvc;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

private WorkerService workerService;


public  ProfileController(WorkerService workerService) {
    this.workerService = workerService;
}

    @GetMapping
    public String profile(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            Model model) {

        model.addAttribute(
                "worker",
                workerService.showWorker(workerDetails.getWorker().getId())
        );

        return "profile/profile";
    }


    @PostMapping
    public String update(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            @ModelAttribute @Valid UpdateWorkerDTO dto,
            BindingResult result) {

        if(result.hasErrors()){
            return "profile/profile";
        }

        workerService.updateWorker(
                workerDetails.getWorker().getId(),
                dto
        );

        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String getChangePasswordPage(Model model) {

        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());

        return "workers/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            @ModelAttribute("changePasswordDTO") @Valid ChangePasswordDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "workers/change-password";
        }

        try {
            workerService.changePassword(dto, workerDetails);

            model.addAttribute("success", "Password changed successfully");

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            return "workers/change-password";
        }

        return "redirect:/workers";
    }

}
