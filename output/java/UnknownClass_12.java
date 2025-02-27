\/\/doc pv commission idmaj
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
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.model.FormRow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Calendar;
import java.sql.*;
\/\/--------date
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
\/\/-----------deate
Calendar calendar = Calendar.getInstance();
int currentYear = calendar.get(Calendar.YEAR);
int nextYear = currentYear + 1;
String currentYearString = String.valueOf(currentYear);
String nextYearString = String.valueOf(nextYear);
\/\/ Now you can use the 'currentYearString' and 'nextYearString' variables
LogUtil.info(\"-----> ************** in script generation PV commission ***********************\",\"\");
\/* Post JSON *\/
\/\/ id ahmed = 824e3a42-3ab2-4e57-8726-ada87668528b
\/\/ id doc = 3a51fba5-9f18-45a3-850d-06f2c3190efa
\/\/ Environnement developpemnt
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
String source_path = \"#envVariable.pathSourceCommission#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
String output_path = \"#envVariable.path_app_formuploads#\";\/\/------------------------------------------------------------------------------
String output_file_name = \"\";
String output_file_name2 = \"\";
String commission_num=\"\";
String date_reunion=\"\";
String heure_reunion=\"\";
JSONObject jsonData = new JSONObject();
jsonData.put(\"source_path\", source_path);
JSONObject data = new JSONObject();
try {
        Connection con = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        LogUtil.info(\"---------------------------->1\", \"\");
    String selectQuery = \" select c_numcomm,c_datecomm_id,c_nompres_id from app_fd_commission where  id=? ;\";
    PreparedStatement stmt = con.prepareStatement(selectQuery);
    stmt.setString(1, recordid);
    ResultSet rs = stmt.executeQuery();
     \/\/ gouver
        ResultSet rs = stmt.executeQuery();
        LogUtil.info(\"------------selectQuery---------------->3\", selectQuery);
        if (rs.next()) {
                    LogUtil.info(\"--------------QUERY RESULT-------------->\", \"\");
                commission_num  = rs.getString(\"c_numcomm\");
                String commission_president = rs.getString(\"c_nompres_id\");
                \/\/get date ---------------
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\");
                DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
                LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"c_datecomm_id\"), dtf);
                DateTimeFormatter newTimeFormat = DateTimeFormatter.ofPattern(\"HH:mm\");
                String extractedTime = dateTime.toLocalTime().format(newTimeFormat);
                \/\/get date -----------
                date_reunion = dateTime.toLocalDate().format(newDateFormat);
                heure_reunion=extractedTime.toString();
        \/\/ put data to object
                data.put(\"commission_num\", commission_num);
                data.put(\"commission_president\", commission_president);
                data.put(\"date_reunion\", date_reunion);
                data.put(\"temp_reunion\", heure_reunion);
                \/\/ ----------------------------------
                output_file_name = commission_num +\"MAS-IDMEJ-template_PVcommission.docx\";
                output_file_name2 = commission_num +\"MAS-IDMEJ-template_PVcommission.pdf\";
    }
    LogUtil.info(\"--------------QUERY membre-------------->\", \"\");
    String selectDocData = \"select c_presence_id,c_nommembre_id,c_premembre_id,c_fonction_id from app_fd_membre where c_id_commission=? ;\";
    LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData);
    stmt = con.prepareStatement(selectDocData);
    stmt.setString(1, recordid);
    rs = stmt.executeQuery();
    JSONArray MembreArray = new JSONArray();
    while (rs.next()) {
                  JSONObject membre = new JSONObject();
                    membre.put(\"nom\",rs.getString(\"c_premembre_id\")+\" \"+rs.getString(\"c_nommembre_id\"));
                    membre.put(\"fonction\",rs.getString(\"c_presence_id\"));
                    membre.put(\"fn\",rs.getString(\"c_fonction_id\"));
                    MembreArray.put(membre);
            }
    data.put(\"list\", MembreArray);
    \/\/ query list
    LogUtil.info(\"--------------QUERY membre-------------->\", \"\");
    String selectDocData2 = \"select distinct c.c_id_personne ,c.c_decision_id,c.c_decisionCommentaire,p.c_nom_id,p.c_prenom_id,p.c_numcartid_id,c.c_ComplimentDocument,c.id,c.c_autreDoc from app_fd_demande_carte c right join app_fd_dossier_commission dc on(c.id=dc.c_id_dossier) join app_fd_personne p on (p.id=c.c_id_personne) where c_id_commission=? ;\";
    LogUtil.info(\"--------------QUERY membre TXT-------------->\", selectDocData2);
    stmt2 = con.prepareStatement(selectDocData2);
    stmt2.setString(1, recordid);
    rs = stmt2.executeQuery();
    JSONArray DecisionArray = new JSONArray();
    while (rs.next()) {
                  JSONObject decision = new JSONObject();
                    decision.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                    decision.put(\"cin\",rs.getString(\"c_numcartid_id\"));
                    decision.put(\"decision\",rs.getString(\"c_decision_id\"));
                    decision.put(\"comm\",rs.getString(\"c_decisionCommentaire\"));
                    DecisionArray.put(decision);
                    if(rs.getString(\"c_decision_id\").equals(\" \")){
                            String source_pathRefus = \"#envVariable.pathSourceRefus#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathRefus = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameRefus = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameRefus2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.PDF\";
                            JSONObject jsonDataRefus = new JSONObject();
                            jsonDataRefus.put(\"source_path\", source_pathRefus);
                            jsonDataRefus.put(\"output_path\", output_pathRefus);
                            jsonDataRefus.put(\"output_file_name\", output_file_nameRefus);
                            JSONObject dataRefus = new JSONObject();
                            dataRefus.put(\"num_com\",commission_num);
                            dataRefus.put(\"dateCom\",date_reunion);
                            dataRefus.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            jsonDataRefus.put(\"data\",dataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            HitApi(lien,jsonDataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            String UpdateFileRefus = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFileRefusName = con.prepareStatement(UpdateFileRefus);
                            stmtUpdateFileRefusName.setString(1, output_file_nameRefus2);
                            stmtUpdateFileRefusName.setString(2,rs.getString(\"id\"));
                            stmtUpdateFileRefusName.executeUpdate();
                    }else  if(rs.getString(\"c_decision_id\").equals(\" \")){
                            String source_pathRefus = \"#envVariable.pathSourceAcc#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathRefus = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameRefus = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameRefus2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.pdf\";
                            JSONObject jsonDataRefus = new JSONObject();
                            jsonDataRefus.put(\"source_path\", source_pathRefus);
                            jsonDataRefus.put(\"output_path\", output_pathRefus);
                            jsonDataRefus.put(\"output_file_name\", output_file_nameRefus);
                            JSONObject dataRefus = new JSONObject();
                            dataRefus.put(\"num_com\",commission_num);
                            dataRefus.put(\"dateCom\",date_reunion);
                            dataRefus.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            jsonDataRefus.put(\"data\",dataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            HitApi(lien,jsonDataRefus);
                            LogUtil.warn(\"--------------API REFUS-------------->\",\"\" );
                            String UpdateFileRefus = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFileRefusName = con.prepareStatement(UpdateFileRefus);
                            stmtUpdateFileRefusName.setString(1, output_file_nameRefus2);
                            stmtUpdateFileRefusName.setString(2,rs.getString(\"id\"));
                            stmtUpdateFileRefusName.executeUpdate();
                    }
                    else  if(rs.getString(\"c_decision_id\").equals(\" \")){
                            String source_pathDoc = \"#envVariable.pathSourceDocument#\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
                            String output_pathDoc =  \"#envVariable.path_app_formuploads#\/demande_carte\/\"+rs.getString(\"id\");\/\/------------------------------------------------------------------------------
                            String output_file_nameDoc = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.docx\";
                            String output_file_nameDoc2 = rs.getString(\"c_numcartid_id\")+\"_DecisionCommissionPV.pdf\";
                            JSONObject jsonDataDocs = new JSONObject();
                            jsonDataDocs.put(\"source_path\", source_pathDoc);
                            jsonDataDocs.put(\"output_path\", output_pathDoc);
                            jsonDataDocs.put(\"output_file_name\", output_file_nameDoc);
                            JSONObject dataDocs = new JSONObject();
                            dataDocs.put(\"num_com\",commission_num);
                            dataDocs.put(\"dateCom\",date_reunion);
                            dataDocs.put(\"nom\",rs.getString(\"c_prenom_id\")+\" \"+rs.getString(\"c_nom_id\"));
                            LogUtil.warn(\"--------------API COMP 1-------------->\",\"\" );
                            String[] rows = rs.getString(\"c_ComplimentDocument\").split(\";\");
                              LogUtil.warn(\"--------------API COMP 2-------------->\",rs.getString(\"c_ComplimentDocument\") );
                            JSONArray DocsArray = new JSONArray();
                            for (int i = 0; i < rows.length; i++) {
                                    if(rows[i]!=\" \"){
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put(\"name\", rows[i]);
                                        DocsArray.put(jsonObject);
                                    }
                        }
                            LogUtil.warn(\"--------------API COMP 3-------------->\",\"\" );
                        if(rs.getString(\"c_autreDoc\")!=\"\" && rs.getString(\"c_autreDoc\")!=null){
                                     JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(\"name\", rs.getString(\"c_autreDoc\"));
                                    DocsArray.put(jsonObject);
                        }
                            dataDocs.put(\"list\",DocsArray);
                        LogUtil.warn(\"--------------API COMP 4-------------->\", dataDocs.toString());
                        LogUtil.warn(\"--------------API COMP 5-------------->\", jsonDataDocs.toString());
                            jsonDataDocs.put(\"data\",dataDocs);
                            LogUtil.warn(\"--------------API Doc-------------->\",\"\" );
                            HitApi(lien,jsonDataDocs);
                            LogUtil.warn(\"--------------API Doc-------------->\",\"\" );
                              String UpdateFilEDoc = \"update app_fd_demande_carte set c_filePVDecisionCommission=? where id=? ;\";
                            stmtUpdateFilEDocName = con.prepareStatement(UpdateFilEDoc);
                            stmtUpdateFilEDocName.setString(1, output_file_nameDoc2);
                            stmtUpdateFilEDocName.setString(2, rs.getString(\"id\"));
                            stmtUpdateFilEDocName.executeUpdate();
                    }else  if(rs.getString(\"c_decision_id\").equals(\" \")){
                            LogUtil.warn(\"--------------INV-------------->\",\"\" );
                            JSONObject jsonDataDocs = new JSONObject();
                            jsonDataDocs.put(\"num_com\",commission_num);
                            jsonDataDocs.put(\"dateCom\",date_reunion);
                            String query=\"update app_fd_demande_carte set c_infoOldCommission=? where id=?\";
                            LogUtil.info(\"--------------QUERY update demande info old commission-------------->\", \"\");
                            stmtUp = con.prepareStatement(query);
                            stmtUp.setString(1, jsonDataDocs.toString());
                            stmtUp.setString(2, rs.getString(\"id\"));
                            stmtUp.executeUpdate();
                            LogUtil.info(\"--------------QUERY update demande info old commission DONE -------------->\", \"\");
                    }
            }
    data.put(\"listDemande\", DecisionArray);
    LogUtil.info(\"------------data------------->\", data.toString());
    jsonData.put(\"data\", data);
    jsonData.put(\"output_path\", output_path+\"\/commission\/\"+recordid);
    jsonData.put(\"output_file_name\", output_file_name);
    LogUtil.info(\"---------------------------->6\", jsonData.toString());
    \/\/UPDATE FILE COMMISSON
    \/\/ commissionFileDocRecordID---Commission
    String UpdateFile = \"update app_fd_commission set c_commissionFileDoc=? where id=? ;\";
    stmtUpdateFileName = con.prepareStatement(UpdateFile);
    stmtUpdateFileName.setString(1, output_file_name2);
    stmtUpdateFileName.setString(2, recordid);
    stmtUpdateFileName.executeUpdate();
    \/\/UPDATE FILE COMMISSON
    HitApi(lien,jsonData);
        rs.close();
        stmt.close();
        con.close();
} catch (SQLException e) {
        LogUtil.error(\"CaseVioo\", e, \"Error executing SQL query\");
} catch (Exception ex) {
        LogUtil.error(\"CaseVioo\", ex, \"Error in script Generation fiche cv post CaseVioo Ouverture\");
}

