package com.foxminded.university.controller;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;

    public GroupController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "groups/groups";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("allStudents", studentService.getAll());
        return "groups/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Group> group = groupService.getById(id);
        if (group.isPresent()) {
            model.addAttribute("group", group.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Group is not present");
        }
        model.addAttribute("allStudents", studentService.getAll());
        return "groups/edit";
    }

    @GetMapping("students/{groupId}")
    public String showSubjects(@PathVariable int groupId, Model model) {
        Optional<Group> group = groupService.getById(groupId);
        group.ifPresent(currentGroup -> model.addAttribute("students", currentGroup.getStudents()));
        return "groups/students";
    }
}
