let question = {};
let created = false;

const questionTypeToIdx = {
    LESSON: 0,
    SPHERE: 1,
};


function goBack() {
    redirect("/questions");
}

async function deleteQuestion(e) {
    e.preventDefault();
    e.stopPropagation();
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
    if (response.ok) {
        alert("Question deleted!");
        goBack();
    } else {
        redirectError(response);
    }
}

async function getQuestion() {
    if (!questionId) return;
    course = {};
    const response = await fetch(`/api/question/${questionId}/`, {
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
    if (response.ok) {
        created = true;
        question = await response.json();
    } else if (response.status == 404) {
        redirect("/404");
    } else if (response.status == 403) {
        redirect("/403");
    } else redirect("/oops");
}

function showQuestion() {
    if (!created) return;

    document.getElementById("delete-question").hidden = false;
    document.title = `HS Stars | ${question.title}`;
    document.getElementById("page_function").textContent = "Update Question";

    document.getElementById("delete-question").addEventListener("click", deleteQuestion);
    document.getElementById("create-question").textContent = "Save";
    document.getElementById("questionTitle").value = question.title;
    document.getElementById("questionType").selectedIndex = questionTypeToIdx[question.questionType] + 1;
    document.getElementById("questionType").disabled = true;
    if (document.getElementById("questionType").selectedIndex == 1) {
        document.getElementById("lessonContent").required = true;
        document.getElementById("lessonContentField").hidden = false;
        document.getElementById("lessonContent").value = question.statement;
        document.getElementById("widgetCode").required = false;
        document.getElementById("questionWidgetField").hidden = true;
    } else {
        document.getElementById("lessonContent").required = false;
        document.getElementById("lessonContentField").hidden = true;
        document.getElementById("widgetCode").value = question.widgetCode;
        document.getElementById("widgetCode").required = true;
        document.getElementById("questionWidgetField").hidden = false;
    }
}

async function submit(e) {
    e.preventDefault();
    e.stopPropagation();
    let request_method = created ? "PUT" : "POST";
    // alert(request_method)
    let url = `/api/question/${created ? questionId : ""}`;

    question["title"] = document.getElementById("questionTitle").value;
    question["questionType"] = document.getElementById("questionType").selectedIndex - 1;

    question["widgetCode"] = document.getElementById("widgetCode").value;

    question["statement"] = document.getElementById("lessonContent").value;

    const response = await fetch(url, {
        method: request_method,
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            Authorization: "Bearer " + (keycloak.token || loginToken),
            Accept: "application/json",
        },
        body: JSON.stringify(question),
        redirect: "follow",
    });
    if (response.ok) {
        goBack();
    } else {
        redirectError(response);
    }
    return false;
}

async function showDiv() {
    let ind = document.getElementById("questionType").selectedIndex;
    document.getElementById("widgetCode").required = false;
    document.getElementById("lessonContent").required = false;
    if (ind === 1) {
        document.getElementById("lessonContent").required = true;
        document.getElementById("lessonContentField").hidden = false;
        document.getElementById("questionWidgetField").hidden = true;
    } else if (ind === 2) {
        document.getElementById("widgetCode").required = true;
        document.getElementById("lessonContentField").hidden = true;
        document.getElementById("questionWidgetField").hidden = false;
    }
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            document.getElementById("submitForm").addEventListener("submit", submit);
            getQuestion()
                .then(function () {
                    showQuestion();
                    loadNavBar();
                    displayMain();
                });
        }
    }).catch(function (e) {
        console.log(e);
        redirect("/oops");
    });
    document.getElementById("questionType").addEventListener("change", showDiv);
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
