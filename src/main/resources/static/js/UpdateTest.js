let course = {};
let test = {};
let create = true;
let valid = false;

async function getTest() {
    let url = `/api/course/${courseId}`;
    if (!courseId) {
        return;
    }

    // getting course name
    course = {};
    const courseResponse = await fetch(url, {
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
    if (!courseResponse.ok) {
        redirect("/404");
    }
    course = await courseResponse.json();
    valid = true;

    if (!testId)
        return
    //seeing whether the test is available or not (create or update)
    url = `/api/course/${courseId}/test/${testId}`;
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
    if (!testResponse.ok) {
        redirect("/404");
    }
    test = await testResponse.json();
    create = false;
}

function goBack() {
    redirect(`/course/${courseId}`);
}

async function deleteTest(e) {
    e.preventDefault();
    e.stopPropagation();
    const response = await fetch(`/api/course/${courseId}/test/${testId}`, {
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
        goBack();
    } else {
        redirectError(response);
    }
}

function showForm() {
    let courseField = document.getElementById("courseTitle");
    courseField.innerText = course["name"];
    if (create) {
        return
    }

    document.getElementById("delete-test").hidden = false;
    document.getElementById("update-test-questions").hidden = false;
    document.title = `HS Stars | ${test.name}`;
    document.getElementById("page_function").textContent = "Update Test"
    document.getElementById("save-test").textContent = "Save"
    document.getElementById("delete-test").addEventListener("click", deleteTest);
    document.getElementById("update-test-questions").addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        redirect(`/course/${courseId}/test/${testId}/questions`);
        return false;
    });

    let tname = document.getElementById("test-name");
    tname.value = test["name"];

    let startDate = document.getElementById("test-start-date");
    startDate.value = test["startDate"].substring(0, test["startDate"].indexOf(" "));
    let startTime = document.getElementById("test-start-time");
    startTime.value = test["startDate"].substring(test["startDate"].indexOf(" ") + 1);

    let endDate = document.getElementById("test-end-date");
    endDate.value = test["endDate"].substring(0, test["endDate"].indexOf(" "));
    let endTime = document.getElementById("test-end-time");
    endTime.value = test["endDate"].substring(test["endDate"].indexOf(" ") + 1);

    // let duration = document.getElementById("test-duration");
    // duration.value = test["duration"];

    // let resultsVisibility = document.getElementById("test-results-visibility");
    // resultsVisibility.checked = test["resultsVisibility"];
}

async function requestSubmit(event) {
    event.preventDefault();
    event.stopPropagation();
    if (!valid) return;
    let request_method = create ? "POST" : "PUT";
    let url = `/api/course/${courseId}/test/${create ? "" : testId}`;
    let tname = document.getElementById("test-name");
    test["name"] = tname.value;

    let startDate = document.getElementById("test-start-date");
    let startTime = document.getElementById("test-start-time");
    test["startDate"] = startDate.value + " " + startTime.value;

    let endDate = document.getElementById("test-end-date");
    let endTime = document.getElementById("test-end-time");
    test["endDate"] = endDate.value + " " + endTime.value;

    // let duration = document.getElementById("test-duration");
    // test["duration"] = duration.value;

    // let resultsVisibility = document.getElementById("test-results-visibility");
    // test["resultsVisibility"] = resultsVisibility.checked;
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
        body: JSON.stringify(test),
        redirect: "follow",
    });
    if (response.ok) {
        goBack();
    } else {
        redirectError(response);
    }
    return false;
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            const submitForm = document.getElementById("submitForm");
            submitForm.addEventListener("submit", requestSubmit)
            Promise.all([
                getRole(courseId).then((isStudent) => {
                    if (isStudent) {
                        redirect("/403");
                    }
                }),
                getTest()])
                .then(function () {
                    showForm();
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
