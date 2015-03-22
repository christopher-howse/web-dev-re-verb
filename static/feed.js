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

function populateFeed()
{
    var posts = [];
    var testPost = {user:"jacob", text:"I am the best",time:"12121212121"};
    posts[0] = testPost;

    var x;
    for (i = 0; i < posts.length;i++)
    {
        addPost(posts[i].text,posts[i].user,posts[i].time);
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

    populateFeed();

    var userPositionURL = '/getUserPosition';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', userPositionURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Sent geolocation");
            getMessages();
        }
        else {
            console.log("Unknown ERROR saving geolocation");
        }
    };

    var doc = "latitude=" + encodeURI(latitude) + "&longitude=" + encodeURI(longitude);
    xhr.send( doc );
}

function geoError()
{
    console.log("Unknown ERROR saving geolocation");
}