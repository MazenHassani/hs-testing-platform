let course = {};
let created = false;

function goBack() {
    if (created)
        redirect(`/course/${courseId}`)
    else
        redirect('/')
}

async function getCourse() {
    if (!courseId)
        return
    course = {};
    const response = await fetch(`/api/course/${courseId}/`, {
        method: "GET",
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
        },
        redirect: "follow",
    });
    if (response.ok) {
        created = true;
        course = await response.json();
    } else
        redirect("/404");
}

async function deleteCourse(e) {
    e.preventDefault();
    e.stopPropagation();
    const response = await fetch(`/api/course/${courseId}/`, {
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
        redirect("/");
    } else {
        redirectError(response);
    }
}

function showCourse() {
    if (!created)
        return;


    document.getElementById("delete-course").hidden = false;
    document.title = `HS Stars | ${course.name}`;
    document.getElementById("page_function").textContent = "Update Course"
    document.getElementById("save-course").textContent = "Save"
    document.getElementById("delete-course").addEventListener("click", deleteCourse);
    document.getElementById("course-name").value = course.name;
    document.getElementById("course-start-date").value = course.startDate.substring(0, course.startDate.indexOf(" "));
    document.getElementById("course-start-time").value = course.startDate.substring(course.startDate.indexOf(" ") + 1);
    document.getElementById("course-end-date").value = course.endDate.substring(0, course.endDate.indexOf(" "));
    document.getElementById("course-end-time").value = course.endDate.substring(course.endDate.indexOf(" ") + 1);
}


async function submit(e) {
    e.preventDefault();
    e.stopPropagation();
    let request_method = created ? "PUT" : "POST";
    let url = `/api/course/${created ? courseId : ''}`;

    course["name"] = document.getElementById("course-name").value;

    let startDate = document.getElementById("course-start-date");
    let startTime = document.getElementById("course-start-time");
    course["startDate"] = startDate.value + " " + startTime.value;

    let endDate = document.getElementById("course-end-date");
    let endTime = document.getElementById("course-end-time");
    course["endDate"] = endDate.value + " " + endTime.value;

    const response = await fetch(url, {
        method: request_method,
        mode: "same-origin",
        credentials: "same-origin",
        cache: "default",
        headers: {
            "content-type": "application/json",
            'Authorization': 'Bearer ' + (keycloak.token || loginToken),
            'Accept': 'application/json',
        },
        body: JSON.stringify(course),
        redirect: "follow",
    });
    if (response.ok) {
        goBack();
        course = await response.json();
    } else {
        redirectError(response);
    }
    return false;
}

async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            document.getElementById("submitForm").addEventListener("submit", submit);
            Promise.all([
                getRole(courseId).then((isStudent) => {
                    if (isStudent) {
                        redirect("/403");
                    }
                }),
                getCourse()])
                .then(function () {
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
