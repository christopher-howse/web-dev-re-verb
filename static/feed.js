function addPost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");

    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div>';
            
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
    var testPost = {user:"jacob", text:"I am the best",time:"12121212121"};
    posts[0] = testPost;
//    posts = allPosts;

    var x;
    for (i = 0; i < posts.length;i++)
    {
        addPost(posts[i].text,posts[i].user,posts[i].time);
    }
}