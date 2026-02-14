package com.harborspace.spacecraft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//import javax.mail.MessagingException;
//import javax.mail.internet.AddressException;

@Controller
public class WebController{
//127.0.0.1/
    @GetMapping("/")
    public String courseList() {
        return "CourseList";
    }

//    @GetMapping("/mail")
//    public String sendEmail() throws AddressException, MessagingException {
//        return "mail";
//    }

    @GetMapping(value = "/404")
    public String notFound(){
        return "error";
    }

    @GetMapping("/oops")
    public String somethingWentWrong(){
        return "oops";
    }

    @GetMapping("/403")
    public String forbidden(){
        return "403";
    }

    @GetMapping("/course/{courseId}")
    public String course(@PathVariable String courseId, Model model){
        model.addAttribute("courseId", courseId);
        return "Course";
    }

    @GetMapping("/course/create")
    public String createCourse() {
        return "UpdateCourse";
    }

    @GetMapping("/course/{courseId}/update")
    public String updateCourse(@PathVariable String courseId, Model model){
        model.addAttribute("courseId", courseId);
        return "UpdateCourse";
    }

    @GetMapping("/course/{courseId}/test/create")
    public String createTest(@PathVariable String courseId, Model model){
        model.addAttribute("courseId", courseId);
        return "UpdateTest";
    }

    @GetMapping("/course/{courseId}/test/{testId}/update")
    public String updateTest(@PathVariable String courseId, @PathVariable String testId, Model model){
        model.addAttribute("courseId", courseId);
        model.addAttribute("testId", testId);
        return "UpdateTest";
    }

    @GetMapping("/course/{courseId}/test/{testId}/questions")
    public String manageTestQuestions(@PathVariable String courseId, @PathVariable String testId, Model model){
        model.addAttribute("courseId", courseId);
        model.addAttribute("testId", testId);
        return "ManageQuestions";
    }

    @GetMapping(value ={"/course/{courseId}/test/{testId}/tasks", "/course/{courseId}/test/{testId}/tasks/{index}"})
    public String takeTest(@PathVariable String courseId, @PathVariable String testId,
                           @PathVariable(required = false) String index,
                           Model model) {
        model.addAttribute("courseId", courseId);
        if (index == null) index = "0";
        model.addAttribute("testId", testId);
        model.addAttribute("index", index);
        return "StudentTest";
    }

    @GetMapping("/course/{courseId}/test/{testId}/{taskId}/submissions")
    public String showSubmissions(@PathVariable String courseId, @PathVariable String testId, @PathVariable String taskId, Model model){
        model.addAttribute("courseId", courseId);
        model.addAttribute("testId", testId);
        model.addAttribute("taskId", taskId);
        return "ShowSubmissions";
    }

    @GetMapping("/course/{courseId}/invite")
    public String createInvite(@PathVariable String courseId, Model model){
        model.addAttribute("courseId", courseId);
        return "CreateInvite";
    }

    @GetMapping("/invite/{code}")
    public String acceptInvite(@PathVariable String code, Model model){
        model.addAttribute("code", code);
        return "Invite";
    }

    @GetMapping("/questions")
    public String myQuestions() {
        return "QuestionList";
    }

    @GetMapping("/questions/create")
    public String createQuestion() {
        return "UpdateQuestion";
    }

    @GetMapping("/questions/{questionId}/update")
    public String updateQuestion(@PathVariable String questionId, Model model){
        model.addAttribute("questionId", questionId);
        return "UpdateQuestion";
    }

//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        log.error(status.toString() + "\t" + request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString());
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "404";
//            }
//            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
//                return "403";
//            }
//        }
//        return (String) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//    }

    @GetMapping("/course/{courseId}/test/{testId}/scoreboard")
    public String scoreboard(@PathVariable String courseId, @PathVariable String testId, Model model){
        model.addAttribute("testId", testId);
        model.addAttribute("courseId", courseId);
        return "Scoreboard";
    }
}
