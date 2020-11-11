package com.foxminded.university.controller;

import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.service.LessonTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
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
        model.addAttribute("lessonstimes", lessonTimeService.findAll());
        return "lessonstimes/lessonstimes";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(@ModelAttribute("lessontime") LessonTime lessonTimeModel) {
        return "lessonstimes/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<LessonTime> lessonTime = lessonTimeService.findById(id);
        if (lessonTime.isPresent()) {
            model.addAttribute("lessontime", lessonTime.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Lesson time is not present");
        }
        return "lessonstimes/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("lessontime") @Valid LessonTime lessonTime, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/lessonstimes/new";
        }
        lessonTimeService.save(lessonTime);
        return "redirect:/lessonstimes";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        lessonTimeService.delete(id);
        return "redirect:/lessonstimes";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("lessontime") @Valid LessonTime lessonTime, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/lessonstimes/edit";
        }
        lessonTimeService.update(lessonTime);
        return "redirect:/lessonstimes";
    }

}
