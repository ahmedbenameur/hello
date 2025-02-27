public class Output {
\/\/gen doc invitation proch comm
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.commons.util.LogUtil;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Collection;
import org.joget.apps.app.service.AppUtil;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import java.util.Map;
import org.joget.commons.util.LogUtil;
import java.lang.StringBuilder;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
\/\/--------date
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
\/\/-----------deate
WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean(\"workflowManager\");
private String lien = \"#envVariable.url_api_doc#\";
LogUtil.info(\"-----> ************** lien ***********************\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---Commission ------------->\", recordid);
      \/\/  String type_cnt = \"\";
 \/\/ function hitAPI
public void HitApi(String lien,JSONObject jsonData){
    try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
             LogUtil.info(\"---------------------------->7\", \"\");
            \/\/ Request setup
            connHTTP.setRequestMethod(\"POST\");
        connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
        connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            \/\/ The response from the server
            int status = connHTTP.getResponseCode();
              LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"CaseVioo\", e, \"Error in script Generation fiche cv  post CaseVioo Ouverture\");
    } finally {
            connHTTP.disconnect();
    }
}
\/\/function hit API
Connection con = null;
try {
        \/\/ retrieve connection from the default datasource
        DataSource ds = (DataSource)AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        \/\/ execute SQL query
        if(!con.isClosed()) {
                String idDossier=\"\";
                PreparedStatement stmtSelectFolders = con.prepareStatement(\"update app_fd_dossier_commission set c_isSaved='true' where c_id_commission=?;\");
                stmtSelectFolders.setString(1,\"#variable.processID#\");
                stmtSelectFolders.executeUpdate();
                \/\/ generate doc invitation
             String selectDocData2 = \"select distinct c.c_id_personne ,c.c_decision_id,c.c_decisionCommentaire,p.c_nom_id,p.c_prenom_id,p.c_numcartid_id,c.c_ComplimentDocument,c.id,c.c_infoOldCommission,commi.c_datecomm_id,c.id from app_fd_demande_carte c right join app_fd_dossier_commission dc on(c.id=dc.c_id_dossier) join app_fd_personne p on (p.id=c.c_id_personne) join app_fd_commission commi on (commi.id=c_id_commission)where c_id_commission=? ;\";
            LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData2);
            stmt2 = con.prepareStatement(selectDocData2);
            stmt2.setString(1, recordid);
            rs = stmt2.executeQuery();
            JSONArray DecisionArray = new JSONArray();
            while (rs.next()) {
                        if(rs.getString(\"c_decision_id\").equals(\"إستدعاء للحضور\")){
                            LogUtil.info(\"--------------INSIDE INV-------------->\", \"\");
                                String source_pathINV = \"#envVariable.pathSourceINV#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                                String output_pathINV = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                                String output_file_nameINV = rs.getString(\"c_numcartid_id\")+\"_Invitation_Commission.docx\";
                                String output_file_nameINV2 = rs.getString(\"c_numcartid_id\")+\"_Invitation_Commission.pdf\";
                                JSONObject jsonDataINV = new JSONObject();
                                jsonDataINV.put(\"source_path\", source_pathINV);
                                jsonDataINV.put(\"output_path\", output_pathINV);
                                jsonDataINV.put(\"output_file_name\", output_file_nameINV);
                                JSONObject dataINVF = new JSONObject();
                                \/\/parse object old commission and affect to new object
                                JSONObject oldComm=new JSONObject(rs.getString(\"c_infoOldCommission\"));
                                LogUtil.info(\"--------------parsed old-------------->\", \"\");
                                dataINVF.put(\"num_com\",oldComm.getString(\"num_com\"));
                                dataINVF.put(\"dateCom\",oldComm.getString(\"dateCom\"));
                                \/\/parse object old commission and affect to new object
                                \/\/get date ---------------
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\");
                                    DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
                                    LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"c_datecomm_id\"), dtf);
                                    DateTimeFormatter newTimeFormat = DateTimeFormatter.ofPattern(\"HH:mm\");
                                    String extractedTime = dateTime.toLocalTime().format(newTimeFormat);
                                    \/\/get date -----------
                                    String date_reunion = dateTime.toLocalDate().format(newDateFormat);
                                    String heure_reunion=extractedTime.toString();
                                \/\/ put data to object
                                dataINVF.put(\"dateComNew\",date_reunion);
                                dataINVF.put(\"heureComNew\",heure_reunion);
                                dataINVF.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                                jsonDataINV.put(\"data\",dataINVF);
                                LogUtil.warn(\"--------------API INV-------------->\",\"\" );
                                HitApi(lien,jsonDataINV);
                                LogUtil.warn(\"--------------API INV-------------->\",\"\" );
                                String UpdateFileInv = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                                stmtUpdateFileInvName = con.prepareStatement(UpdateFileInv);
                                stmtUpdateFileInvName.setString(1, output_file_nameINV2);
                                stmtUpdateFileInvName.setString(2,rs.getString(\"id\"));
                                stmtUpdateFileInvName.executeUpdate();
                        \/\/generation doc inv
                }
            }
        }
} catch(Exception e) {
        LogUtil.error(\"assignmentForceComplete \", e, \"error get DAO\");
} finally {
        try {
                if(con != null) {
                        con.close();
                }
        } catch(SQLException e) {\/* ignored *\/}
}


}