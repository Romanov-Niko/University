package com.foxminded.university.controller;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.service.DayScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public String viewLessonsForDayForStudent(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id")int id, Model model) {
        Optional<DaySchedule> daySchedule = dayScheduleService.getByDateForStudent(id, date);
        daySchedule.ifPresent(schedule -> model.addAttribute("lessons", schedule.getLessons()));
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/student", params = "action=month")
    public String viewLessonsForMonthForStudent(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id")int id, Model model) {
        List<DaySchedule> daysSchedules = dayScheduleService.getByMonthForStudent(id, date);
        List<Lesson> lessons = new ArrayList<>();
        daysSchedules.forEach(daySchedule -> lessons.addAll(daySchedule.getLessons()));
        model.addAttribute("lessons", lessons);
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/teacher", params = "action=day")
    public String viewLessonsForDayForTeacher(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id")int id, Model model) {
        Optional<DaySchedule> daySchedule = dayScheduleService.getByDateForTeacher(id, date);
        daySchedule.ifPresent(schedule -> model.addAttribute("lessons", schedule.getLessons()));
        return "daysschedules/daysschedules";
    }

    @PostMapping(value = "/teacher", params = "action=month")
    public String viewLessonsForMonthForTeacher(@RequestParam(value = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam(value = "id")int id, Model model) {
        List<DaySchedule> daysSchedules = dayScheduleService.getByMonthForTeacher(id, date);
        List<Lesson> lessons = new ArrayList<>();
        daysSchedules.forEach(daySchedule -> lessons.addAll(daySchedule.getLessons()));
        model.addAttribute("lessons", lessons);
        return "daysschedules/daysschedules";
    }
}
