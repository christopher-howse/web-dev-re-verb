function toggleSignUp()
{
	var login = document.getElementById('login-front');
	var signup = document.getElementById('signup-front');
	if(login.style.display == "block")
    {
		login.style.display = "none";
		signup.style.display = "block";
	} 
    else 
    {
		login.style.display = "block";
		signup.style.display = "none";
	}
}

function setPortal(start)
{
	var login = document.getElementById('login-front');
	var signup = document.getElementById('signup-front');    
    if(start=="signup")
    {
		login.style.display = "none";
		signup.style.display = "block";  
    }
    else
    {
		login.style.display = "block";
		signup.style.display = "none";        
    }
}

function setError(error,init)
{
    var errorLogin = document.getElementById('login-error');
    var errorSignup = document.getElementById('signup-error');
    if(error != "undefined")
    {
        if(init=="signup")
        {
            errorSignup.innerHTML = error;
        }
        else
        {
            errorLogin.innerHTML = error;
        }
    }
}

function initPortal()
{
    var params = getParams();
    init = unescape(params["init"]);
    error = unescape(params["error"]);
    if (init)
    {
        setPortal(init);
        setError(error,init);
    }
}

function getParams() {

    var params = {},
        pairs = document.URL.split('?')
               .pop()
               .split('&');

    for (var i = 0, p; i < pairs.length; i++) {
           p = pairs[i].split('=');
           params[ p[0] ] =  p[1];
    }     

    return params;
}