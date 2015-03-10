function addPost(text,user,time)
{
    var feed = document.getElementById('main-feed');
    var post = document.createElement("div");
    post.setAttribute("class","feed-post");

    post.innerHTML = '<div class="feed-post-top"><div class="feed-post-user">'+user+'</div><div class="feed-post-time">'+time+'</div></div><div class="feed-post-text">'+text+'</div>';
            
    feed.appendChild(post);
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