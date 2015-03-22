//on load, ajax request for all the users from the server
var users;
var userPosts;

function getUsers(evt)
{
    var getUsersUrl = window.location.origin + '/admin/getUsers';
    var xhr = new XMLHttpRequest();
    xhr.open('GET', getUsersUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            users = JSON.parse(xhr.responseText);
            //for user in users, create table row with mustache
            var rendered = Mustache.render(userTableTemplate, users);
            document.getElementById('usersDiv').innerHTML = rendered;
        }
        else {
            console.log("getUsers Failed");
        }
    };
    xhr.send();

    var userTableTemplate = '<table>'
        + '<tr><th>Username</th><th>Password</th><th>Role</th><th>Number of Reports</th><th>Account Status</th><th>Edit</th></tr>'
        + '{{#.}}<tr>'
            + '<td class=clickable onclick=\"getUserPosts(\'{{name}}\')\">{{name}}</td>'
            + '<td>{{password}}</td>'
            + '<td class=clickable onclick=\"toggleUserRole(\'{{name}}\')\">{{role}}</td>'
            + '<td>{{num_reports}}</td>'
            + '<td class=clickable onclick=\"toggleSuspendUser(\'{{name}}\')\">{{accountStatus}}</td>'
            + '<td class=clickable onclick=\"getUserInfo(\'{{name}}\')\">put img here</td>'
        + '</tr>{{/.}}'
        + '</table>';
}

function getUserPosts(username)
{
    var userPostsTemplate = '<table>'
        + '<tr><th>Post ID</th><th>Time</th><th>Post</th><th></th></tr>'
        + '{{#.}}<tr>'
            + '<td>{{postId}}</td>'
            + '<td>{{timeStamp}}</td>'
            + '<td>{{postBody}}</td>'
            + '<td class=clickable onclick=\"deletePost(\'{{postId}}\', \'{{username}}\')\">Delete</td>'
        + '</tr>{{/.}}'
        + '</table>';
    var userBannerTemplate = '<button class=\"close-button\" onclick=\"closePostDiv()\">'
        + '<img class=\"close-button\" src=\"imgs/close-icon-light.png\">'
        + '</button>'
        + '<h2>{{username}}</h2>';
    var userActionsTemplate = '<div class=\"buttonHolder\">'
        + '<button class=common onclick=\"toggleUserRole(\'{{username}}\')\">Toggle User Role</button>'
        + '<button class=common onclick=\"toggleSuspendUser(\'{{username}}\')\">Toggle User Suspension</button>'
        + '<button class=common onclick=\"confirmDeleteUser(\'{{username}}\')\">Delete User</button>'
        + '</div>';

    closeUsersDiv();
    var usernameDto = {username : username};
    var getUserPostsUrl = window.location.origin + '/admin/getUserPosts';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', getUserPostsUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            userPosts = JSON.parse(xhr.responseText);
            //for user in users, create table row with mustache
            var rendered = Mustache.render(userBannerTemplate, usernameDto)
                + Mustache.render(userPostsTemplate, userPosts)
                + Mustache.render(userActionsTemplate, usernameDto);
            document.getElementById('userPostsDiv').innerHTML = rendered;
        }
        else {
            console.log("getUserPosts Failed");
        }
    };
    var doc = JSON.stringify(usernameDto);
    xhr.send(doc);
}

//TOGGLE ACCOUNT STATES

function toggleUserRole(username)
{
    var usernameDto = {username : username};
    var toggleUserRoleUrl = window.location.origin + '/admin/toggleUserRole';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', toggleUserRoleUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //If it worked, just call getUsers to update the users table
            closePostDiv();
        }
        else {
            console.log("toggleUserRole Failed");
        }
    };
    var doc = JSON.stringify(usernameDto);
    xhr.send(doc);
}

function toggleSuspendUser(username)
{
    var usernameDto = {username : username};
    var toggleSuspendUserUrl = window.location.origin + '/admin/toggleSuspendUser';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', toggleSuspendUserUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //If it worked, just call getUsers to update the users table
            closePostDiv();
        }
        else {
            console.log("toggleSuspendUser Failed");
        }
    };
    var doc = JSON.stringify(usernameDto);
    xhr.send(doc);
    console.log("hit suspend user");
}

//DELETES

function deletePost(postId, username)
{
    var postIdDto = {id : postId};
    var deletePostUrl = window.location.origin + '/admin/deletePost';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', deletePostUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //If it worked, just call getUserPosts to update the post table
            getUserPosts(username);
        }
        else {
            console.log("deletePost Failed");
        }
    };
    var doc = JSON.stringify(postIdDto);
    xhr.send(doc);
}

function deleteUser(username)
{
    console.log("hit delete user");
    var usernameDto = {username : username};
    var deleteUserUrl = window.location.origin + '/admin/deleteUser';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', deleteUserUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //If it worked, just call getUsers to update the users table
            var success = JSON.parse(xhr.responseText);
            if(success)
            {
                closePostDiv();
            }
            else
            {
                console.log("deleteUser Failed: database error")
            }
        }
        else
        {
            console.log("deleteUser Failed: communication error");
        }
    };
    var doc = JSON.stringify(usernameDto);
    xhr.send(doc);
    console.log("hit delete user");
}

function confirmDeleteUser(username)
{
    var action = "deleteUser(\"" + username + "\")";
    var message = "delete the user: " + username;
    var confirmation = new Confirmation(message, action);
    //for username, create a confirmation dialog with mustache
    var rendered = Mustache.render(confirmationTemplate, confirmation);
    document.getElementById('confirmationDiv').innerHTML = rendered;
}

//CONFIRMATION CODE

function Confirmation(message, action)
{
    this.message = message;
    this.action = action;
}

var confirmationTemplate = '<div class=\"buttonHolder\">'
    + '<p>Are you sure you want to {{message}}?</p>'
    + '<button class=common onclick={{action}}>Yes</button>'
    + '<button class=common onclick=\"closeConfirmationDialog()\">No</button>'
    + '</div>';


//CLEANUP CALLS

function closeUsersDiv()
{
    document.getElementById('usersDiv').innerHTML = "";
}

function closePostDiv()
{
    document.getElementById('userPostsDiv').innerHTML = "";
    closeConfirmationDialog();
    getUsers();
}

function closeConfirmationDialog()
{
    document.getElementById('confirmationDiv').innerHTML = "";
}

//USER INFO CODE

function UserInfo(newUsername, oldUsername, password, about_me)
{
    this.newUsername = newUsername;
    this.oldUsername = oldUsername;
    this.password = password;
    this.about_me = about_me;
}

function getUserInfo(username)
{
    var userInfoEditTemplate = '<div class="insideOverlay">'
                             + '<label>Username: <input type="text" name="username" value={{name}}></label>'
                             + '<label>Description: <input type="text" name="description" value={{description}}></label>'
                             + '<label>Password: <input type="text" name="password" value={{password}}></label>'
                             + '<div class="buttons">'
                             + '<button class="flatButton" name="btnSave" onclick="saveUserInfo(\"{{name}}\")">Save</button>'
                             + '<button class="flatButton" name="btnCancel" onclick="cancelUserInfo()">Cancel</button>';

    var usernameDto = {username : username};
    var getUserInfoUrl = window.location.origin + '/admin/getUserInfo';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', getUserInfoUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            userInfo = JSON.parse(xhr.responseText);
            //for user in users, create table row with mustache
            var rendered = Mustache.render(userInfoEditTemplate, userInfo);
            document.getElementById("userInfoDiv").innerHTML = rendered;
            showOverlay("userInfoDiv");
        }
        else {
            console.log("getUserInfo Failed");
        }
    };
    var doc = JSON.stringify(usernameDto);
    xhr.send(doc);
}

function saveUserInfo(oldName)
{
    var newName = document.querySelector("input[name='username']").value;
    var description = document.querySelector("input[name='description']").value;
    var password = document.querySelector("input[name='password']").value;
    var saveUserInfoUrl = window.location.origin + '/admin/deleteUser';

    var userInfoDto = new UserInfo(newName, oldName, password, description);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', saveUserInfoUrl, true );
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            hideOverlay('userInfoDiv');
            getUsers();
        }
        else {
            var over = document.getElementById('editUserInfo');
            over.innerHTML += "<p>Unknown Error</p>";
            result.value = "Unknown ERROR";
        }
    };
    // convert to the offical JSON syntax
    var doc = JSON.stringify( userInfoDto );
    xhr.send( doc );
}

function showOverlay(overlay)
{
    document.getElementById('overlay-back').style.display = "block";
    var over = document.getElementById(overlay);
    over.style.display = "block";
}

function hideOverlay(overlay)
{
    document.getElementById('overlay-back').style.display = "none";
    var over = document.getElementById(overlay);
    over.style.display = "none";
}