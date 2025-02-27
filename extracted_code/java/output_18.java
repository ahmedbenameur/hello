public class Output {
\/\/ DDRASS
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
String recordid =\"#variable.processID#\";
Connection con = null;
Collection assignees = new ArrayList();
\/\/ assignees.add(\"#currentUser.username#\");
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
            \/\/ LogUtil.warn(\" --------------------> Adding participant to process-----------> \",\"\");
        if(!con.isClosed()) {
               \/\/ GET USER ROLES ---------------------------------
                 try{
                            PreparedStatement stmtSelectFolders = con.prepareStatement(\"select id,locale from jwdb.dir_user;\");
                        ResultSet rs1 = stmtSelectFolders.executeQuery();
                        while (rs1.next()) {
                                \/\/  LogUtil.warn(\" --------------------> selecting users for dir-----------> \",\"\");
                             if(rs1.getString(\"locale\")!=null && !rs1.getString(\"locale\").isEmpty()){
                                       JSONObject ob = new JSONObject(rs1.getString(\"locale\"));
                                    String gouv=ob.getString(\"gouv\");
                                    String del=ob.getString(\"del\");
                                    String sec=ob.getString(\"sec\");
                                    String role=ob.getString(\"list\");
                                    \/\/ LogUtil.warn(\" --------------------> parsed object -----------> \",\"\");
                                    if(gouv.equals(userGouv) && (role.contains(\"MAS-D-DRAS\") || role.contains(\"MAS-DPS-CHEF\")) ){
                                            \/\/ LogUtil.warn(\" --------------------> condition true -----------> \",\"\");
                                            assignees.add(rs1.getString(\"id\"));
                                            \/\/ LogUtil.info(\" --------------------> Added user-----------> \",rs1.getString(\"id\"));
                                    }
                             }
                            }
                 }catch(Exception x){
                         LogUtil.error(\" -------------------->errorr-----------> \",x,\"\");
                 }
                    try{
                                \/\/ GET USER ROLES ---------------------------------
                        PreparedStatement stms = con.prepareStatement(\"select c_nompres_id from jwdb.app_fd_commission where id=?;\");
                        stms.setString(1,recordid);
                        ResultSet rs2 = stms.executeQuery();
                        while (rs2.next()) {
                                    assignees.add(rs2.getString(\"c_nompres_id\"));
                                    \/\/ LogUtil.info(\" --------------------> Added user-----------> \",rs2.getString(\"c_nompres_id\"));
                            }
                    }catch(Exception x){
                             LogUtil.error(\" -------------------->errorr-----------> \",x,\"\");
                    }
                }
            \/\/GET PERSONE demands ------------------------------------------------------
        \/\/------------------------------------------------------
    } catch(Exception e) {
        LogUtil.warn(\"Setting users to process \",\"error\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}
return assignees;


}