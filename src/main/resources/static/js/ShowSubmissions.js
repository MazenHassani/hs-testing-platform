let userId;
let submissions = {};
let currentSort;
let ascending = true;
let questionWeights = {}

function goBack() {
    redirect(`/course/${courseId}/test/${testId}/tasks`);
}

async function getTest() {
    if (!courseId || !testId) {
        goBack();
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
            Authorization: "Bearer " + (keycloak.token || loginToken),
            Accept: "application/json",
        },
        redirect: "follow",
    });
    if (testResponse.ok) {
        test = await testResponse.json();
        document.getElementById("test-title").textContent = test.name;
        return test;
    } else {
        redirectError(testResponse);
    }
}

async function getScores(testId) {
    submissions = [];
    const response = await fetch(`/api/submission/${taskId}`, {
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
    submissions = await response.json();
}

function setUserSubject() {
    userId = keycloak.subject;
}

function showTestQuestions(testQuestions) {
    const tableHeader = document.querySelector('#scoreboard-table thead tr');
    const scoreCol = document.getElementById('score-col');
    const templateRow = document.getElementById('user-score').content.querySelector('tr');
    const scoreTemplate = templateRow.querySelector('.total-score');
    testQuestions.forEach((testQuestion) => {
        if (testQuestion.questionType !== "SPHERE")
            return;
        const tqElem = document.createElement('th');
        tqElem.id = `tq-${testQuestion.id}`;
        tqElem.classList.add('col');
        tqElem.classList.add('text-center');
        tqElem.textContent = testQuestion.questionTitle;
        tableHeader.insertBefore(tqElem, scoreCol);

        document.getElementById(tqElem.id).style.cursor = "pointer";

        document.getElementById(tqElem.id).addEventListener("click", function () {
            sortTable(`.tq-${testQuestion.id}`);
        });

        const tqTemplate = document.createElement('td');
        tqTemplate.textContent = '-';
        tqTemplate.classList.add('col');
        tqTemplate.classList.add('text-center');
        tqTemplate.classList.add('q-score');
        tqTemplate.classList.add(`tq-${testQuestion.id}`);
        templateRow.insertBefore(tqTemplate, scoreTemplate);

        questionWeights[testQuestion.id] = testQuestion.weight;
    });
}

function setTestURL() {
    const scoreboardBtn = document.getElementById("test");
    scoreboardBtn.href = `/course/${courseId}/test/${testId}/tasks`;
}

async function showSubmission(submissionApiId) {
    const response = await fetch(`/api/code/problems/${submissionApiId}/result`, {
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
    if (!response.ok) {
        redirectError(response);
    } else {
        document.getElementById("code").textContent = await response.text();
    }
}

function showUserScores(usersScores) {
    let table_elem = document.querySelector("#scoreboard-table tbody");
    let template_name = "user-score"
    table_elem.innerHTML = "";
    usersScores.forEach((userScores) => {
        let scoreboardRow = document.getElementById(template_name).content.cloneNode(true);
        scoreboardRow.querySelector(".user-id").innerText = userScores.userId;
        let totalScore = 0;
        const scores = userScores.scoreResponses;
        for (let i = 0; i < scores.length; i++) {
            let current_elem = scoreboardRow.querySelector(`.tq-${scores[i].testQuestionId}`);
            if(!current_elem) continue;
            if (!isStudent) {
                current_elem.addEventListener("click", async function () {
                    await showSubmission(scores[i].submissionApiId);
                });
                current_elem.setAttribute("data-toggle", "modal");
                current_elem.style.cursor = "pointer";
                current_elem.setAttribute("data-target", ".bd-example-modal-lg");
            }
            if (scores[i].status === 'SUCCESS') {
                totalScore += questionWeights[scores[i].testQuestionId];
                current_elem.textContent = questionWeights[scores[i].testQuestionId];
            } else {
                current_elem.textContent = '0';
            }
        }
        scoreboardRow.querySelector(".total-score").innerText = totalScore;
        table_elem.append(scoreboardRow);
    });

}


function highlightCol(col_name) {
    const bg_class = "highlight-color";
    const text_class = "text-white";
    const cells = document.getElementsByTagName("td");
    const highlightCols = document.getElementsByClassName(col_name);
    for (let i = 0; i < cells.length; i++) {
        if (cells[i].classList.contains(bg_class)) {
            cells[i].classList.remove(bg_class);
            cells[i].classList.remove(text_class);
        }
    }
    for (let i = 0; i < highlightCols.length; i++){
        highlightCols[i].classList.add(bg_class);
        highlightCols[i].classList.add(text_class);
    }
}

function setSorters() {
    document.getElementById("score-col").style.cursor = "pointer";
    document.getElementById("score-col").addEventListener("click", function () {
        sortTable(".total-score");
    });
    document.getElementById("user-col").style.cursor = "pointer";
    document.getElementById("user-col").addEventListener("click", function () {
        sortTable(".user-id");
    });
}

function sortTable(feat) {
    // feat = total-score for example
    highlightCol(feat.slice(1));
    const table = document.getElementById("scoreboard-table");
    let switching = true;
    if (currentSort == feat) {
        ascending = !ascending;
    } else {
        ascending = feat == ".user-id";
        currentSort = feat;
    }


    let asc = ascending;

    // Run loop until no switching is needed
    while (switching) {
        switching = false;
        let rows = table.getElementsByTagName("tr");

        // Loop to go through all rows
        for (let i = 1; i < (rows.length - 1); i++) {

            // Fetch 2 elements that need to be compared
            let x = rows[i].querySelector(feat).textContent;
            let y = rows[i + 1].querySelector(feat).textContent;


            if (asc)
                [x, y] = [y, x]

            // Check if 2 rows need to be switched
            if (x < y) {
                // If yes, mark Switch as needed and break loop
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
                break;
            }
        }

    }
}

function showScores() {
    //const userScores = scoreboard.userInformation;
    const submissionss = submissions.??;
    showTestQuestions(submissionss);
    //showUserScores(userScores, testQuestions);
}


async function init() {
    initKeyCloak(true).then((authenticated) => {
        console.log(authenticated ? "authenticated" : "not authenticated");
        if (authenticated) {
            Promise.all([
                getRole(courseId),
                setUserSubject(),
                getTest(),
                getScores(testId)]).then(function () {
                showScores();
                document.querySelector(".nav").hidden = false;
                setTestURL();

                document.getElementById("scoreboard-tab").click();
                setSorters();
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
