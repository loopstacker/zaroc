const generalSheet = document.getElementById('theme-general');
const homeSheet    = document.getElementById('theme-home');
const teamSheet = document.getElementById("theme-team");
const guidelinesSheet = document.getElementById('theme-guidelines');
const gallerySheet = document.getElementById('theme-gallery');
const statsSheet = document.getElementById('theme-stats');
const implementationSheet = document.getElementById("theme-implementation");
const feedbackSheet = document.getElementById("theme-feedback");
const aiLogicSheet = document.getElementById("theme-ai-logic")
const themeBtn     = document.getElementById('themeBtn');

themeBtn.addEventListener('click', toggleTheme);

function applyTheme(theme) {
    if (theme === 'light') {
        if (generalSheet) generalSheet.href = "/css/light-mode/general-style-light.css";
        if (homeSheet)    homeSheet.href    = "/css/light-mode/home-light.css";
        if (teamSheet)    teamSheet.href    = "/css/light-mode/team-light.css";
        if (guidelinesSheet) guidelinesSheet.href = "/css/light-mode/guidelines-light.css"
        if (gallerySheet) gallerySheet.href = "/css/light-mode/gallery-light.css"
        if (statsSheet) statsSheet.href = "/css/light-mode/stats-light.css"
        if (implementationSheet) implementationSheet.href = "/css/light-mode/implementation-light.css"
        if (feedbackSheet) feedbackSheet.href = "/css/light-mode/feedback-light.css"
        if (aiLogicSheet) aiLogicSheet.href = "/css/light-mode/ai-logic-light.css"
        themeBtn.textContent = 'Dark';
    } else {
        if (generalSheet) generalSheet.href = "/css/dark-mode/general-style-dark.css";
        if (homeSheet)    homeSheet.href    = "/css/dark-mode/home-dark.css";
        if (teamSheet)    teamSheet.href    = "/css/dark-mode/team-dark.css";
        if (guidelinesSheet) guidelinesSheet.href = "/css/dark-mode/guidelines-dark.css"
        if (gallerySheet) gallerySheet.href = "/css/dark-mode/gallery-dark.css"
        if (statsSheet) statsSheet.href = "/css/dark-mode/stats-dark.css"
        if (implementationSheet) implementationSheet.href = "/css/dark-mode/implementation-dark.css"
        if (feedbackSheet) feedbackSheet.href = "/css/dark-mode/feedback-dark.css"
        if (aiLogicSheet) aiLogicSheet.href = "/css/dark-mode/ai-logic-dark.css"
        themeBtn.textContent = 'Light';
    }
    sessionStorage.setItem('theme', theme); // to explain, this is built in the website, it remembers the preferences for that session alone
    // there is also local storage , which remembers it always
}

function replaceDarkPhotos(theme){
    const domainModel = document.getElementById("image-domain-model");
    if(domainModel == null) return
    if(theme === 'light'){
        domainModel.src = "../images/domain-model-light.png";
    }else{
        domainModel.src = "../images/domain-model-dark.png";
    }
}
function toggleTheme() {
    const current = sessionStorage.getItem('theme') || 'dark';
    applyTheme(current === 'dark' ? 'light' : 'dark');
    replaceDarkPhotos(current === 'dark' ? 'light' : 'dark');
}

// restore theme on page load
const savedTheme = sessionStorage.getItem('theme') || 'dark';
applyTheme(savedTheme);
replaceDarkPhotos(savedTheme);