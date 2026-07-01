init();

function init() {
    const emailField =  document.getElementById("email");
    const feedbackForm =  document.getElementById("feedback-form");
    emailField.addEventListener("input", validateEmail);
    feedbackForm.addEventListener("submit", validateSubmit);
}

function validateEmail() {
    const nameField = document.getElementById("name");
    const emailField = document.getElementById("email");
    const emailError = document.getElementById("email-error");

    const nameValue = nameField.value.trim().toLowerCase().replace(" ", ".");
    const emailValue = emailField.value.trim().split("@")[0];

    if (nameValue !== emailValue) {
        emailError.innerText = "Email address does not match name";
        return false;
    } else {
        emailError.innerText = "";
        return true;
    }
}

function validateNumber() {

}

function validateSubmit(e) {
    e.preventDefault();
    const emailField = document.getElementById("email");
    const emailError = document.getElementById("email-error");
    const phoneField = document.getElementById("phone");
    const phoneError = document.getElementById("phone-error");
    const formError = document.getElementById("form-error");

    const phoneValue =phoneField.value.trim();
    const emailValue = emailField.value.trim();

    let isValid = true;

    if (!emailValue && !phoneValue) {
        emailError.textContent = "Please enter a valid email address";
        phoneError.innerText = "Please enter a valid phone number";
        isValid = false;
    } else {
        emailError.innerText = "";
        phoneError.innerText = "";
        formError.innerText = "";
        isValid = true;
    }

    if (isValid && !validateEmail()) {
        isValid = false;
    } else {
        document.getElementById("feedback-form").submit();
        formError.innerText = "";
    }
}