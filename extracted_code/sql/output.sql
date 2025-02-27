{"tools":[{"className":"org.joget.apps.app.lib.DatabaseUpdateTool","properties":{"jdbcDatasource":"default","query":"update app_fd_demande_carte set c_recoursDemandeFirsteTime='false' where id='#variable.processID#'"}},{"className":"org.joget.apps.app.lib.DatabaseUpdateTool","properties":{"jdbcDatasource":"default","query":"INSERT INTO app_fd_recours (
    id,
    c_num_demande,
    dateCreated,
    c_id_personne,
    c_statut,
    c_recours,
    c_nature,
    c_degree_handicap,
    c_type_id,
    c_decision_id,
    c_typerecours,
    c_activityprocessid,
    c_id_demande,
    c_old_decision_id
) VALUES(
 UUID(),
     (select concat('إعتراض ',c_num_demande) from jwdb.app_fd_demande_carte where c_id_personne='#variable.id_personne#'),
    CURRENT_DATE,
    '#variable.id_personne#',
    'المهمة قيد التنفيذ',
    'false',
    'مطلب إعتراض-طلب بطاقة الإعاقة',
    (SELECT c_degree_handicap FROM app_fd_demande_carte WHERE id = '#variable.processID#'),
    (SELECT c_type_id FROM app_fd_demande_carte WHERE id = '#variable.processID#'),
    \"\",
    (SELECT c_typerecours FROM app_fd_demande_carte WHERE id = '#variable.processID#'),
    \"#assignment.processId#\",
    '#variable.processID#',
    '#variable.decision_id#'
)"}}],"runInMultiThread":"","comment":"

