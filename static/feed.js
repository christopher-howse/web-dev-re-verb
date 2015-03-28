var currentUser;

function addPost(text,user,time,post_id,favorited)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");
    post.setAttribute("onclick","toggleOverlay();replySetup("+post_id+")");
    post.setAttribute("id","feed-post-"+post_id);

    if(currentUser == user)
    {
        var reportButton = '<button class="delete-button postButton" onclick="deletePost('+post_id+')">delete</button>';
    }
    else
    {
        var reportButton = '<button class="report-button postButton" onclick="report('+post_id+')">report</button>';
    }

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

    post.innerHTML =    '<div class="feed-post-top">'
                    +   '<div class="feed-post-user">'+user+'</div>'
                    +   '<div class="feed-post-time">'+time+'</div>'
                    +   '</div>'
                    +   '<div class="feed-post-text">'+textbody+'</div>'
                    +   '<div class=post-buttons>'
                    +   '<button class="favorite-button postButton" onclick="favorite('+post_id+','+favorited+')">favorite</button>'
                    +   '<button onmousedown="toggleOverlay();replySetup('+post_id+')" class="postButton">reply</button>'
                    +   reportButton
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

function replySetup(id)
{
    var form = document.getElementById('reply');
    jQuery('.post-replies').html('');
    if(document.querySelector(".input-id"))
    {
        var input = document.querySelector(".input-id");
        input.setAttribute("value", id);
    }
    else
    {
        var input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("class","input-id");
        input.setAttribute("name", "id");
        input.setAttribute("value", id);
        //append to form element that you want .
        form.appendChild(input);
    }
    
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
    
    
    var replypost = document.querySelector(".reply-post");
    replypost.setAttribute("id","feed-post-"+post.postId);
    
    
    var buttons = document.querySelector('.post-buttons');
    var fav = document.querySelector('#reply-favorite');
    fav.setAttribute("class", "favorite-button postButton");
    fav.setAttribute("onclick",'favorite('+post.postId+','+post.favorite+')');

    if(replyUser.innerHTML == currentUser)
    {
        var deleteButton = document.querySelector('#reply-report');
        deleteButton.id = 'reply-delete';
        deleteButton.innerHTML = 'delete';
        deleteButton.setAttribute("class", "delete-button postButton");
        deleteButton.setAttribute("onclick",'deletePost('+post.postId+')');
    }
    else
    {
        var report = document.querySelector('#reply-report');
        report.setAttribute("class", "report-button postButton");
        report.setAttribute("onclick",'report('+post.postId+')');
    }

    if(!post.favorite)
    {
         fav.style.background = '';//TODO: Make it look good
    }
    else
    {
         fav.style.background = '#00695d';//TODO: Make it look good
    }
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
    
    if (allPosts.length > 2)
    {
        overlay.style.height = (2 * 135 + 275).toString() + "px";
        replyFeed.style.height = (2 * 135).toString() + "px";
        replyFeed.style.overflow = "scroll";
    }
    else
    {
        overlay.style.height = (allPosts.length * 135 + 275).toString() + "px";
        replyFeed.style.height = (allPosts.length * 135).toString() + "px";
        replyFeed.style.overflow = "visible";
    }
    
    var x;
    for (i = 0; i < allPosts.length;i++)
    {
        addReply(allPosts[i].postBody,allPosts[i].username,allPosts[i].timeStamp,allPosts[i].postId,allPosts[i].favorite);
    }
}

function addReply(text,user,time,id,favorited)
{
    var feed = document.querySelector('.post-replies');
    var post = document.createElement("div");
    post.setAttribute("class","reply-post-feed");
    post.setAttribute("id","feed-post-"+id);

    if(currentUser == user)
    {
        var reportButton = '<button class="delete-button postButton" onclick="deletePost('+id+')">delete</button>';
    }
    else
    {
        var reportButton = '<button class="report-button postButton" onclick="report('+id+')">report</button>';
    }
        
    post.innerHTML =    '<div class="feed-post-top">'
                    +   '<div class="feed-post-user">'+user+'</div>'
                    +   '<div class="feed-post-time">'+time+'</div>'
                    +   '</div><div class="feed-post-text">'+text+'</div>'
                    +   '<div class=post-buttons>'
                    +   '<button class="favorite-button postButton" onclick="favorite('+id+','+favorited+')">favorite</button>'
                    +   reportButton
                    +   '</div>';

    var favoriteButton = post.querySelector('.favorite-button');
    if(favorited)
    {
        favoriteButton.style.background = '#00695d';//TODO: Make it look good
    }

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
            currentUser = xhr.responseText;
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
    var e = window.event;
    e.cancelBubble = true;
    if (e.stopPropagation) e.stopPropagation();
    var favoritePostURL = '/favoritePost';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', favoritePostURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Favorited Post");
            var favoriteButton = document.querySelectorAll('#feed-post-'+post_id+' .favorite-button');
            if(fav)
            {
                for (i = 0; i < favoriteButton.length;i++)
                {                
                    favoriteButton[i].style.background = '';//TODO: Make it look good
                }
            }
            else
            {
                for (i = 0; i < favoriteButton.length;i++)
                {   
                    favoriteButton[i].style.background = '#00695d';//TODO: Make it look good
                }
            }
            for (i = 0; i < favoriteButton.length;i++)
            {
                favoriteButton[i].setAttribute('onclick', 'favorite('+post_id+','+!fav+')');
            }
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
    var e = window.event;
    e.cancelBubble = true;
    if (e.stopPropagation) e.stopPropagation();
    var reportPostURL = '/reportPost';

    // get an AJAX object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', reportPostURL, true );
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            console.log("Reported Post");
            showReportOverlay();
        }
        else {
            console.log("Unknown ERROR reporting post");
        }
    };
    var doc = "post_id=" + encodeURI(post_id);
    xhr.send( doc );

}

function hideReportOverlay()
{
    var over = document.querySelector("#report-overlay");
    if(over)
    {
        console.log("Hide report");
        over.style.display = "none";
    }
}

function showReportOverlay()
{
    var over = document.querySelector("#report-overlay");
    if(over)
    {
        console.log("Show report");
        over.style.display = "block";
    }
}

function deletePost(postId)
{
    var e = window.event;
    e.cancelBubble = true;
    if (e.stopPropagation) e.stopPropagation();

    var postIdDto = {id : postId};
    var deletePostUrl = '/deletePost';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', deletePostUrl, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() {
        if ( xhr.readyState != 4) return;
        if ( xhr.status == 200 || xhr.status == 400) {
            location.reload();
        }
        else {
            console.log("deletePost Failed");
        }
    };
    var doc = "post_id=" + encodeURI(postId);
    xhr.send(doc);
}