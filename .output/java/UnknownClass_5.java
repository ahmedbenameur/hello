\/\/ WEB SERVICE CRES
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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS 1---Web SERVICE  CRS -------------\",\"\");
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
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
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.warn(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                String insertSql3 = \"UPDATE app_fd_personne SET c_prenommere_id=? , c_nommere_id=? ,c_prenomgrandpere_id=?,c_prenompere_id=?,c_nompere_id=? where id=?\";
                                if(\"1\".equals(\"#variable.statut_api#\")){
                                        insertLine(jsonRes,tableId,insertSql3);
                                }else{
                                    LogUtil.info(\"code RETOUR != 1**********************************************\", \"#variable.statut_api#\");
                                }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service cres\", e, \"Error\");
    } finally {
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        String Prenom_mere =res.getString(\"prenomMere\");
                        String Nom_mere=res.getString(\"nomMere\");
                        String prenom_gp=res.getString(\"prenomGP\");
                        String prenom_pere=res.getString(\"prenomPere\");
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,Prenom_mere);
                        stmtInsert3.setString(2,Nom_mere);
                        stmtInsert3.setString(3,prenom_gp);
                        stmtInsert3.setString(4,prenom_pere);
                        stmtInsert3.setString(5,NomPere);
                        stmtInsert3.setString(6,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"cres insert to db\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"cres insert to db\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.Process1WebServiceCRESSEndPoint#\";
LogUtil.info(\"------------PROCESS 1---Web SERVICE  2-------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
JSONObject jsonData = new JSONObject();
String dateStr=\"#variable.dateNaiss#\";
SimpleDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd\");
Date date = dateFormat.parse(dateStr);
Calendar calendar = Calendar.getInstance();
calendar.setTime(date);
int day = calendar.get(Calendar.DAY_OF_MONTH);
int month = calendar.get(Calendar.MONTH) + 1; \/\/ Month is 0-based
int year = calendar.get(Calendar.YEAR);
String strDay = String.valueOf(day);
String strMonth = String.valueOf(month);
String strYear = String.valueOf(year);
jsonData.put(\"jourNaiss\",strDay);
jsonData.put(\"moisNaiss\",strMonth);
jsonData.put(\"anneeNaiss\",strYear);
String sexe = (\"#variable.sexe#\"==\"\") ? \"1\" : \"2\";
jsonData.put(\"sexe\",sexe);
String NomPere=\"\";
String cin=\"\";
    DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
    Connection conSelect = ds.getConnection();
    try {
            if (!conSelect.isClosed()) {
                    String SelectQuery = \"select * from app_fd_personne where id=?\";
                    PreparedStatement stmtSelect = conSelect.prepareStatement(SelectQuery);
                    stmtSelect.setString(1,recordid);
                    LogUtil.info(\"QUERY  ----------------------------------------> \", SelectQuery);
                    ResultSet rs=stmtSelect.executeQuery();
                    while(rs.next()){
                            NomPere=rs.getString(\"c_nom_id\");
                            jsonData.put(\"nom\",rs.getString(\"c_nom_id\"));
                            jsonData.put(\"chainePere\",rs.getString(\"c_chainePere\"));
                            jsonData.put(\"chaineMere\",rs.getString(\"c_chaineMere\"));
                    }
            }
    } catch (Exception ex) {
            LogUtil.error(\"cres get from db\", ex, \"Error retrieving\");
    } finally {
            try {
                    if (conSelect != null) {
                            conSelect.close();
                    }
            } catch (Exception ex) {
                    LogUtil.error(\"cres get from db\", ex, \"Error closing the jdbc connection\");
            }
    }
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);

