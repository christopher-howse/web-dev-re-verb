function init()
{
var navBar = document.querySelector('.reverb-navigation-bar');
      var headerBar = document.querySelector('.reverb-header-bar');
      var navBg = document.querySelector('.reverb-navigation-bg');

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

function init2()
{
var navdrawerContainer = document.querySelector('.navdrawer-container');
      var appbarElement = document.querySelector('.app-bar');
      var darkbgElement = document.querySelector('.navdrawer-bg');

      var menuBtn = document.querySelector('.menu');
      menuBtn.addEventListener('click', function() {
        var isOpen = navdrawerContainer.classList.contains('open');
        if(isOpen) {
          appbarElement.classList.remove('open');
          navdrawerContainer.classList.remove('open');
          darkbgElement.classList.remove('open');
        } else {
          appbarElement.classList.add('open');
          navdrawerContainer.classList.add('open');
          darkbgElement.classList.add('open');
        }
      }, true);
}

//        var menuBtn = document.querySelector('.reverb-hamburger');
//      menuBtn.addEventListener('click', function() {
//        sampleCompleted('appbar-navdrawer-bottombar-sample.html-menuclick');
//      }, true);
