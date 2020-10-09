package com.foxminded.university.controller;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("subjects", subjectService.getAll());
        return "subjects/subjects";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjects/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("subject") Subject subject, RedirectAttributes redirectAttributes) {
        try {
            subjectService.save(subject);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/subjects/new";
        }
        return "redirect:/subjects";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Subject> subject = subjectService.getById(id);
        model.addAttribute("subject", subject);
        return "subjects/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("subject") Subject subject, RedirectAttributes redirectAttributes) {
        try {
            subjectService.update(subject);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/subjects/edit/"+subject.getId();
        }
        return "redirect:/subjects";
    }
}
