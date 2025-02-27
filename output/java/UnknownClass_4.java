import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.joget.commons.util.UuidGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowProcessResult;
import org.joget.workflow.model.service.*;
import org.joget.directory.model.Department;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.workflow.model.service.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
Connection con = null;
\/\/initialize
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
            LogUtil.warn(\" --------------------> in process 1 id=-----------> \",recordid);
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                    PreparedStatement stmt = con.prepareStatement(\"UPDATE app_fd_personne SET c_finInscription='1' where id=?;\");
                    stmt.setString(1,recordid);
                    stmt.executeUpdate();
                }
    } catch(Exception e) {
        LogUtil.error(\"Error while trying to set tag fin inscription process 1\", e, \"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}

