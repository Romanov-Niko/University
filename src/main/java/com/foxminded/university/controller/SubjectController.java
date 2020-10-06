package com.foxminded.university.controller;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.service.AudienceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class SubjectController {

    private final AudienceService audienceService;

    public SubjectController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("audiences", audienceService.getAll());
        return "audiences/index";
    }

    @GetMapping("/new")
    public String newAudience(Model model) {
        model.addAttribute("audience", new Audience());
        return "audiences/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("audience") Audience audience, RedirectAttributes redirectAttributes) {
        try {
            audienceService.save(audience);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/audiences/new";
        }
        return "redirect:/audiences";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        audienceService.delete(id);
        return "redirect:/audiences";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Audience> audience = audienceDao.getById(id);
        model.addAttribute("audience", audience);
        return "audiences/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("audience") Audience audience, RedirectAttributes redirectAttributes) {
        try {
            audienceService.update(audience);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/audiences/edit/"+audience.getId();
        }
        return "redirect:/audiences";
    }
}
