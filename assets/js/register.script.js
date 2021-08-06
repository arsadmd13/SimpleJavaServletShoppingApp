window.onload = () => {
    let queryString = new URLSearchParams(window.location.search.substring(1));
    if(queryString.has('PasswordDoesn\'tMatch') && queryString.get('PasswordDoesn\'tMatch')) {
        raiseNewError("pdme", "Password doesn't match!");
    } else if(queryString.has('DuplicateRegistration') && queryString.get('DuplicateRegistration')) {
        raiseNewError("dupReg", "This email has been already registered. Please login with the same!");
    }
}
function validateForm(){
    if(document.getElementById('password').value !== document.getElementById('confirmPassword').value) {
        raiseNewError("pdme", "Password doesn't match!");
        return false;
    }
    return true;
}

function raiseNewError(errorId, errorMessage) {
    if(document.getElementById(errorId)) return;
    let errorContainer = document.getElementById('errors');
    let newErrorElement = document.createElement('small');
    newErrorElement.id = errorId;
    newErrorElement.innerText = errorMessage;
    errorContainer.appendChild(newErrorElement);
    errorContainer.style.display = "block";
}