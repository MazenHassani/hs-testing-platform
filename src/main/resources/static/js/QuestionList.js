let userId;
let questions = [];

function goBack() {
    redirect("/");
}

async function getQuestions() {
    questions = [];
    const response = await fetch("/api/question/", {
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
    questions = await response.json();
}

function setUserSubject() {
    userId = keycloak.subject;
}

function setCreateListener() {
    document.getElementById("create-question").addEventListener("click", function () {
        redirect(`/questions/create`);
    });
}

async function fetchQuestions(type) {
    await getQuestions();
    let table_name = type + "-table";
    let table_elem = document.querySelector("#" + table_name + " tbody");
    table_elem.innerHTML = "";
    questions.forEach((question) => {
        let template_name = "question-item-template";
        if (type == "all") {
            template_name = "all-" + template_name;
        } else {
            if (
                (question.questionType == "SPHERE" && type == "lessons") ||
                (question.questionType == "LESSON" && type == "widgets")
            ) {
                return;
            }
        }
        let newQuestion = document.getElementById(template_name).content.cloneNode(true);

        newQuestion.querySelector(".question-title").innerText = question.title;
        if (type == "all") {
            newQuestion.querySelector(".question-type").innerText = question.questionType;
        }
        newQuestion.querySelector(".question-edit").addEventListener("click", function () {
            redirect(`/questions/${question.id}/update`);
        });
        newQuestion.querySelector(".question-delete").addEventListener("click", function () {
            deleteQuestion(question.id).then((res) => async function () {
                if (res.ok) {
                    alert("Question deleted!");
                    window.location.reload();
                } else {
                    redirectError(res);
                }
            });
        });
        table_elem.append(newQuestion);
    });
}

async function deleteQuestion(questionId) {
    const response = await fetch(`/api/question/${questionId}`, {
        method: "DELETE",
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
    if (!response.ok) {
        redirectError(response);
    }
    return response;
}

async function setTabListeners() {
    document.getElementById("all-tab").addEventListener("click", function () {
        fetchQuestions("all");
    });
    document.getElementById("widgets-tab").addEventListener("click", function () {
        fetchQuestions("widgets");
    });
    document.getElementById("lessons-tab").addEventListener("click", function () {
        fetchQuestions("lessons");
    });
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([
                setUserSubject(),
                setCreateListener(),
                setTabListeners()])
                .then(function () {
                    document.getElementById("all-tab").click();
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
