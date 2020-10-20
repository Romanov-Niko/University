package com.foxminded.university.controller;

import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.service.LessonTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/lessonstimes")
public class LessonTimeController {

    private final LessonTimeService lessonTimeService;

    public LessonTimeController(LessonTimeService lessonTimeService) {
        this.lessonTimeService = lessonTimeService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("lessonstimes", lessonTimeService.getAll());
        return "lessonstimes/lessonstimes";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(@ModelAttribute("lessontime") LessonTime lessonTimeModel) {
        return "lessonstimes/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<LessonTime> lessonTime = lessonTimeService.getById(id);
        if (lessonTime.isPresent()) {
            model.addAttribute("lessontime", lessonTime.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Lesson time is not present");
        }
        return "lessonstimes/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("lessontime") LessonTime lessonTime, RedirectAttributes redirectAttributes) {
        try {
            lessonTimeService.save(lessonTime);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/lessonstimes/new";
        }
        return "redirect:/lessonstimes";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        lessonTimeService.delete(id);
        return "redirect:/lessonstimes";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("lessontime") LessonTime lessonTime, RedirectAttributes redirectAttributes) {
        try {
            lessonTimeService.update(lessonTime);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/lessonstimes/edit/"+lessonTime.getId();
        }
        return "redirect:/lessonstimes";
    }

}