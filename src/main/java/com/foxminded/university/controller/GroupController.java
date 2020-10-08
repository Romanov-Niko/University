package com.foxminded.university.controller;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.service.GroupService;
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
    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public GroupController(GroupService groupService, GroupDao groupDao, StudentDao studentDao) {
        this.groupService = groupService;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "groups/index";
    }

    @GetMapping("/new")
    public String newAudience(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("allStudents", studentDao.getAll());
        return "groups/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("group") Group group, @RequestParam(value = "stud" , required = false) int[] stud, RedirectAttributes redirectAttributes) {
        List<Student> students = new ArrayList<>();
        if(stud != null) {
            for (int id: stud){
                Student student = studentDao.getById(id).get();
                students.add(student);
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
    public String delete(@PathVariable("id") int id) {
        groupService.delete(id);
        return "redirect:/groups";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Group> group = groupDao.getById(id);
        model.addAttribute("group", group);
        model.addAttribute("allStudents", studentDao.getAll());
        return "groups/edit";
    }

    @PostMapping("update/{id}")
    public String update(@ModelAttribute("group") Group group, @RequestParam(value = "stud" , required = false) int[] stud, RedirectAttributes redirectAttributes) {
        List<Student> students = new ArrayList<>();
        if(stud != null) {
            for (int id: stud){
                Student student = studentDao.getById(id).get();
                students.add(student);
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

    @GetMapping("students/{id}")
    public String showSubjects(@PathVariable("id") int id, Model model) {
        Optional<Group> group = groupDao.getById(id);
        model.addAttribute("students", group.get().getStudents());
        return "groups/students";
    }
}
