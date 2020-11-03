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
        model.addAttribute("groups", groupService.findAll());
        return "groups/groups";
    }

    @GetMapping("/new")
    public String redirectToSaveForm(Model model, Group groupModel) {
        model.addAttribute("allStudents", studentService.findAll());
        return "groups/new";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        Optional<Group> group = groupService.findById(id);
        if (group.isPresent()) {
            model.addAttribute("group", group.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Group is not present");
        }
        model.addAttribute("allStudents", studentService.findAll());
        return "groups/edit";
    }

    @GetMapping("students/{groupId}")
    public String showSubjects(@PathVariable int groupId, Model model) {
        Optional<Group> group = groupService.findById(groupId);
        group.ifPresent(currentGroup -> model.addAttribute("students", currentGroup.getStudents()));
        return "groups/students";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("group") Group group, @RequestParam(value = "studentsOfGroup" , required = false) int[] studentsOfGroup, RedirectAttributes redirectAttributes) {
        List<Student> students = new ArrayList<>();
        if(studentsOfGroup != null) {
            for (int id: studentsOfGroup){
                Optional<Student> student = studentService.findById(id);
                student.ifPresent(students::add);
            }
            group.setStudents(students);
        }
        try {
            groupService.save(group);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/groups/new";
        }
        return "redirect:/groups";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("group") Group group, @RequestParam(value = "studentsOfGroup" , required = false) int[] studentsOfGroup, RedirectAttributes redirectAttributes) {
        List<Student> students = new ArrayList<>();
        if(studentsOfGroup != null) {
            for (int id: studentsOfGroup){
                Optional<Student> student = studentService.findById(id);
                student.ifPresent(students::add);
            }
            group.setStudents(students);
        }
        try {
            groupService.update(group);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/groups/edit/"+group.getId();
        }
        return "redirect:/groups";
    }

}
