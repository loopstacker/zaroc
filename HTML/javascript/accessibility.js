// button menu toggle
document.getElementById('accessibility-toggle')
    .addEventListener('click', function (e) {
        e.stopPropagation();
        document.getElementById('accessibility-menu')
            .classList.toggle('display-none');
    });

document.addEventListener('click', function (e) {
    const widget = document.getElementById('accessibility-widget');
    if (!widget.contains(e.target)) {
        document.getElementById('accessibility-menu')
            .classList.add('display-none');
    }
});


// font size
let fontSize = parseInt(sessionStorage.getItem('fontSize')) || 16; // will check for th session storage item each time
document.documentElement.style.fontSize = fontSize + 'px';

document.getElementById('font-decrease')
    .addEventListener('click', function () { changeFontSize(-1); });

document.getElementById('font-reset')
    .addEventListener('click', function () { resetFontSize(); });

document.getElementById('font-increase')
    .addEventListener('click', function () { changeFontSize(1); });

function changeFontSize(amount) {
    fontSize = Math.min(24, Math.max(12, fontSize + amount));
    document.documentElement.style.fontSize = fontSize + 'px';
    sessionStorage.setItem('fontSize', fontSize); // THIS STORAGE it will be the font size inside this function
}

function resetFontSize() {
    fontSize = 16;
    document.documentElement.style.fontSize = '16px';
    sessionStorage.setItem('fontSize', 16);
}

// colorblind
const savedColorblind = sessionStorage.getItem('colorblind') || 'none'; // will check for th session storage item each time
applyColorblind(savedColorblind);
document.getElementById('colorblind-select').value = savedColorblind;

document.getElementById('colorblind-select')
    .addEventListener('change', function () {
        applyColorblind(this.value);
        sessionStorage.setItem('colorblind', this.value); // THIS STORAGE it will be the font size inside this function
    });

function applyColorblind(mode) {
    document.documentElement.classList.remove('cb-deuteranopia', 'cb-protanopia', 'cb-tritanopia', 'cb-grayscale');

    if (mode !== 'none') {
        document.documentElement.classList.add('cb-' + mode);
    }
}

// dyslexia font
let dyslexiaOn = sessionStorage.getItem('dyslexia') === 'true';
if (dyslexiaOn) {
    document.body.classList.add('dyslexia-mode');
    document.getElementById('dyslexia-btn').textContent = 'On';
}

document.getElementById('dyslexia-btn')
    .addEventListener('click', function () {
        dyslexiaOn = !dyslexiaOn;
        document.body.classList.toggle('dyslexia-mode', dyslexiaOn);
        this.textContent = dyslexiaOn ? 'On' : 'Off';
        sessionStorage.setItem('dyslexia', dyslexiaOn);
    });