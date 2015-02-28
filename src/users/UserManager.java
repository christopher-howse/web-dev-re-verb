package users;

import java.util.ArrayList;

/**
 * Created by christopherhowse on 15-02-28.
 */
public class UserManager
{
    public ArrayList<User> getUsers()
    {
        ArrayList<User> result = new ArrayList<>();
        //TODO: Get users from database;
        //for now fake users
        result.add(new User("test_name", "test_password", "test_role"));
        result.add(new User("test_name", "test_password", "test_role"));
        result.add(new User("test_name", "test_password", "test_role"));
        return result;
    }
}
