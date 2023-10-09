package io.gitub.mat3e.controller;

import io.gitub.mat3e.logic.ProjectService;
import io.gitub.mat3e.model.Project;
import io.gitub.mat3e.model.ProjectStep;
import io.gitub.mat3e.model.projection.ProjectWriteModel;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(
        @ModelAttribute("project") @Valid ProjectWriteModel current,
        BindingResult bindingResult,
        Model model
        ){
        if(bindingResult.hasErrors()){
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("project", getProjects());
        model.addAttribute("message", "Project added!");
        return "projects";
    }
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project")  ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MMM-dd'T'HH:mm") LocalDateTime deadline
    ){
        try{
            service.createGroup(deadline,id);
            model.addAttribute("message","Group added!");
        }catch (IllegalStateException | IllegalArgumentException e){
            model.addAttribute("message","Error while creating group!");
        }
        return "projects";
    }
    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }


}