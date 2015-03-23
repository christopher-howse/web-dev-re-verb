function addPost(text,user,time,post_id,favorited)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    post.setAttribute("onclick","toggleOverlay()");
    post.setAttribute("id","feed-post-"+post_id);

    post.innerHTML =    '<div class="feed-post-top">'
                    +   '<div class="feed-post-user">'+user+'</div>'
                    +   '<div class="feed-post-time">'+time+'</div>'
                    +   '</div>'
                    +   '<div class="feed-post-text">'+text+'</div>'
                    +   '<div class=post-buttons>'
                    +   '<button class="favorite-button postButton" onclick="favorite('+post_id+','+favorited+')">favorite</button>'
                    +   '<button onclick="toggleOverlay()" class="postButton">reply</button>'
                    +   '<button class="report-button postButton" onclick="report('+post_id+')">report</button>'
                    +   '</div>';
            
    feed.appendChild(post);
    var favoriteButton = document.querySelector('#feed-post-'+post_id+' .favorite-button');
    if(favorited)
    {
        favoriteButton.style.background = '#00695d';//TODO: Make it look good
    }
}

function addUpdatePost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    
    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div><div class=post-buttons><button class="common">reply</button><button class="common">favorite</button><button class="common">re:post</button></div>';
    
    feed.insertBefore(post,feed.childNodes[0])
    
}

function getMessages()
{
    if(document.querySelector("title") && document.querySelector("title").innerHTML == "My Account")
    {
        var getMessagesURL = "/getMessagesByUser";
    }
    else
    {
        var getMessagesURL = "/getMessagesByLocation";
    }
    var xhr = new XMLHttpRequest();
    xhr.open('GET', getMessagesURL, true );
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
        addPost(allPosts[i].postBody,allPosts[i].username,allPosts[i].timeStamp, allPosts[i].postId, allPosts[i].favorite);
    }
}


function toggleOverlay(){
	var overlay = document.getElementById('overlay');
	var specialBox = document.getElementById('overlay-back');
	if(overlay.style.display == "block"){
		overlay.style.display = "none";
		specialBox.style.display = "none";
	} else {
		overlay.style.display = "block";
		specialBox.style.display = "block";
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
    if(postButton)
    {
        postButton.disabled = false;
    }


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

function favorite(post_id, fav)
{
    var favoritePostURL = '/favoritePost';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', favoritePostURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Favorited Post");
            var favoriteButton = document.querySelector('#feed-post-'+post_id+' .favorite-button');
            if(fav)
            {
                favoriteButton.style.background = '';//TODO: Make it look good
            }
            else
            {
                favoriteButton.style.background = '#00695d';//TODO: Make it look good
            }
            favoriteButton.setAttribute('onclick', 'favorite('+post_id+','+!fav+')');
        }
        else {
            console.log("Unknown ERROR favorting post");
        }
    };
    var doc = "post_id=" + encodeURI(post_id) + "&favorite=" + encodeURI(fav);
    xhr.send( doc );

}

function report(post_id)
{
    var reportPostURL = '/reportPost';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', reportPostURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Reported Post");
        }
        else {
            console.log("Unknown ERROR reporting post");
        }
    };
    var doc = "post_id=" + encodeURI(post_id);
    xhr.send( doc );

}