package com.foxminded.university.controller;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
        model.addAttribute("subjects", subjectService.findAll());
        return "subjects/subjects";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Subject subjectModel) {
        return "subjects/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Subject> subject = subjectService.findById(id);
        if (subject.isPresent()) {
            model.addAttribute("subject", subject.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Subject is not present");
        }
        return "subjects/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("subject") @Valid Subject subject, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/subjects/new";
        }
        try {
            subjectService.save(subject);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/subjects/new";
        }
        return "redirect:/subjects";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        subjectService.delete(id);
        return "redirect:/subjects";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("subject") @Valid Subject subject, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/subjects/edit";
        }
        try {
            subjectService.update(subject);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/subjects/edit/"+subject.getId();
        }
        return "redirect:/subjects";
    }

}
