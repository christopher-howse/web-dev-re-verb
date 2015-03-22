package users;

/**
 * Created by christopherhowse on 15-03-22.
 */
public class UserInfoDto
{
    public String newUsername;
    public String oldUsername;
    public String password;
    public String about_me;

    public UserInfoDto(String newUsername, String oldUsername, String password, String about_me)
    {
        this.newUsername = newUsername;
        this.oldUsername = oldUsername;
        this.password = password;
        this.about_me = about_me;
    }
}
