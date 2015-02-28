import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by christopherhowse on 15-02-28.
 */
public class JsonTransformer implements ResponseTransformer
{
    private Gson gson = new Gson();

    @Override
    public String render(Object o) throws Exception
    {
        return gson.toJson(o);
    }
}
