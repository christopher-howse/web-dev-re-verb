function init()
{
    var navBar = document.querySelector('.reverb-navigation-bar');
    var headerBar = document.querySelector('.reverb-header-bar');
    var navBg = document.querySelector('.reverb-navigation-bg');

    getAdmin();
    
    var menuBtn = document.querySelector('.reverb-hamburger');
    menuBtn.addEventListener('click', function() 
    {
        var isOpen = navBar.classList.contains('open');
        if(isOpen) 
        {
            headerBar.classList.remove('open');
            navBar.classList.remove('open');
            navBg.classList.remove('open');
        } 
        else 
        {
            headerBar.classList.add('open');
            navBar.classList.add('open');
            navBg.classList.add('open');
        }
    }, true);
}


function getAdmin()
{
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if ( xhr.readyState == 4) {
            adminSelect( xhr );
        }
    }
    xhr.open( "GET", "/admin/getAdmin", true );
    xhr.send();
}

function adminSelect(xhr)
{
    var navAdmin = document.querySelector('.reverb-administrator');
    admin = xhr.responseText;
    console.log(admin);
    if (admin == "true")
    {
        navAdmin.style.display = "none";
    }
}