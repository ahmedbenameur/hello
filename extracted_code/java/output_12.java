public class Output {
import org.joget.workflow.model.service.*;
import java.lang.Exception;
import org.joget.commons.util.LogUtil;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.*;
String id_personne = \"#requestParam.id_personne#\";
String type_process = \"#requestParam.type_process#\";
LogUtil.info(\"----> id_personne\",id_personne);
 \/\/Get record Id from process
AppService appService = (AppService) AppUtil.getApplicationContext().getBean(\"appService\");
String recordid = appService.getOriginProcessId(workflowAssignment.getProcessId());
WorkflowManager wm = (WorkflowManager) pluginManager.getBean(\"workflowManager\");
wm.activityVariable(workflowAssignment.getActivityId(),\"id_personne\", id_personne);
wm.activityVariable(workflowAssignment.getActivityId(),\"type_process\", type_process);
wm.activityVariable(workflowAssignment.getActivityId(),\"processID\", recordid);


}