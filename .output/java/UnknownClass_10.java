{"tools":[{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"\/\/ WEB SERVICE cnam
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
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
LogUtil.warn(\"------------PROCESS 2 ---Web SERVICE  CNAM  -------------\",\"\");
public void hitApi(JSONObject jsonData ,String lien ,String tableId,String token){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            \/\/ Request setup
            connHTTP.setRequestMethod(\"GET\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            connHTTP.setRequestProperty(\"Authorization\", \"Bearer \" + token);
            connHTTP.setDoOutput(true);
            \/\/ OutputStream os = connHTTP.getOutputStream();
            \/\/ byte[] input = jsonData.toString().getBytes(\"utf-8\");
            \/\/ os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            \/\/ LogUtil.info(\"----->  json data  \", jsonData.toString());
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                            LogUtil.warn(\"response ----------------------------------------> \", line);
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                LogUtil.info(\"insert line**********************************************\", jsonRes.toString());
                                JSONArray itemsArray = jsonRes.getJSONArray(\"items\");
                                JSONObject item = itemsArray.getJSONObject(0);
                                String libCaisseValue = item.getString(\"lib_caisse\");
                                LogUtil.warn(\"insert line**********************************************\", libCaisseValue);
                                String insertSql3 = \"UPDATE app_fd_demande_carte SET c_couv_id=? where id=?\";
                                insertLine(jsonRes,tableId,insertSql3,libCaisseValue);
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service 1\", e, \"ERROR\");
    } finally {
            connHTTP.disconnect();
    }
}
public String hitApiToken2(String lien){
        String token=\"\";
        try {
                URL url = new URL(lien);
                connHTTP = (HttpURLConnection) url.openConnection();
                \/\/ Request setup
                connHTTP.setRequestMethod(\"POST\");
                connHTTP.setRequestProperty(\"Content-Type\", \"application\/x-www-form-urlencoded\");
                connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
                connHTTP.setDoOutput(true);
                connHTTP.setDoInput(true);
                String username = \"#envVariable.loginCnam#\";
                String password = \"#envVariable.passCnam#\";
                \/\/ LogUtil.warn(\"username**********************************************\", username);
                \/\/ LogUtil.warn(\"password**********************************************\", password);
                String auth = username + \":\" + password;
                String authHeaderValue = \"Basic \" + Base64.getEncoder().encodeToString(auth.getBytes());
                connHTTP.setRequestProperty(\"Authorization\", authHeaderValue);
                String postData = \"grant_type=client_credentials\";
                try{
                        DataOutputStream wr = new DataOutputStream(connHTTP.getOutputStream());
                        wr.writeBytes(postData);
                        wr.flush();
                }catch(Exception x){
                        LogUtil.error(\"---------->\",x,\"error\");
                }
            int status = connHTTP.getResponseCode();
            LogUtil.info(\"response code \", String.valueOf(status));
            if (status >= 300) {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                            return token;
                    }
                    reader.close();
            } else {
                    reader = new BufferedReader(new InputStreamReader(connHTTP.getInputStream(), StandardCharsets.UTF_8));
                    while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                JSONObject jsonRes = new JSONObject(line);
                                token=jsonRes.getString(\"access_token\");
                                return token;
                    }
                    reader.close();
            }
    } catch (Exception e) {
            return token;
            LogUtil.error(\"web service 1\", e, \"ERROR\");
    } finally {
            return token;
            connHTTP.disconnect();
    }
}
public void insertLine(JSONObject res,String tableId, String insertSql3,String value){
        LogUtil.info(\"insert line ----------------------------------------> \", res.toString());
        LogUtil.info(\"insert line table ----------------------------------------> \", tableId);
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        try {
                if (!con3.isClosed()) {
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,value);
                        stmtInsert3.setString(2,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"Dictionnaire signalement\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"Dictionnaire signalement\", ex, \"Error closing the jdbc connection\");
                }
        }
}
public String getValue(){
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        String id=\"\";
        try {
                if (!con3.isClosed()) {
                        String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
                        PreparedStatement stmtSelect = con3.prepareStatement(qrIdSoc);
                        String id_personne=\"#variable.id_personne#\";
                        LogUtil.warn(\"id personne  ----------------------------------------> \", id_personne);
                        stmtSelect.setString(1,id_personne);
                        LogUtil.warn(\"QUERY  ----------------------------------------> \", stmtSelect.toString());
                        ResultSet rs = stmtSelect.executeQuery();
                        while (rs.next()){
                                LogUtil.warn(\"idsocial  ----------------------------------------> \", rs.getString(1));
                                id=rs.getString(1);
                                return id;
                        }
                }
        } catch (Exception ex) {
                LogUtil.error(\"web service cnam\", ex, \"Error storing using jdbc\");
                return id;
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                                return id;
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"web service cnam\", ex, \"Error closing the jdbc connection\");
                        return id;
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lienToken = \"#envVariable.TokenCNAM#\";
\/\/ private String lien = \"https:\/\/apigw.cni.tn\/reg\/get\/bc\/bycindn\/1.0.0\/getBCIdSocDdByCinDn\";
\/\/+++++++++++++++++++++++++++
\/\/ LogUtil.info(\"------------PROCESS 2 CNAM--------------LINK -------------\",lien);
\/\/ LogUtil.info(\"------------PROCESS 2 CNAM- TOKEN -------------LINK -------------\",lienToken);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
\/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
\/\/ LogUtil.info(\"------------RecordID---------------->\", recordid);
\/\/initiate object
String id_Social=getValue();
String token=hitApiToken2(lienToken);
LogUtil.warn(\"------------id_Social---------------->\", id_Social);
private String lien = \"#envVariable.Process2WebServiceCNAMEndPoint#\/\"+id_Social;
LogUtil.warn(\"------------lien---------------->\", lien);
LogUtil.warn(\"------------token---------------->\", token);
JSONObject jsonData = new JSONObject();
jsonData.put(\"idSocial\",id_Social);
hitApi(jsonData,lien,recordid,token);
 "}},{"className":"org.joget.apps.app.lib.BeanShellTool","properties":{"script":"\/\/ WEB SERVICE AMEN
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
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
LogUtil.info(\"------------PROCESS  2 --Web SERVICE  AMEN -------------\",\"\");
public String getValue(){
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
        Connection con3 = ds.getConnection();
        String id=\"\";
        try {
                if (!con3.isClosed()) {
                        String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
                        PreparedStatement stmtSelect = con3.prepareStatement(qrIdSoc);
                        String id_personne=\"#variable.id_personne#\";
                        LogUtil.warn(\"id personne  ----------------------------------------> \", id_personne);
                        stmtSelect.setString(1,id_personne);
                        LogUtil.warn(\"QUERY  ----------------------------------------> \", stmtSelect.toString());
                        ResultSet rs = stmtSelect.executeQuery();
                        while (rs.next()){
                                LogUtil.warn(\"idsocial  ----------------------------------------> \", rs.getString(1));
                                id=rs.getString(1);
                                return id;
                        }
                }
        } catch (Exception ex) {
                LogUtil.error(\"web service cnam\", ex, \"Error storing using jdbc\");
                return id;
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                                return id;
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"web service cnam\", ex, \"Error closing the jdbc connection\");
                        return id;
                }
        }
}
public void hitApi(JSONObject jsonData , String lien ,String tableId){
        try {
            URL url = new URL(lien);
            connHTTP = (HttpURLConnection) url.openConnection();
            \/\/ Request setup
            connHTTP.setRequestMethod(\"POST\");
            connHTTP.setRequestProperty(\"Content-Type\", \"application\/json\");
            connHTTP.setRequestProperty(\"Accept\", \"application\/json\");
            \/\/ connHTTP.setRequestProperty(\"apikey\", \"#envVariable.ApiKey#\");
              String username = \"#envVariable.loginAmen#\";
              String password = \"#envVariable.passAmen#\";
            \/\/   LogUtil.warn(\"username**********************************************\", username);
            \/\/   LogUtil.warn(\"password**********************************************\", password);
              String auth = username + \":\" + password;
              byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
              String authHeaderValue = \"Basic \" + new String(encodedAuth);
              connHTTP.setRequestProperty(\"Authorization\", authHeaderValue);
            connHTTP.setDoOutput(true);
            OutputStream os = connHTTP.getOutputStream();
            byte[] input = jsonData.toString().getBytes(\"utf-8\");
            os.write(input, 0, input.length);
            int status = connHTTP.getResponseCode();
            LogUtil.warn(\"response code \", String.valueOf(status));
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
                                LogUtil.info(\"response ----------------------------------------> \", line);
                                String responseString=line.toString();
                                String cleanedString=responseString.replace(\"[\", \"\").replace(\"]\", \"\");
                                LogUtil.info(\"cleanedString ----------------------------------------> \", cleanedString);
                                JSONObject jsonRes = new JSONObject(cleanedString);
                                LogUtil.info(\"--------------------->\",jsonRes.toString());
                               if(jsonRes.getString(\"code\")==0){
                                    String insertSql3 = \"UPDATE app_fd_personne SET c_couv_id=?  where id=?\";
                                    insertLine(jsonRes,tableId,insertSql3);
                               }
                    }
                    reader.close();
            }
    } catch (Exception e) {
            LogUtil.error(\"web service Amen\", e, \"ERROR\");
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
                        PreparedStatement stmtInsert3 = con3.prepareStatement(insertSql3);
                        stmtInsert3.setString(1,res.getString(\"libTypeSoin\"));
                        stmtInsert3.setString(2,tableId);
                        LogUtil.info(\"QUERY  ----------------------------------------> \", insertSql3);
                        stmtInsert3.executeUpdate();
                }
        } catch (Exception ex) {
                LogUtil.error(\"AMEN\", ex, \"Error storing using jdbc\");
        } finally {
                try {
                        if (con3 != null) {
                                con3.close();
                        }
                } catch (Exception ex) {
                        LogUtil.error(\"AMEN\", ex, \"Error closing the jdbc connection\");
                }
        }
}
Connection con = null;
DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
private String lien = \"#envVariable.webServiceAmen#\";
\/\/ LogUtil.info(\"------------PROCESS  2 --Web SERVICE  AMEN --------------LINK -------------\",lien);
private HttpURLConnection connHTTP;
private BufferedReader reader;
String line;
StringBuilder responseContent = new StringBuilder();
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
LogUtil.info(\"------------RecordID---------------->\", recordid);
JSONObject jsonData = new JSONObject();
String qrIdSoc=\"select c_identifiantsocial_id from app_fd_personne where id=?\";
String id_Social=getValue();
jsonData.put(\"ident_uni\",id_Social);
LogUtil.info(\"------------JSON IBJECT ---------------->\", jsonData.toString());
hitApi(jsonData,lien,recordid);
 "}}],"runInMultiThread":"","comment":"

