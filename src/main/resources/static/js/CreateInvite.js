let course = {};


function goBack() {
    redirect(`/course/${courseId}`);
}

async function getCourse() {
    course = {};
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
    if (response.ok) course = await response.json();
    else
        redirect("/404");
}

function showCourse() {
    document.title = `H.S SpaceCraft Invite to Course - ${course.name}`;
    document.getElementById("course-id").innerText = courseId;
    document.getElementById("course-name").innerText = course.name;
}

function showInvites(teacherResponses, studentResponses) {

    document.getElementById("invites").hidden = false;
    let teacherElm = document.getElementById("teachers-invites");
    let studentElm = document.getElementById("students-invites");
    teacherResponses.forEach((response) => {
        let newInvite = document.getElementById("invite-template").content.cloneNode(true);
        newInvite.querySelector(
            ".invite-link"
        ).href = `/invite/${response.invitationCode}`;
        newInvite.querySelector(".invite-link").innerText = response.invitationCode;
        teacherElm.append(newInvite);
    });
    studentResponses.forEach((response) => {
        let newInvite = document.getElementById("invite-template").content.cloneNode(true);
        newInvite.querySelector(
            ".invite-link"
        ).href = `/invite/${response.invitationCode}`;
        newInvite.querySelector(".invite-link").innerText = response.invitationCode;
        studentElm.append(newInvite);
    });
    document.getElementById("invites").classList.remove("hidden");
}

async function submit(e) {
    e.preventDefault();
    e.stopPropagation();
    document.getElementById("invites").classList.add("hidden");
    let request_method = "POST";
    let url = `/api/invite/`;

    const studentsEmails = document
        .getElementById("invite-students")
        .value.split(",")
        .map((item) => item.trim())
        .filter((item) => item);
    const studentRequest = {
        courseId: courseId,
        type: "STUDENT",
        emails: studentsEmails,
    };
    const teachersEmails = document
        .getElementById("invite-teachers")
        .value.split(",")
        .map((item) => item.trim())
        .filter((item) => item);
    const teachersRequest = {
        courseId: courseId,
        type: "TEACHER",
        emails: teachersEmails,
    };

    let studentResponse = fetch(url, {
        method: request_method,
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        body: JSON.stringify(studentRequest),
        redirect: "follow",
    });
    let teacherResponse = fetch(url, {
        method: request_method,
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        body: JSON.stringify(teachersRequest),
        redirect: "follow",
    });
    studentResponse = await studentResponse;
    teacherResponse = await teacherResponse;
    if (studentResponse.ok && teacherResponse.ok) {
        showSuccess("Invitations sent successfully!");
        showInvites(await teacherResponse.json(), await studentResponse.json());
    } else {
        alert(`Students request: Status: ${studentResponse.status}, body: ${await studentResponse.text()}`);
        alert(`Teachers request: Status: ${teacherResponse.status}, body: ${await teacherResponse.text()}`);
    }
    return false;
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([
                getCourse(),
                getRole(courseId).then((isStudent) => {
                    if (isStudent)
                        redirect("/403");
                }),
                document.getElementById("submitForm").addEventListener("submit", submit)
            ]).then(function () {
                showCourse();
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
