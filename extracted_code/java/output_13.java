public class Output {
{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"import org.joget.workflow.util.WorkflowUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public String getEncodedMesage(String message){
        String input = message;
        try {
                String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
                encoded = encoded.replace(\"+\", \"%20\");
                return encoded;
        } catch (UnsupportedEncodingException e) {
                return \"Error\";
                LogUtil.error(\"Error in ANNULER_UPDATE_RENOUVELLEMENT ices\",e,\"Cannot get encoded message\");
        }
}
HttpServletResponse response = WorkflowUtil.getHttpServletResponse();
    response.sendRedirect(\"\/jw\/web\/userview\/#envVariable.appId#\/#envVariable.userViewId#\/_\/#form.commission.pageUrl#?flash=\"+getEncodedMesage(\"#form.commission.flashParam#\"));
"}}],"runInMultiThread":"","comment":"


}