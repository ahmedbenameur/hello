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
LogUtil.info(\"-----> ************** in script generation list personne  ***********************\",\"\");
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
LogUtil.info(\"------------RecordID---demande carte ------------->\", recordid);
LogUtil.info(\"------------------------------------------------------>\",\"#variable.id_personne#\");
      \/\/  String type_cnt = \"\";
String source_path = \"MAS-IDMEJ-template_recu.docx\";\/\/ ---------------------------------------------------------------------------------------------------------------------------------
    String output_path = \"#envVariable.path_app_formuploads#\/demande_carte\/\"+recordid;\/\/------------------------------------------------------------------------------
String output_file_name = \"\";
String output_file_name2 = \"\";
JSONObject jsonData = new JSONObject();
jsonData.put(\"source_path\", source_path);
    String gouvernorat =\"\";
     String secteur_ul=\"\";
    JSONObject data = new JSONObject();
try {
        Connection con = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        con = ds.getConnection();
        LogUtil.info(\"---------------------------->1\", \"\");
    String selectQuery = \" select p.c_gouv_id, p.c_carte_id,p.c_identifiantsocial_id,p.c_datenaiss_id,p.c_adresse_id,p.c_premiernumtelephone_id,p.c_numcartid_id,p.c_secteur_id,p.c_prenom_id,p.c_nom_id,d.c_field40,d.c_field39,d.c_label_autre_doc_2,d.c_label_autre_doc_1,d.c_docmedical_id,d.c_docmedicavis_id,d.c_carnetdesoin_id,d.createdBy,d.dateCreated from app_fd_personne p join app_fd_demande_carte d on (p.id= d.c_id_personne) where d.c_id_personne =? ;\";
    PreparedStatement stmt = con.prepareStatement(selectQuery);
    stmt.setString(1, \"#variable.id_personne#\");
    ResultSet rs = stmt.executeQuery();
     \/\/ gouver
        ResultSet rs = stmt.executeQuery();
        LogUtil.info(\"------------selectQuery---------------->3\", selectQuery);
        if (rs.next()) {
                    LogUtil.info(\"--------------QUERY RESULT-------------->\", \"\");
                gouvernorat = rs.getString(\"c_gouv_id\");
                String num_identite = rs.getString(\"c_numcartid_id\");
                String  type_identite= rs.getString(\"c_carte_id\");
                String id_social = rs.getString(\"c_identifiantsocial_id\");
                String date_naissance = rs.getString(\"c_datenaiss_id\");
                String adresse_demandeur = rs.getString(\"c_adresse_id\");;
                String num_tel = rs.getString(\"c_premiernumtelephone_id\");
                 secteur_ul = rs.getString(\"c_secteur_id\");
                String prenom_demandeur = rs.getString(\"c_prenom_id\");
                String nom_demandeur = rs.getString(\"c_nom_id\");
                String charge_dossier = rs.getString(\"createdBy\");
        \/\/get date ---------------
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\");
        DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern(\"dd\/MM\/yyyy\");
        LocalDateTime dateTime = LocalDateTime.parse(rs.getString(\"dateCreated\"), dtf);
        \/\/get date -----------
                String date_demande = dateTime.toLocalDate().format(newDateFormat);;
        LogUtil.info(\"--------------QUERY 1 TXT gouv id -------------->\", gouvernorat);
               \/\/ data.put(\"gouvernorat\", gouvernorat);
                data.put(\"num_identite\", num_identite);
                data.put(\"type_identite\", type_identite);
                data.put(\"id_social\", id_social);
                data.put(\"date_naissance\", date_naissance);
                data.put(\"adresse_demandeur\", adresse_demandeur);
                data.put(\"num_tel\", num_tel);
                data.put(\"prenom_demandeur\", prenom_demandeur);
                data.put(\"nom_demandeur\", nom_demandeur);
                data.put(\"charge_dossier\", charge_dossier);
                data.put(\"date_demande\", date_demande);
                \/\/ ----------------------------------
                output_file_name = num_identite+\"MAS-IDMEJ-template_recu.docx\";
                output_file_name2 = num_identite+\"MAS-IDMEJ-template_recu.pdf\";
                JSONArray DocsArray = new JSONArray();
                JSONObject Docs = new JSONObject();
                \/\/projet.put(\"date_dem\", c_date_creation);
                if(rs.getString(\"c_carnetdesoin_id\")!=\"\" && rs.getString(\"c_carnetdesoin_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\", \"        \");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_docmedicavis_id\")!=\"\" && rs.getString(\"c_docmedicavis_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",\"  \");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_docmedical_id\")!=\"\" && rs.getString(\"c_docmedical_id\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",\"  \");
                           DocsArray.put(Docs);
                }
                if(rs.getString(\"c_label_autre_doc_1\")!=\"\" && rs.getString(\"c_label_autre_doc_1\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",rs.getString(\"c_label_autre_doc_1\"));
                           DocsArray.put(Docs);
                }
                 if(rs.getString(\"c_label_autre_doc_2\")!=\"\" && rs.getString(\"c_label_autre_doc_2\")!=null){
                          JSONObject Docs = new JSONObject();
                        Docs.put(\"name\",rs.getString(\"c_label_autre_doc_2\"));
                           DocsArray.put(Docs);
                }
               \/\/ DocsArray.put(Docs);
                data.put(\"list\", DocsArray);
                \/\/-----------------------------------
            data.put(\"date_demande\", date_demande);
    }
     \/\/select c_lib_secteur from app_fd_secteur;
    LogUtil.info(\"--------------QUERY gouv-------------->\", \"\");
    String selectDocData = \"select c_lib_gouv from app_fd_gouvernorat where id=? ;\";
    LogUtil.info(\"--------------QUERY gouv TXT-------------->\", selectDocData);
    LogUtil.info(\"--------------QUERY gouv TXT gouv id -------------->\", gouvernorat);
    stmt = con.prepareStatement(selectDocData);
    stmt.setString(1, gouvernorat);
    rs = stmt.executeQuery();
    while (rs.next()) {
         data.put(\"gouvernorat\", rs.getString(\"c_lib_gouv\"));
    }
    String selectDocData2 = \"select c_lib_secteur from app_fd_secteur where id=? ;\";
    LogUtil.info(\"--------------QUERY gouv TXT-------------->\", selectDocData2);
    LogUtil.info(\"--------------QUERY gouv TXT gouv id -------------->\", secteur_ul);
    stmt = con.prepareStatement(selectDocData2);
    stmt.setString(1, secteur_ul);
    rs = stmt.executeQuery();
    while (rs.next()) {
         data.put(\"secteur_ul\", rs.getString(\"c_lib_secteur\"));
    }
    LogUtil.info(\"------------data------------->\", data.toString());
      jsonData.put(\"data\", data);
    jsonData.put(\"output_path\", output_path);
    jsonData.put(\"output_file_name\", output_file_name);
        LogUtil.info(\"---------------------------->6\", jsonData.toString());
    \/\/LogUtil.info(\"---------------------------->documentData\", documentData.toString());
    \/\/update file name in field
    String UpdateFile = \"update app_fd_demande_carte set c_demandeFileDoc=? where id=? ;\";
    stmtUpdateFileName = con.prepareStatement(UpdateFile);
    stmtUpdateFileName.setString(1, output_file_name2);
    stmtUpdateFileName.setString(2, recordid);
    stmtUpdateFileName.executeUpdate();
        rs.close();
        stmt.close();
        con.close();
} catch (SQLException e) {
        LogUtil.error(\"genDoc\", e, \"Error executing SQL query\");
} catch (Exception ex) {
        LogUtil.error(\"genDoc\", ex, \"Error in script Generation genDoc\");
}
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
        LogUtil.error(\"genDoc\", e, \"genDoc\");
} finally {
        connHTTP.disconnect();
}

