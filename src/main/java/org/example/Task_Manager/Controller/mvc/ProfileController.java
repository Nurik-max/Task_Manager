package org.example.Task_Manager.Controller.mvc;

import jakarta.validation.Valid;
import org.example.Task_Manager.DTO.workers.ChangePasswordDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
import org.example.Task_Manager.Sevice.TaskStatisticsService;
import org.example.Task_Manager.Sevice.WorkerService;
import org.example.Task_Manager.details.WorkerDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

private final WorkerService workerService;
private final TaskStatisticsService taskStatisticsService;
/*TODO LIST: make templates for them
   - make profileDTO for profile
   - make updateProfile for profile*/

public  ProfileController(WorkerService workerService, TaskStatisticsService taskStatisticsService) {
    this.workerService = workerService;
    this.taskStatisticsService = taskStatisticsService;
}


    @GetMapping("/profile")
    public String profile(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            Model model) {

        model.addAttribute(
                "worker",
                workerService.showWorker(workerDetails.getWorker().getId())
        );
        model.addAttribute(
                "statistics", taskStatisticsService.getStatistics(workerDetails.getWorker().getId())
        );

        return "profile/profile";
    }

    @GetMapping("/edit")
    public String editProfile(@AuthenticationPrincipal WorkerDetails workerDetails, Model model) {
    model.addAttribute("worker", workerService.showProfile(workerDetails.getWorker().getId()));
    return "profile/editProfile";
    }

    @PostMapping
    public String update(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            @ModelAttribute @Valid ProfileUpdateDTO dto,
            BindingResult result) {

        if(result.hasErrors()){
            return "profile/profile";
        }

        workerService.updateProfile(workerDetails.getWorker().getId(), dto);

        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String getChangePasswordPage(Model model) {

        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());

        return "profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal WorkerDetails workerDetails,
            @ModelAttribute("changePasswordDTO") @Valid ChangePasswordDTO dto,
            BindingResult result, RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            return "profile/change-password";
        }

        try {
            workerService.changePassword(workerDetails.getWorker().getId(), dto);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Password changed successfully"
            );

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            return "profile/change-password";
        }

        return "redirect:/profile";
    }

}
