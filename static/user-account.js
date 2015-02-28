function editUserInfo()
{
    var over = document.getElementById('editUserInfo');
    over.style.display = "block";
}

function saveUserInfo()
{
    var over = document.getElementById('editUserInfo');
    over.style.display = "none";
}

function cancelUserInfo()
{
    var over = document.getElementById('editUserInfo');
    over.style.display = "none";
}

function initializeMap(position) {
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    //TODO: Add error checking
    var myLatlng = new google.maps.LatLng(latitude, longitude);
    var mapOptions = {
    zoom: 7,
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

