package com.foxminded.university.controller;

import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.service.DayScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/daysschedules")
public class DayScheduleController {

    private final DayScheduleService dayScheduleService;

    public DayScheduleController(DayScheduleService dayScheduleService) {
        this.dayScheduleService = dayScheduleService;
    }

    @GetMapping("/search/teacher/{id}")
    public String searchScheduleForTeacher(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("person", "teacher");
        return "daysschedules/search";
    }

    @GetMapping("/search/student/{id}")
    public String searchScheduleForStudent(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("person", "student");
        return "daysschedules/search";
    }

    @PostMapping(value = "/student", params = "action=day")
    public String viewDailyStudentSchedule(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id") int studentId, Model model) {
        DaySchedule daySchedule = dayScheduleService.findByDateForStudent(studentId, date);
        model.addAttribute("lessons", daySchedule.getLessons());
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/student", params = "action=month")
    public String viewMonthlyStudentSchedule(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id") int studentId, Model model) {
        List<DaySchedule> daysSchedules = dayScheduleService.findByMonthForStudent(studentId, date);
        List<Lesson> lessons = new ArrayList<>();
        daysSchedules.forEach(daySchedule -> lessons.addAll(daySchedule.getLessons()));
        model.addAttribute("lessons", lessons);
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/teacher", params = "action=day")
    public String viewDailyTeacherSchedule(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id") int teacherId, Model model) {
        DaySchedule daySchedule = dayScheduleService.findByDateForTeacher(teacherId, date);
        model.addAttribute("lessons", daySchedule.getLessons());
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/teacher", params = "action=month")
    public String viewMonthlyTeacherSchedule(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id") int teacherId, Model model) {
        List<DaySchedule> daysSchedules = dayScheduleService.findByMonthForTeacher(teacherId, date);
        List<Lesson> lessons = new ArrayList<>();
        daysSchedules.forEach(daySchedule -> lessons.addAll(daySchedule.getLessons()));
        model.addAttribute("lessons", lessons);
        return "daysschedules/daysschedules";
    }
}
