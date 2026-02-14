let invite = {};
let course = {};

async function getInvite() {
    let url = `/api/invite/${invite_code}/`;
    const response = await fetch(url, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow"
    });
    if (response.ok)
        invite = await response.json();
    else
        redirect("/404");
}

async function getCourse() {
    const response = await fetch(`/api/invite/${invite_code}/course`, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow"
    });
    if (response.ok) course = await response.json();
    else
        redirect("/404");
}

async function acceptInvite() {
    const response = await fetch(`/api/invite/${invite_code}/accept/`, {
        method: "PUT",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        redirect: "follow"
    });
    if (response.ok) {
        alert("Invitation was accepted successfully!");
        location.href = `/course/${course.id}`;
    }
    else {
        redirectError(response);
    }
}

function showInvite() {
    let course_clicked = document.getElementById("course-name-clicked");
    course_clicked.textContent = course.name;
    let course_role_clicked = document.getElementById("course-role-clicked");
    course_role_clicked.textContent = invite.type;
    if (invite.hasOwnProperty("userId") && invite.userId) {
        redirect("/404");
    }
    document.getElementById("accept").addEventListener("click", acceptInvite);
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([getInvite(), getCourse()]).then(function () {
                loadNavBar();
                showInvite();
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
