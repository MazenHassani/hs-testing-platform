let redirected = false;
let isStudent = true;
let keycloak = {};
let loginToken = "";

async function initKeyCloak(forceLogin) {
    keycloak = new Keycloak({
        url: "https://dev.harbour.space:8443/auth",
        realm: "HS",
        clientId: "testing-platform",
        "public-client": true,
    });
    await keycloak.init();
    if (forceLogin && !keycloak.authenticated) {
        keycloak.login();
        // console.log(keycloak.token);
        // console.log(keycloak);
        loginToken = keycloak.token;
    }
    return keycloak.authenticated;
}

function loadNavBar() {
    $("header").load("/NavBar.html");
}

function redirect(address) {
    if (redirected)
        return
    window.location.href = address;
    redirected = true;
}

function refreshTeacherView() {
    if (isStudent)
        return;
    let teacherElems = document.getElementsByClassName("teacher-view");
    for (let i = 0; i < teacherElems.length; i++) {
        teacherElems[i].hidden = false;
    }
}


function displayMain() {
    if (redirected)
        return
    document.querySelector("main").hidden = false;
}

function showSuccess(message){
    document.querySelector(".success-message").innerHTML = `${message}`
    document.querySelector(".success-message").hidden = false;

    setTimeout(function () {
        document.querySelector(".success-message").hidden = true;
        document.querySelector(".success-message").innerHTML = "";
    }, 5000);
}

function redirectError(response) {
    if (response.status == 404)
        redirect("/404");
    else if (response.status == 403)
        redirect("/403");
    else if (response.status == 400) {
        response.json().then((response) => {
            console.log(response);
            document.querySelector(".error-message").innerHTML = `${response.message}`
            document.querySelector(".error-message").hidden = false;

            setTimeout(function () {
                document.querySelector(".error-message").hidden = true;
                document.querySelector(".error-message").innerHTML = "";
            }, 5000);
        });
    } else
        redirect("/oops");
}


function isCourseManager(keycloak) {
    if (!keycloak || !keycloak.resourceAccess["testing-platform"] || !keycloak.resourceAccess["testing-platform"].roles)
        return false;
    return keycloak.resourceAccess["testing-platform"].roles.includes("course_manager");
}


async function getRole(courseId) {
    if (isCourseManager(keycloak)) {
        return isStudent = false;
    }
    const response = await fetch(`/api/invite/course/${courseId}/user`, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + loginToken,
            'Accept': 'application/json',
        },
        redirect: "follow",
    });
    if (response.ok) {
        let type = await response.json();
        isStudent = type == "STUDENT";
    } else {
        redirect("/403");
        return true;
    }
    return isStudent;
}
