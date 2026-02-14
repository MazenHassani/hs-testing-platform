let courses = [];
let size = 0;

async function refreshManagerView() {
    if (!isCourseManager(keycloak))
        return;
    let managerElems = document.getElementsByClassName("manager-view");
    for (let i = 0; i < managerElems.length; i++) {
        managerElems[i].hidden = false;
    }
}

async function getCourses() {
    courses = [];
    const response = await fetch("/api/course/", {
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
    courses = await response.json();
}

function showCourses() {
    let coursesElem = document.getElementById("courses-container");
    courses.forEach((course) => {
        let newCourse = document.getElementById("course-template").content.cloneNode(true);
        newCourse.querySelector(".course-name").addEventListener("click", function () {
            redirect(`/course/${course.id}`);
        });
        newCourse.querySelector(".card-title").innerText = course.name;
        newCourse.querySelector(".course-start-date").innerText = "Starts on:\t" + course.startDate;
        newCourse.querySelector(".course-end-date").innerText = "Ends on:\t" + course.endDate;
        coursesElem.append(newCourse);
    });
    if (courses.length === 0) {
        document.getElementById("course-list-message").innerText = "You are not enrolled in any courses yet! :(";
    }
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            getCourses().then(function () {
                showCourses();
                document.getElementById("create-course").addEventListener("click", function () {
                    redirect(`/course/create`);
                });
                loadNavBar();
                refreshManagerView();
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
