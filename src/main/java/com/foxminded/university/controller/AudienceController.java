package com.foxminded.university.controller;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.service.AudienceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/audiences")
public class AudienceController {

    private final AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("audiences", audienceService.getAll());
        return "audiences/audiences";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("audience", new Audience());
        return "audiences/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Audience> audience = audienceService.getById(id);
        model.addAttribute("audience", audience);
        return "audiences/edit";
    }
}
