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
    public String redirectToSaveForm(Model model) {
        model.addAttribute("lessontime", new LessonTime());
        return "lessonstimes/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<LessonTime> lessonTime = lessonTimeService.getById(id);
        model.addAttribute("lessontime", lessonTime);
        return "lessonstimes/edit";
    }
}
