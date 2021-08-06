window.onload = () => {
    let queryString = new URLSearchParams(window.location.search.substring(1));
    if(queryString.has('InvalidUser') && queryString.get('InvalidUser')) {
        if(document.getElementById('iu')) return;
        let errorContainer = document.getElementById('errors');
        let newErrorElement = document.createElement('small');
        newErrorElement.id = "iu";
        newErrorElement.innerText = "Invalid username or password!";
        errorContainer.appendChild(newErrorElement);
        errorContainer.style.display = "block";
    } 
    if(queryString.has('Registered!')) {
        if(document.getElementById('rs')) return;
        let successContainer = document.getElementById('success');
        let newSuccessElement = document.createElement('small');
        newSuccessElement.id = "rs";
        newSuccessElement.innerText = "Registration successfull! Login.";
        successContainer.appendChild(newSuccessElement);
        successContainer.style.display = "block";
    }
}