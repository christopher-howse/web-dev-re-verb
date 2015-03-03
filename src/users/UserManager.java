package users;

import utilityDtos.BooleanDto;
import java.util.ArrayList;

public class UserManager
{
    private static ArrayList<User> testUsers = new ArrayList<User>();

    public UserManager()
    {
        testUsers.add(new User("test_name", "test_password", "admin"));
        testUsers.add(new User("test_name2", "test_password", "pleb"));
        testUsers.add(new User("test_name3", "test_password", "pleb"));
    }

    public static ArrayList<User> getUsers()
    {
        //TODO: Get users from database;
        //for now fake users
        return testUsers;
    }

    public static BooleanDto toggleUserRole(String username)
    {
        //TODO: Switch the role of the user in the database
        //for now fake toggle of user
        BooleanDto booleanDto = new BooleanDto(false);
        User tempUser = new User(username, "", "");
        for(User user : testUsers)
        {
            if(user.compareTo(tempUser) == 0)
            {
                user.toggleRole();
                booleanDto.success = true;
                return booleanDto;
            }
        }
        return booleanDto;
    }
    static public User getUser(String userName)
    {
        return new User("test_name", "test_password", "test_role");
    }
    static public User createUser(String userName, String password)
    {
        User newUser = new User(userName, password, "user");
        //TODO: add new user to database;
        return newUser;
    }
}
