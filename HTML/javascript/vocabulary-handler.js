//to reassign all links each time we update language
const allLinks = document.querySelectorAll("header a, nav a");

//all translatable elements from different pages
const menuElements = document.querySelectorAll("[data-menu]");

const AILogicPageElements = document.querySelectorAll("[data-ai]");
const teamPageElements = document.querySelectorAll("[data-team]");
const guidelinesPageElements = document.querySelectorAll("[data-guidelines]");
const implementationPageElements = document.querySelectorAll("[data-implementation]");

const feedbackPageElements = document.querySelectorAll("[data-feedback]");
const feedbackPlaceHolders = document.querySelectorAll("[data-feedback-placeholder]");
const feedbackValues = document.querySelectorAll("[data-feedback-value]");

const galleryPageElements = document.querySelectorAll("[data-gallery]");
const galleryPageAltImageElements = document.querySelectorAll("[data-gallery-alt]");

const statsPageElements = document.querySelectorAll("[data-stats]");
const statsPagePlaceholders = document.querySelectorAll("[data-stats-placeholder]");

const homePageElements = document.querySelectorAll("[data-home]");


let currentLanguage = "EN";

const languageBox = document.getElementById("lang-sel-box");
languageBox.addEventListener("change", () => {
    updateLanguageUrl();//reassigning all variables to new href with changed ?lang=
    updatePagesTranslation();
})

updatePagesTranslation();


function updatePagesTranslation() {
    //top bar
    translateMenuItems();
    assignAllLinks();
    updateSelectionBox();

    //all other pages
    translateHomePage();
    translateTeamPage();
    translateAILogicPage();
    translateImplementationPage();
    translateFeedbackPage();
    translateGalleryPage();
    translateGuidelinesPage();
    translateStatsPage();
}

function updateSelectionBox() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    languageBox.value = currentLanguage;
}

function updateLanguageUrl() {
    currentLanguage = languageBox.value;
    const url = new URL(window.location.href);
    url.searchParams.set("lang", currentLanguage);
    window.history.replaceState({}, "", url);
    //updating language based on selected item in selection box
}

function findWord(currentLanguage, key) {
    return vocabularyData[currentLanguage][key];
}




function translateMenuItems() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of menuElements) {
        const translation = findWord(currentLanguage, el.dataset.menu);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateTeamPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of teamPageElements) {
        const translation = findWord(currentLanguage, el.dataset.team);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateAILogicPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of AILogicPageElements) {
        const translation = findWord(currentLanguage, el.dataset.ai);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateImplementationPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of implementationPageElements) {
        const translation = findWord(currentLanguage, el.dataset.implementation);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateFeedbackPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of feedbackPageElements) {
        const translation = findWord(currentLanguage, el.dataset.feedback);
        if (translation !== undefined) el.textContent = translation;
    }


    //when js is processing data-name-name tage, in dataset of element is converting to dataset.nameName
    for (let el of feedbackPlaceHolders) {
        const translation = findWord(currentLanguage, el.dataset.feedbackPlaceholder);
        if (translation !== undefined) el.placeholder = translation;
    }

    for (let el of feedbackValues) {
        const translation = findWord(currentLanguage, el.dataset.feedbackValue);
        if (translation !== undefined) el.value = translation;
    }
}

function translateGalleryPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of galleryPageElements) {
        const translation = findWord(currentLanguage, el.dataset.gallery);
        if (translation !== undefined) el.textContent = translation;
    }

    for (let el of galleryPageAltImageElements) {
        const translation = findWord(currentLanguage, el.dataset.galleryAlt);
        if (translation !== undefined) el.alt = translation;
    }
}

function translateGuidelinesPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of guidelinesPageElements) {
        const translation = findWord(currentLanguage, el.dataset.guidelines);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateHomePage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of homePageElements) {
        const translation = findWord(currentLanguage, el.dataset.home);
        if (translation !== undefined) el.textContent = translation;
    }
}

function translateStatsPage() {
    const searchBar = window.location.search;
    const params = new URLSearchParams(searchBar);
    currentLanguage = params.get("lang") || "EN"; //for first loading
    for (let el of statsPageElements) {
        const translation = findWord(currentLanguage, el.dataset.stats);
        if (translation !== undefined) el.innerHTML = translation;
    }

    for (let el of statsPagePlaceholders) {
        const translation = findWord(currentLanguage, el.dataset.statsPlaceholder);
        if (translation !== undefined) el.placeholder = translation;
    }


    const download_line = document.getElementById("download_from_stats");
    if(download_line == null) return;
    const url = new URL(download_line.href);
    url.searchParams.set("lang", currentLanguage);
    download_line.href = url.toString();
}

function assignAllLinks() {
    const searchBar = window.location.search;
    let params = new URLSearchParams(searchBar);
    let lang = params.get("lang") || "EN";  //just in case
    allLinks.forEach(element => {
        const url = new URL(element.href);
        url.searchParams.set("lang", lang);
        element.href = url.toString();
    });
}