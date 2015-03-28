function showOverlay(overlay)
{
    document.getElementById('overlay-edit-back').style.display = "block";
    var over = document.getElementById(overlay);
    over.style.display = "block";
}

function hideOverlay(overlay)
{
    document.getElementById('overlay-edit-back').style.display = "none";
    var over = document.getElementById(overlay);
    over.style.display = "none";
}

function editUserInfo()
{
    showOverlay('editUserInfo');
}

function saveUserInfo()
{
    var name = document.querySelector("input[name='username']").value;
    var description = document.querySelector("input[name='description']").value;
    var saveUserInfoURL = '/auth/saveUserInfo';

    //TODO: Some error checking
    // create a Javascript object to send
    var userObject = {name: name, description: description};
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
    var saveChangePasswordURL = '/auth/saveChangePassword';

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
    getUserPosition(position);
}

function initializeUserGeolocation()
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
    var getUserInfoUrl = '/auth/getUserInfo';
    var xhr = new XMLHttpRequest();
    xhr.open('GET', getUserInfoUrl, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            userInfo = JSON.parse(xhr.responseText);


            var rendered = Mustache.render(userInfoTemplate, userInfo);
            document.getElementById("userInfo").innerHTML = rendered;
            var rendered = Mustache.render(userInfoEditTemplate, userInfo);
            document.getElementById("editUserInfo").innerHTML = rendered;
            initializeUserGeolocation();
        }
        else {
            console.log("getUserInfo Failed");
        }
    };
    xhr.send();

    var userInfoTemplate = "<h2>My Account</h1>"
                         + "<p>Username: {{name}}</p>"
                         + "<p>About me: {{description}}</p>"
                         + "<button class='raisedButton' name='btnEditUserInfo' onclick='editUserInfo()''>Edit Info</button>";

    var userInfoEditTemplate = '<div class="insideOverlay">'
                             + '<label>Username: <input type="text" name="username" value="{{name}}"></label>'
                             + '<label>Description: <input type="text" name="description" value="{{description}}"></label>'
                             + '<div class="buttons">'
                             + '<button class="flatButton" name="btnSave" onclick="saveUserInfo()">Save</button>'
                             + '<button class="flatButton" name="btnCancel" onclick="cancelUserInfo()">Cancel</button>'
                             + '<button class="flatButton" name="btnChangePassword" onclick="showChangePassword()">Change Password</button></div></div>';
}

