//on load, ajax request for all the users from the server
function getUsers(evt)
{
    console.log('Hit getUsers()');
    var getUsersUrl = window.location.origin + '/admin/getUsers';
    var xhr = new XMLHttpRequest();
    xhr.open('GET', getUsersUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //user response here
            console.log(xhr.responseText);
        }
        else {
            console.log("getUsers Failed");
        }
    };
    xhr.send();
}

