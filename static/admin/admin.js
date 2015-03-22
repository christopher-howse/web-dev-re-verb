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
}

function getUserPosts(username)
{
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

function Confirmation(message, action)
{
    this.message = message;
    this.action = action;
}

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

var userTableTemplate = '<table>'
    + '<tr><th>Username</th><th>Password</th><th>Role</th><th>Number of Reports</th><th>Account Status</th><th>Edit</th></tr>'
    + '{{#.}}<tr>'
        + '<td onclick=\"getUserPosts(\'{{name}}\')\">{{name}}</td>'
        + '<td>{{password}}</td>'
        + '<td onclick=\"toggleUserRole(\'{{name}}\')\">{{role}}</td>'
        + '<td>{{num_reports}}</td>'
        + '<td onclick=\"toggleSuspendUser(\'{{name}}\')\">{{accountStatus}}</td>'
        + '<td onclick=\"editUserInfo(\'{{name}}\')\">put img here</td>'
    + '</tr>{{/.}}'
    + '</table>';

var userPostsTemplate = '<table>'
    + '<tr><th>Post ID</th><th>Time</th><th>Post</th><th></th></tr>'
    + '{{#.}}<tr>'
        + '<td>{{postId}}</td>'
        + '<td>Fake Time</td>'
        + '<td>{{postBody}}</td>'
        + '<td onclick=\"deletePost(\'{{postId}}\', \'{{username}}\')\">Delete</td>'
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

var confirmationTemplate = '<div class=\"buttonHolder\">'
    + '<p>Are you sure you want to {{message}}?</p>'
    + '<button class=common onclick={{action}}>Yes</button>'
    + '<button class=common onclick=\"closeConfirmationDialog()\">No</button>'
    + '</div>';