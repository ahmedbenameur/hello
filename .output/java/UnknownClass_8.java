\/\/mailing
\/\/envoi d'email
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.joget.apps.app.service.*;
import org.joget.apps.app.model.*;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import java.sql.*;
import java.util.*;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.model.AppActivity;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormStoreBinder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.sql.*;
import org.joget.commons.util.UuidGenerator;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.commons.util.StringUtil;
import org.joget.apps.form.model.Form;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.*;
import java.util.*;
import java.sql.Date;
import java.util.Collection;
import org.joget.directory.model.User;
import org.joget.directory.model.service.ExtDirectoryManager;
import org.joget.workflow.shark.model.dao.WorkflowAssignmentDao;
import org.joget.workflow.model.WorkflowAssignment;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.workflow.util.WorkflowUtil;
import org.joget.commons.util.PluginThread;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Store;
import java.util.ArrayList;
import java.util.List;
import org.joget.apps.app.service.AppService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.plugin.base.PluginManager;
import org.joget.commons.util.UuidGenerator;
import org.joget.commons.util.PluginThread;
import org.apache.commons.mail.HtmlEmail;
import org.joget.commons.util.StringUtil;
import java.util.Random;
import org.joget.commons.util.UuidGenerator;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.service.AppUtil;
import java.util.Set;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.SecurityUtil;
import org.joget.directory.dao.UserDao;
import org.joget.directory.dao.RoleDao;
import org.joget.directory.model.User;
import org.joget.directory.model.service.DirectoryUtil;
import org.joget.directory.model.service.UserSecurity;
import org.joget.commons.util.StringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.commons.util.UuidGenerator;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.ApplicationPlugin;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.joget.directory.model.service.ExtDirectoryManager;
import java.lang.Thread;
    \/\/ config mail
    \/\/Reuse Email Tool to send separated email to a list of users;
    Plugin plugin = pluginManager.getPlugin(\"org.joget.apps.app.lib.EmailTool\");
    \/\/Get default properties (SMTP setting) for email tool
    Map propertiesMap = AppPluginUtil.getDefaultProperties(plugin, null, appDef, null);
    propertiesMap.put(\"pluginManager\", pluginManager);
    propertiesMap.put(\"appDef\", appDef);
    propertiesMap.put(\"request\", request);
    ApplicationPlugin emailTool = (ApplicationPlugin) plugin;
     Collection mail = new ArrayList();
 String msg=\"\";
    DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean(\"setupDataSource\");
    Connection con = ds.getConnection();
    String dateCom=\"\";
    String nomPres=\"\";
            \/\/
    try{
            String query = \"select * from app_fd_commission where id=?\";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1,\"#variable.processID#\");
    \/\/app_fd_membre where c_id_commission=?
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                    dateCom=rs.getString(\"c_datecomm_id\");
                    nomPres=rs.getString(\"c_nompres_id\");
                    LogUtil.warn(\"---------------------------------> send to date\"+dateCom+\" pres: \",nomPres);
            }
            String query2 = \"select * from app_fd_membre where c_id_commission=?\";
            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setString(1,\"#variable.processID#\");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                    mail.add(rs2.getString(\"c_mailMembre\"));
                    LogUtil.warn(\"---------------------------------> send to mail: \",(rs2.getString(\"c_mailMembre\")));
            }
            String query3 = \"select email from dir_user where id=?\";
            PreparedStatement stmt3 = con.prepareStatement(query3);
            stmt3.setString(1,nomPres);
            ResultSet rs3 = stmt3.executeQuery();
            while (rs3.next()) {
                    mail.add(rs3.getString(1));
                    LogUtil.warn(\"---------------------------------> send to mail: \",(rs3.getString(1)));
            }
        } catch(Exception e) {
                LogUtil.error(\"Sample app - Form 1\", e, \"Error loading user data in load binder\");
        } finally {
                \/\/always close the connection after used
                try {
                        if(con != null) {
                                con.close();
                        }
                } catch(SQLException e) {\/* ignored *\/}
        }
        msg = \"\
\"+\"            3086  2005                 2                   \"+dateCom +\"     \
 \";
            LogUtil.warn(\"---------------------------------> send to mail for loop\",msg);
            for(String m : mail ){
                         propertiesMap.put(\"toSpecific\",m);
                         propertiesMap.put(\"subject\", \"       \");
                         propertiesMap.put(\"message\", msg);
                        \/\/set properties and execute the tool
                        ((PropertyEditable) emailTool).setProperties(propertiesMap);
                        emailTool.execute(propertiesMap);
                        LogUtil.info(\"---> send to \"+m+\" message: \",msg);
                          try {
                             Thread.sleep(10000);
                        } catch (InterruptedException e) {
                        }
            }

