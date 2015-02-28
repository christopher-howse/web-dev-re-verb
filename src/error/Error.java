package error;

/**
 * Created by Jake on 28/02/2015.
 */
public class Error
{
    //Error page that appears for most errors
    private static final String ERROR_PAGE =
            "<html>" +
                    "<head><title>Error Page</title></head>" +
                    "<body> <h1>Error</h1> %s </body>" +
                    "</html>";

    // Error page call
    public static String errorPage( String msg ) {
        return String.format( ERROR_PAGE, msg );
    }
}
