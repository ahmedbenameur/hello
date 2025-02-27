{"tools":[{"className":"org.joget.apps.app.lib.DatabaseUpdateTool","properties":{"jdbcDatasource":"default","query":"UPDATE app_fd_recours AS r
JOIN app_fd_demande_carte AS d ON r.c_id_personne = d.c_id_personne
SET
    r.c_decision_id =
        CASE
            WHEN r.c_degree_handicap <> d.c_degree_handicap AND r.c_type_id <> d.c_type_id THEN 'UpdateDecisionDegreeHandicapTypeHandicap'
            WHEN r.c_degree_handicap <> d.c_degree_handicap THEN 'UpdateDecisionDegreeHandicap'
            WHEN r.c_type_id <> d.c_type_id THEN 'UpdateDecisionTypeHandicap'
            ELSE 'deniedRequest'
        END,
    r.c_statut =
        CASE
            WHEN r.c_degree_handicap <> d.c_degree_handicap AND r.c_type_id <> d.c_type_id THEN 'UpdateDecisionDegreeHandicapTypeHandicap'
            WHEN r.c_degree_handicap <> d.c_degree_handicap THEN 'UpdateDecisionDegreeHandicap'
            WHEN r.c_type_id <> d.c_type_id THEN 'UpdateDecisionTypeHandicap'
            ELSE 'deniedRequest'
        END
WHERE r.c_id_personne = \"#variable.id_personne#\" AND r.id != \"\";
"}},{"className":"org.joget.apps.app.lib.DatabaseUpdateTool","properties":{"jdbcDatasource":"default","query":"update app_fd_demande_carte set c_recours='done' where id='#variable.processID#'"}}],"runInMultiThread":"","comment":"

