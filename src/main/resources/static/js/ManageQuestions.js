const Sortable = window.Sortable
let test;
let myQuestions;
let myQuestions_dict;
let valid = false;
let test_questions;
let test_questions_ids = new Set();

function goBack() {
    redirect(`/course/${courseId}/test/${testId}/update`);
}

function showTestQuestions(test_questions) {
    const questions = test_questions.questionsDtos;
    const testQuestionIds = test_questions.testQuestionIds;

    if (!questions) {
        return;
    }
    let questions_sz = questions.length;
    const questionsWeights = test_questions.questionsWeights;
    let questionList = document.getElementById("questions-list");
    questionList.innerHTML = "";
    test_questions_ids = new Set();
    for (let i = 0; i < questions_sz; i++) {
        let question = questions[i];
        test_questions_ids.add(question.id);
        let newQuestion = getNewQuestion(question);
        newQuestion.querySelector(".question-weight").value = questionsWeights[i];
        newQuestion.querySelector(".list-group-item").dataset.testQuestionId = testQuestionIds[i];
        questionList.append(newQuestion);
    }
}

async function getQuestions() {
    myQuestions = [];
    const response = await fetch("/api/question/", {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow",
    });
    myQuestions = await response.json();
}

async function showMyQuestions() {
    let myQuestionsForm = document.getElementById("questions-checklist");

    myQuestions_dict = {};
    myQuestions.forEach((question) => {
        myQuestions_dict[question.id] = question;
        let template_name = "questions-checklist-item";
        let newQuestion = document.getElementById(template_name).content.cloneNode(true);
        newQuestion.querySelector(".form-check-label").textContent = question.title;
        newQuestion.querySelector(".form-check").setAttribute("id", question.id);
        myQuestionsForm.prepend(newQuestion);
    });
}

async function submitChanges() {
    let url = `/api/test-question/course/${courseId}/test/${testId}`;
    let questionElems = document.getElementById("questions-list").getElementsByClassName("list-group-item");
    let testQuestionDtos = [];
    for (let i = 0; i < questionElems.length; i++) {
        let curQuestion = questionElems[i];
        let testQuestion = {
            id: parseInt(curQuestion.dataset.testQuestionId) || null,
            questionId:curQuestion.value,
            testId:parseInt(testId),
            weight:parseInt(curQuestion.querySelector(".question-weight").value)};
        if(isNaN(testQuestion.id)) {
            delete testQuestion.id;
        }
        testQuestionDtos.push(testQuestion);
    }
    const request = {"testQuestionDtos": testQuestionDtos, "testId": parseInt(testId)};
    const response = await fetch(url, {
        method: "POST",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },

        body: JSON.stringify(request),
        redirect: "follow",
    });
    if (response.ok) {
        showSuccess("The test has been updated");
        test_questions = await response.json();
        showTestQuestions(test_questions);
    } else {
        redirectError(response);
    }
}

function getNewQuestion(question) {
    const template_name = "test-step-template";
    let newQuestion = document.getElementById(template_name).content.cloneNode(true);
    newQuestion.querySelector(".list-group-item").value = question.id;
    newQuestion.querySelector(".question-title").innerText = question.title;
    newQuestion.querySelector(".question-type").innerText = question.questionType;
    if (question.questionType === "LESSON") {
        newQuestion.querySelector(".question-weight").value = "0";
        newQuestion.querySelector(".question-weight").hidden = true;
        newQuestion.querySelector(".question-weight-title").hidden = true;
        newQuestion.querySelector(".question-weight").disabled = true;
    } else {
        newQuestion.querySelector(".question-weight").value = "1";
    }
    newQuestion.querySelector(".question-link").addEventListener("click", function () {
        redirect(`/questions/${question.id}/update`);
    });
    newQuestion.querySelector(".question-remove").addEventListener("click", function () {
        test_questions_ids.delete(question.id);
        this.parentNode.remove();
    });
    return newQuestion;
}

function addQuestions() {
    const elems = document.getElementById("questions-checklist").getElementsByClassName("form-check");
    let questionList = document.getElementById("questions-list");

    for (let i = 0; i < elems.length; i++) {
        let currentId = elems[i].getAttribute("id");
        let checked = elems[i].querySelector(".form-check-input").checked;

        let question = myQuestions_dict[currentId];
        if (!checked || test_questions_ids.has(question.id)) {
            continue;
        }
        test_questions_ids.add(question.id);

        questionList.append(getNewQuestion(question));
    }
}

async function showAddQuestions() {
    await getQuestions();
    await showMyQuestions();
    document.getElementById("add-questions-button").addEventListener("click", addQuestions);
}

async function showPage() {
    if (!valid) {
        goBack();
        return;
    }
    document.getElementById("test-name").innerText = test.name;
    document.getElementById("save-test").addEventListener("click", submitChanges);
    await showAddQuestions();

    let url = `/api/test-question/course/${courseId}/test/${testId}`;
    const testQuestionResponse = await fetch(url, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow",
    });
    test_questions = await testQuestionResponse.json();
    showTestQuestions(test_questions);
    // const questions = test_questions.questionsDtos;
    // const testQuestionIds = test_questions.testQuestionIds;
    //
    // if (!questions) {
    //     return;
    // }
    // let questions_sz = questions.length;
    // const questionsWeights = test_questions.questionsWeights;
    // let questionList = document.getElementById("questions-list");
    // test_questions_ids = new Set();
    // for (let i = 0; i < questions_sz; i++) {
    //     let question = questions[i];
    //     test_questions_ids.add(question.id);
    //     let newQuestion = getNewQuestion(question);
    //     newQuestion.querySelector(".question-weight").value = questionsWeights[i];
    //     newQuestion.querySelector(".list-group-item").dataset.testQuestionId = testQuestionIds[i];
    //     questionList.append(newQuestion);
    // }
}

async function getTest() {
    if (!courseId || !testId) {
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
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow",
    });
    if (testResponse.ok) {
        test = await testResponse.json();
        valid = true;
    }
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            getTest().then(() => {
                Sortable.create(document.getElementById("questions-list"), {
                    animation: 150
                });
                showPage();
                loadNavBar();
                displayMain();
            });
        }
    }).catch(function (e) {
        console.log(e);
        redirect("/oops");
    });
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

docReady(init);
