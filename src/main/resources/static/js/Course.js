let course = {};
let tests = [];
let invites = [];


function goBack() {
    redirect("/");
}

async function getCourse() {
    const response = await fetch(`/api/course/${courseId}/`, {
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
    if (!response.ok) {
        redirect("/404");
    }
    course = await response.json();
}

function showCourse() {
    document.title = `HS Stars | ${course.name}`;
    document.getElementById("course-id").innerText = courseId;
    document.getElementById("course-name").innerText = course.name;
    document.getElementById("course-start-date").innerText = "Starts on:\t" + course.startDate;
    document.getElementById("course-end-date").innerText = "Ends on:\t" + course.endDate;
    document.getElementById("add-test").addEventListener("click", function () {
        redirect(`/course/${courseId}/test/create`);
    });
    document.getElementById("update-course").addEventListener("click", function () {
        redirect(`/course/${courseId}/update`);
    });
    document.getElementById("invite-users").addEventListener("click", function () {
        redirect(`/course/${courseId}/invite`);
    });
}

async function getTests() {
    tests = [];
    const response = await fetch(`/api/course/${courseId}/test`, {
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
    const responseBody = await response.json();
    tests = responseBody["content"];
}

async function getInvites() {
    invites = [];
    const response = await fetch(`/api/invite/course/${courseId}?acceptedOnly=true`, {
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
    if (!response.ok) {
        redirectError(response);
    }
    const responseBody = await response.json();
    invites = responseBody["content"];
}

async function deleteTest(testId) {
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
    if (!response.ok) {
        redirectError(response);
    }
    return response;
}

function showTests() {
    let upcomingTableBodyElm = document.querySelector("#upcoming-test-list tbody");
    let pastTableBodyElm = document.querySelector("#past-test-list tbody");
    let currentTableBodyElm = document.querySelector("#current-test-list tbody");
    upcomingTableBodyElm.innerHTML = "";
    pastTableBodyElm.innerHTML = "";
    currentTableBodyElm.innerHTML = "";
    tests.forEach((test) => {
        let newTest = document.getElementById("test-template").content.cloneNode(true);
        newTest.querySelector(".test-id").innerText = test.id;
        newTest.querySelector(".test-name").innerText = test.name;
        newTest.querySelector(".test-start-date").innerText = test.startDate;
        newTest.querySelector(".test-end-date").innerText = test.endDate;
        // newTest.querySelector(".test-duration").innerText = test.duration;
        // newTest.querySelector(".test-visibility").innerText = test.resultsVisibility;
        // newTest.querySelector(".test-weight").innerText = test.weight;
        newTest.querySelector(".test-scoreboard button").href = `/course/${courseId}/test/${test.id}/scoreboard`;
        newTest.querySelector(".test-scoreboard").addEventListener("click", function () {
            redirect(`/course/${courseId}/test/${test.id}/scoreboard`);
        });
        newTest.querySelector(".test-tasks button").href = `/course/${courseId}/test/${test.id}/tasks`;
        newTest.querySelector(".test-tasks button").addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            redirect(this.href);
            return false;
        });
        newTest.querySelector(".test-update").addEventListener("click", function () {
            redirect(`/course/${courseId}/test/${test.id}/update`);
        });
        newTest.querySelector(".test-remove").addEventListener("click", async function () {
            deleteTest(test.id).then((res) => async function () {
                if (res) {
                    alert("Test deleted!");
                    window.location.reload();
                } else {
                    redirectError(res);
                }
            });
        });
        let startDate = new Date(test.startDate.replace(/ /g,"T"));
        let endDate = new Date(test.endDate.replace(/ /g,"T"));
        if (startDate > new Date()) {
            newTest.querySelector(".test-tasks").remove();
            newTest.querySelector(".test-scoreboard").remove();
            upcomingTableBodyElm.append(newTest);
        } else if (endDate < new Date()) {
            newTest.querySelector(".test-update").remove();
            newTest.querySelector(".test-remove").remove();
            pastTableBodyElm.append(newTest);
        } else {
            newTest.querySelector(".test-remove").remove();
            currentTableBodyElm.append(newTest);
        }
    });
}

function deleteInvite(inviteId) {
    return async function () {
        const response = await fetch(`/api/invite/${inviteId}/`, {
            method: "DELETE",
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
        if (response.ok) {
            getInvites().then(showInvites);
        } else {
            redirectError(response);
        }
    };
}

function showInvites() {
    let teachersTableBodyElm = document.querySelector("#teachers-list tbody");
    let studentsTableBodyElm = document.querySelector("#students-list tbody");
    teachersTableBodyElm.innerHTML = "";
    studentsTableBodyElm.innerHTML = "";
    invites.forEach((invite) => {
        let newInvite = document.getElementById("invite-template").content.cloneNode(true);
        newInvite.querySelector(".invite-id").innerText = invite.id;
        newInvite.querySelector(".invite-user-id").innerText = invite.userId;
        newInvite.querySelector(".invite-user-name").innerText = invite.username;
        newInvite.querySelector(".invite-date").innerText = invite.createdOn;
        newInvite.querySelector(".invite-accept-date").innerText = invite.updatedOn;
        newInvite.querySelector(".invite-remove button").addEventListener("click", deleteInvite(invite.id));
        if (invite.type === "STUDENT") {
            studentsTableBodyElm.append(newInvite);
        } else {
            teachersTableBodyElm.append(newInvite);
        }
    });
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([
                getCourse(),
                getTests(),
                getInvites(),
                getRole(courseId),
            ]).then(function () {
                showCourse();
                showTests();
                showInvites();
                document.getElementById('tests-tab').click();
                loadNavBar();
                refreshTeacherView();
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
