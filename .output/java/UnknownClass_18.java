import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.directory.model.Department;
import org.joget.commons.util.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.joget.workflow.model.service.*;
import java.lang.Exception;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.*;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.json.JSONObject;
\/\/Get record Id from process
\/\/ AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
\/\/ String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
Connection con = null;
Collection assignees = new ArrayList();
assignees.add(\"#currentUser.username#\");
\/\/ get current user locale
String currentUserLocale=\"#currentUser.locale#\";
JSONObject ob = new JSONObject(currentUserLocale);
String userGouv=ob.getString(\"gouv\");
String userDel=ob.getString(\"del\");
String userSec=ob.getString(\"sec\");
\/\/initialize
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
            \/\/ LogUtil.info(\" --------------------> in process tasjil ID-----------> \",recordid);
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                    PreparedStatement stmtSelectFolders = con.prepareStatement(\"select id,locale from jwdb.dir_user where locale!=\"\";\");
                    ResultSet rs1 = stmtSelectFolders.executeQuery();
                    while (rs1.next()) {
                           JSONObject ob = new JSONObject(rs1.getString(\"locale\"));
                            String gouv=ob.getString(\"gouv\");
                            String del=ob.getString(\"del\");
                            String sec=ob.getString(\"sec\");
                            String role=ob.getString(\"list\");
                            if(del.equals(userDel) && role.contains(\"MAS-CHU\")){
                                    assignees.add(rs1.getString(\"id\"));
                            }
                        }
                }
            \/\/GET PERSONE demands ------------------------------------------------------
        \/\/------------------------------------------------------
    } catch(Exception e) {
        LogUtil.error(\"Serring users to process \", e, \"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}
return assignees;

