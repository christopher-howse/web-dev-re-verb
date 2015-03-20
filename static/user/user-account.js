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

function editUserInfo()
{
    showOverlay('editUserInfo');
}

function saveUserInfo()
{
    var handle = document.querySelector("input[name='handle']").value;
    var name = document.querySelector("input[name='username']").value;
    var description = document.querySelector("input[name='description']").value;
    var saveUserInfoURL = 'saveUserInfo';

    //TODO: Some error checking
    // create a Javascript object to send
    var userObject = {handle: handle, name: name, description: description};
    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', saveUserInfoURL, true );
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            hideOverlay('editUserInfo');
            getUserInfo();
        }
        else {
            var over = document.getElementById('editUserInfo');
            over.innerHTML += "<p>Unknown Error</p>";
            result.value = "Unknown ERROR";
        }
    };
    // convert to the offical JSON syntax
    var doc = JSON.stringify( userObject );
    xhr.send( doc );
}

function cancelUserInfo()
{
    hideOverlay('editUserInfo');
}

function showChangePassword()
{
    showOverlay("changePassword");
}

function saveChangePassword()
{
    var oldPassword = document.querySelector("input[name='oldPassword']").value;
    var newPassword = document.querySelector("input[name='newPassword']").value;
    var verifyPassword = document.querySelector("input[name='verifyPassword']").value;
    var saveChangePasswordURL = 'saveChangePassword';

    var over = document.getElementById('changePassword');
    if(newPassword != verifyPassword)
    {
        over.innerHTML += "Passwords don't match";
        return;
    }

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', saveChangePasswordURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            //over.innerHTML += xhr.responseText;
            hideOverlay('changePassword');
            hideOverlay('editUserInfo');
        }
        else {
            over.innerHTML += "Unknown ERROR";
        }
    };

    var doc = "oldPassword=" + encodeURI(oldPassword) + "&newPassword=" + encodeURI(newPassword);
    xhr.send( doc );
}

function cancelChangePassword()
{
    hideOverlay('changePassword');
    hideOverlay('editUserInfo');
}

function initializeMap(position) {
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    //TODO: Add error checking
    var myLatlng = new google.maps.LatLng(latitude, longitude);
    var mapOptions = {
    zoom: 14,
    center: myLatlng,
    scrollwheel: false
    }
    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    var marker = new google.maps.Marker({
      position: myLatlng,
      map: map,
      title: 'You Are Here!'
    });
}

function initializeGeolocation()
{
    navigator.geolocation.getCurrentPosition(initializeMap, mapError);
}

function mapError()
{
    document.getElementById('map-canvas').innerHTML ="<p>Error getting location</p>";
}

var userInfo;

function getUserInfo(evt)
{
    var getUserInfoUrl = window.location.origin + '/user/getUserInfo';
    var xhr = new XMLHttpRequest();
    xhr.open('GET', getUserInfoUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            userInfo = JSON.parse(xhr.responseText);
            //for user in users, create table row with mustache
            var rendered = Mustache.render(userInfoTemplate, userInfo);
            document.getElementById("userInfo").innerHTML = rendered;
            var rendered = Mustache.render(userInfoEditTemplate, userInfo);
            document.getElementById("editUserInfo").innerHTML = rendered;
            initializeGeolocation();
        }
        else {
            console.log("getUserInfo Failed");
        }
    };
    xhr.send();

    var userInfoTemplate = "<h2>My Account</h1>"
                         + "<p>Handle: {{handle}}</p>"
                         + "<p>Username: {{name}}</p>"
                         + "<p>Description: {{description}}</p>"
                         + "<button name='btnEditUserInfo' onclick='editUserInfo()''>Edit Info</button>";

    var userInfoEditTemplate = '<label>Handle: <input type="text" name="handle" value={{handle}}></label><br>'
                             + '<label>Username: <input type="text" name="username" value={{name}}></label><br>'
                             + '<label>Description: <input type="text" name="description" value={{description}}></label><br>'
                             + '<button name="btnSave" onclick="saveUserInfo()">Save</button>'
                             + '<button name="btnCancel" onclick="cancelUserInfo()">Cancel</button>'
                             + '<button name="btnChangePassword" onclick="showChangePassword()">Change Password</button>';
}

