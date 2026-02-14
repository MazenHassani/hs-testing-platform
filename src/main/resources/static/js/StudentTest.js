let test_questions = {};
let test = {};
let username = "";
let subscribed = false;

function goBack() {
    redirect(`/course/${courseId}`);
}

function goStart() {
    window.history.pushState({}, "", `/course/${courseId}/test/${testId}/tasks/0`);
    onIndexChange();
}

function setScoreboardURL() {
    const scoreboardBtn = document.getElementById("scoreboard");
    scoreboardBtn.href = `/course/${courseId}/test/${testId}/scoreboard`;
}

async function getUsername() {
    const profile = await keycloak.loadUserProfile();
    username = profile.username || keycloak.subject || "";
}

async function getTest() {
    if (!courseId || !testId) {
        goBack();
        return;
    }
    let url = `/api/course/${courseId}/test/${testId}`;
    test = {};
    const testResponse = await fetch(url, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            Authorization: "Bearer " + (keycloak.token || loginToken),
            Accept: "application/json",
        },
        redirect: "follow",
    });
    if (testResponse.ok) {
        test = await testResponse.json();
        document.getElementById("test-title").textContent = test.name;
        return test;
    } else {
        redirectError(testResponse);
    }
}

function showTest(test) {
    if (!test)
        return;
    document.title = `HS Stars | ${test.name}`
    document.getElementById('test-start-time').textContent = test.startDate;
    document.getElementById('test-end-time').textContent = test.endDate;
    let timer = setInterval(function () {
        let now = new Date().getTime();
        let distance = new Date(test.endDate.replace(/ /g, "T")) - now;
        let days = Math.floor(distance / (1000 * 60 * 60 * 24));
        let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        let seconds = Math.floor((distance % (1000 * 60)) / 1000);
        if (days > 0) {
            document.getElementById('test-current-time').textContent = `${days}d ${hours}h ${minutes}m ${seconds}s`;
        } else {
            document.getElementById('test-current-time').textContent = `${hours}h ${minutes}m ${seconds}s`;
        }
    }, 1000);
}

async function getTestQuestions() {
    let url = `/api/test-question/course/${courseId}/test/${testId}/tasks`;
    const testQuestionResponse = await fetch(url, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            Authorization: "Bearer " + (keycloak.token || loginToken),
            Accept: "application/json",
        },
        redirect: "follow",
    });
    if (testQuestionResponse.ok) {
        test_questions = await testQuestionResponse.json();
        return test_questions;
    } else {
        redirectError(testQuestionResponse);
    }
}

function showQuestions() {
    if (!test_questions || !test_questions.questionsDtos || !test_questions.questionsDtos.length)
        return;
    const questions = test_questions.questionsDtos;
    let questionList = document.getElementById("questions-list");
    for (let i = 0; i < questions.length; i++) {
        let template_name = "questions-list-item";
        let question = questions[i];
        let newQuestion = document.getElementById(template_name).content.cloneNode(true);
        newQuestion.querySelector(".question-title").innerText = question.title;
        newQuestion.querySelector(".question-title").href = `/course/${courseId}/test/${testId}/tasks/${i}`;
        newQuestion.querySelector(".question-title").addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            window.history.pushState({}, "", this.href);
            onIndexChange();
            return false;
        })
        questionList.append(newQuestion);
    }
}

async function getQuestion() {
    if (!test_questions || !test_questions.questionsDtos || !test_questions.questionsDtos.length)
        return;
    const questions = test_questions.questionsDtos;
    if (index < 0 || index >= questions.length) {
        goStart();
        return null;
    }
    const navItems = document.querySelectorAll("#questions-list .nav-link");
    navItems.forEach(node => node.classList.remove("active"));
    if (navItems.length > index) {
        navItems[index].classList.add("active");
    }
    const nextBtn = document.getElementById("next");
    const prevBtn = document.getElementById("previous");
    nextBtn.href = `${index * 1 + 1}`;
    prevBtn.href = `${index * 1 - 1}`;
    return questions[index];
}

function trimAttributes(node) {
    $.each(node.attributes, function () {
        let attrName = this.name;
        let attrValue = this.value;
        // remove attribute name start with "on", possible unsafe,
        // for example: onload, onerror...
        //
        // remove attribute value start with "javascript:" pseudo protocol, possible unsafe,
        // for example href="javascript:alert(1)"
        // credits: https://gist.github.com/ufologist/5a0da51b2b9ef1b861c30254172ac3c9
        if ((attrName && attrName.indexOf('on') == 0) || (attrValue && attrValue.indexOf('javascript') == 0)) {
            $(node).removeAttr(attrName);
        }
    });
}

function sanitize(html) {
    // [jQuery.parseHTML(data [, context ] [, keepScripts ])](http://api.jquery.com/jQuery.parseHTML/) added: 1.8
    // Parses a string into an array of DOM nodes.
    //
    // null context and do not keep scripts
    // credits: https://gist.github.com/ufologist/5a0da51b2b9ef1b861c30254172ac3c9
    let output = $($.parseHTML('<div>' + html + '</div>', null, false));
    output.find('*').each(function () {
        trimAttributes(this);
    });
    return output.html();
}

function showLesson(question) {
    if (!question)
        return;
    let statement = question.statement.replace(/(?:\r\n|\r|\n)/g, '<br/>') || '';
    let lesson = sanitize(statement);
    let lessonContainer = document.getElementById('lesson-container');
    lessonContainer.innerHTML = lesson;
    lessonContainer.hidden = false;
    MathJax.typeset();
}

function getWidget() {
    (function (d, s, id) {
        SE_BASE = "767e7217.widgets.sphere-engine.com";
        SE_HTTPS = true;
        SE = window.SE || (window.SE = []);
        var js,
            fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.async = false;
        js.src = (SE_HTTPS ? "https" : "http") + "://" + SE_BASE + "/static/sdk/sdk.min.js";
        fjs.parentNode.insertBefore(js, fjs);
    })(document, "script", "sphere-engine-jssdk");
}

function removeWidget() {
    let d = document.getElementById('sphere-engine-jssdk');
    if (d) d.remove();
    SE = window.SE = {};
}

async function sendSubmission(RawSubmissionRequest) {
    const response = await fetch(`/api/scoreboard/add`, {
        method: 'POST',
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        body: JSON.stringify(RawSubmissionRequest),
        redirect: "follow",
    });
    if (!response.ok) {
        alert(`Failed to submit code, Status code: ${response.status}`);
    }
    return false;
}

function setSubmitListener() {
    SE.ready = function (f) {
        let script = document.querySelector("#sphere-engine-jssdk");
        script.addEventListener("load", f);
    };
    SE.ready(function () {
        let SEWidget = SE.widget("question-widget");
        if (subscribed) {
            return;
        }
        let afterSendSubmission = function (data) {
            console.log(data);
            let testQuestionId = test_questions.testQuestionIds[index];
            let rawSubmission = {
                submissionApiId: data['apiSubmissionId'],
                receivedUserId: data.user.id,
                receivedName: data.user.name,
                receivedEmail: data.user.email,
                userId: username,
                testQuestionId: testQuestionId
            };
            sendSubmission(rawSubmission);
        };
        SEWidget.events.subscribe("afterSendSubmission", afterSendSubmission);
        subscribed = true;
    });
}

function showWidget(question) {
    let widgetContainer = document.getElementById('widget-container');
    widgetContainer.innerHTML = "";
    removeWidget();
    let widgetTemplate = document.getElementById('widget-template').content.cloneNode(true);
    let widget = widgetTemplate.querySelector('.se-widget');
    let testQuestionId = test_questions.testQuestionIds[index];
    widget.id = widget.dataset.id = "question-widget";
    widget.dataset.userId = username || keycloak.subject;
    widget.dataset.widget = question.widgetCode;
    widget.dataset.customData = `${testQuestionId}`;
    getWidget();
    setSubmitListener();
    widgetContainer.append(widgetTemplate);
    widgetContainer.hidden = false;
}

function displayQuestion(question) {
    document.getElementById("questionStatement").innerText = question.statement;
    if (question.questionType.startsWith("MCQ")) {
        if (!question.answer || !question.answer.choices)
            redirect("/oops");
        else {
            let choicesSection;
            if (question.questionType == "MCQ_ORDER") {
                console.log(question.questionType);
                console.log(question.id);
                Sortable.create(document.getElementById("choices"), {
                    animation: 150
                });
            }
            choicesSection = document.querySelector("#choices");

            const choices = question.answer.choices;
            choices.forEach((choice) => {
                let newChoice = document.querySelector(".choice-template").content.cloneNode(true);
                newChoice.querySelector(".form-check-label").textContent = sanitize(choice.statement);
                newChoice.id = choice.id;
                newChoice.querySelector(".form-check-input").value = sanitize(choice.statement);
                choicesSection.append(newChoice);
                MathJax.typeset();
            });

        }
    }
    document.getElementById('question-container').hidden = false;
}

async function showQuestion(question) {
    if (!question)
        return;
    document.getElementById('question-title').textContent = question.title;
    document.getElementById('widget-container').hidden = true;
    document.getElementById('lesson-container').hidden = true;
    document.getElementById('question-container').hidden = true;
    if (question.questionType === "LESSON") showLesson(question);
    else if (question.questionType === "SPHERE") showWidget(question);
    else {
        $("#question-container").load((`/studentTestQuestions/${question.questionType}.html`), () => displayQuestion(question));

    }
}

function fetchData() {
    getTest().then(showTest);
    getTestQuestions().then((testQuestions) => {
        showQuestions(testQuestions);
        getQuestion().then((question) => {
            showQuestion(question);
        });
    });
}

function onIndexChange() {
    let location = window.location.href || "";
    if (location.length && location[location.length - 1] === "#") {
        location = location.slice(0, -1)
    }
    let parts = location.split('/');
    if (parts.length === 0) parts = ["0"];
    index = parts[parts.length - 1] || 0;
    getQuestion().then((question) => {
        showQuestion(question);
    }).catch(function (e) {
        console.log(e);
    });
}

window.onpopstate = function () {
    onIndexChange();
};

function setNavigationButtons() {
    const nextBtn = document.getElementById("next");
    const prevBtn = document.getElementById("previous");
    const clickEvent = function (e) {
        e.preventDefault();
        e.stopPropagation();
        window.history.pushState({}, "", this.href);
        onIndexChange();
        return false;
    };
    nextBtn.addEventListener("click", clickEvent);
    prevBtn.addEventListener("click", clickEvent);
}

function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([
                getUsername(),
                setScoreboardURL(),
                fetchData()])
                .then(function () {
                    loadNavBar();

                    document.getElementById("test-tab").click();
                    displayMain();

                    document.querySelector(".nav").hidden = false;
                });
        }
    }).catch(function (e) {
        console.log(e);
        alert("failed to initialize");
    });
    setNavigationButtons();
}

function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}

function mazenSubmission(e) {
    console.log(e);
    e.preventDefault();
    const rbs = document.querySelectorAll('input[name="radio-choice"]');
    let selectedValue;
    for (const rb of rbs) {
        if (rb.checked) {
            selectedValue = rb.value;
            break;
        }
    }
    alert(selectedValue);
    axios.post('/api/submission/', {
            value : selectedValue,
            taskId : test_questions.testQuestionIds[index]
        })
      .then(function (response) {
        console.log(response);
        alert(response.data)
      })
      .catch(function (error) {
        console.log(error);
      });
}

docReady(init);
