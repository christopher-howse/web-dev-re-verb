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
    var getUserPostsUrl = window.location.origin + '/admin/getUserPosts';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', getUserPosts, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            userPosts = JSON.parse(xhr.responseText);
            //for user in users, create table row with mustache
            var rendered = Mustache.render(userTableTemplate, users);
            document.getElementById('userPostsDiv').innerHTML = rendered;
        }
        else {
            console.log("getUserPosts Failed");
        }
    };
    var doc = JSON.stringify(username);
    xhr.send(doc);
}

var userTableTemplate = '<table>'
    + '<tr><th>Username</th><th>Password</th><th>Role</th></tr>'
    + '{{#.}}<tr>'
        + '<td onclick=\"getUserPosts(\'{{name}}\')\">{{name}}</td>'
        + '<td>{{password}}</td>'
        + '<td>{{role}}</td>'
    + '</tr>{{/.}}'
    + '</table>';

var userPostsTemplate = '<table>'
    + '<tr><th>Username</th><th>Password</th><th>Role</th></tr>'
    + '{{#.}}<tr>'
        + '<td>{{name}}</td>'
        + '<td>{{password}}</td>'
        + '<td>{{role}}</td>'
    + '</tr>{{/.}}'
    + '</table>';