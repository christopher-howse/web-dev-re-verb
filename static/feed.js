function addPost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");

    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div><div class=post-buttons><button class="favorite-button">favorite</button><button class="repost-button">re:post</button>';
            
    feed.appendChild(post);
}

function addUpdatePost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    
    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div>';
    
    feed.insertBefore(post,feed.childNodes[0])
    
}

function getMessages()
{
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "/getMessagesByLocation", true );
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            var posts = JSON.parse(xhr.responseText);
            populateFeed(posts);            
        }
        else {
            console.log("getting messages Failed");
        }
    };
    xhr.send();
}

function populateFeed(allPosts)
{
    var posts = [];
    console.log(allPosts);

    var x;
    for (i = 0; i < allPosts.length;i++)
    {
        addPost(allPosts[i].postBody,allPosts[i].username,allPosts[i].timeStamp);
    }
}

function initializeGeolocation()
{
    var postButton = document.querySelector(".posting-buttons button");
    postButton.disabled = true;

    var feed = document.getElementById('main-feed');
    feed.innerHTML = '<p>Getting location information!</p>';
    navigator.geolocation.getCurrentPosition(getUserPosition, geoError);
}

function getUserPosition(position)
{
    var postButton = document.querySelector(".posting-buttons button");
    postButton.disabled = false;

    var feed = document.getElementById('main-feed');
    feed.innerHTML = '';

    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;

    var userPositionURL = '/getUserPosition';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', userPositionURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Sent geolocation");
        }
        else {
            console.log("Unknown ERROR saving geolocation");
        }
    };

    var doc = "latitude=" + encodeURI(latitude) + "&longitude=" + encodeURI(longitude);
    xhr.send( doc );
    
    getMessages();
}

function geoError()
{
    console.log("Unknown ERROR saving geolocation");
}