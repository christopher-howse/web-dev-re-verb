function addPost(text,user,time, id)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    post.setAttribute("onclick","toggleOverlay();replySetup("+id+")");
    
    var textbody;
    if(typeof text == 'string' || text instanceof String)
    {
        if (text.length > 60)
        {
            textbody = text.substr(1,60);
            textbody = textbody.concat("...");
        }
        else
        {
            textbody = text
        }
    }
    else
    {
        textbody = text
    }
    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+textbody+'</div><div class=post-buttons><button onmousedown="toggleOverlay();replySetup('+id+')"  class="common">reply</button><button class="common">favorite</button><button class="common">re:post</button></div>';
            
    feed.appendChild(post);
}

function addUpdatePost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    
    
    var textbody;
    if(typeof text == 'string' || text instanceof String)
    {
        if (text.length > 60)
        {
            textbody = text.substr(1,60);
            textbody = textbody.concat("...");
        }
        else
        {
            textbody = text
        }
    }
    else
    {
        textbody = text
    }
    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+textbody+'</div><div class=post-buttons><button onclick="toggleOverlay()" class="common">reply</button><button class="common">favorite</button><button class="common">re:post</button></div>';
    
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
    console.log(allPosts);

    var x;
    for (i = 0; i < allPosts.length;i++)
    {
        addPost(allPosts[i].postBody,allPosts[i].username,allPosts[i].timeStamp,allPosts[i].postId);
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

function replySetup(id)
{
    var form = document.getElementById('reply');
    var input = document.createElement("input");
    jQuery('.post-replies').html('');
    input.setAttribute("type", "hidden");
    input.setAttribute("name", "id");
    input.setAttribute("value", id);
    //append to form element that you want .
    form.appendChild(input);
    
    getReplyPost(id);
    getReplies(id);
}

function getReplyPost(id)
{
    var idObj = {id:id};
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "/getMessage", true );
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            var post = JSON.parse(xhr.responseText);
            addReplyPost(post);            
        }
        else {
            console.log("getting messages Failed");
        }
    };
    var doc = JSON.stringify( idObj );
    xhr.send( doc );
}

function addReplyPost(post)
{
    console.log(post);
    var replyText = document.querySelector(".reply-post-text");
    var replyUser = document.querySelector(".reply-post-user");
    var replyTime = document.querySelector(".reply-post-time");
    
    replyText.innerHTML = post.postBody;
    replyUser.innerHTML = post.username;
    replyTime.innerHTML = post.timeStamp;
    
}

function getReplies(id)
{
        var idObj = {id:id};
    var xhr = new XMLHttpRequest();
    xhr.open('POST', "/getReplies", true );
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            var post = JSON.parse(xhr.responseText);
            populateReplies(post);            
        }
        else {
            console.log("getting messages Failed");
        }
    };
    var doc = JSON.stringify( idObj );
    xhr.send( doc );
}

function populateReplies(allPosts)
{
    console.log(allPosts);

    var overlay = document.querySelector('.overlay');
    var replyFeed = document.querySelector('.post-replies');
    
    if (allPosts.length > 4)
    {
        overlay.style.height = (4 * 135 + 275).toString() + "px";
        replyFeed.style.height = (4 * 135).toString() + "px";
        replyFeed.style.overflow = "scroll";
    }
    else
    {
        overlay.style.height = (allPosts.length * 135 + 275).toString() + "px";
    }
    
    var x;
    for (i = 0; i < allPosts.length;i++)
    {
        addReply(allPosts[i].postBody,allPosts[i].username,allPosts[i].timeStamp,allPosts[i].postId);
    }
}

function addReply(text,user,time, id)
{
    var feed = document.querySelector('.post-replies');
    var post = document.createElement("div");
    post.setAttribute("class","reply-post-feed");
        
    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div><div class=post-buttons><button class="common">reply</button><button class="common">favorite</button><button class="common">re:post</button></div>';
            
    feed.appendChild(post);

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
            getMessages();
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