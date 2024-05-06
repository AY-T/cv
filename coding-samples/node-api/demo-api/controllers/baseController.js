'use strict';

const sqlConnection = require('../database/sqlConnection');

class BaseController {
    constructor(endpoint, router) {
        this.addDefaultRoutes(endpoint, router);
    }

    addDefaultRoutes(endpoint, router) {
        /* get all */
        router.get('/' + endpoint, function (req, res, next) {

            const connection = sqlConnection.createConnection();
            let query = 'SELECT * FROM ' + endpoint + ' ';

            // CODE MADE BY REPO OWNER
            // Added handling of query parameters as part of task 4.
            let conditions = '';

            // Parse query parameters if present, and later add them to the SQL query.
            if (Object.keys(req.query).length !== 0) {
                conditions = conditions.concat(' WHERE ')

                for (let condition in req.query) {
                    const key = condition.toString();
                    const value = req.query[condition].toString();
                    const conditionToAdd = key + ' = \'' + value + '\' and ';
                    conditions = conditions.concat(conditionToAdd);
                }

                // Remove trailing 'and ' and replace with ' '.
                conditions = conditions.slice(0, -4);
                conditions = conditions.concat(' ');

            }

            query = query.concat(conditions + ';');

            // END OF: CODE MADE BY REPO OWNER

            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });
        });


        /* get by id */
        router.get('/' + endpoint + '/:id', function (req, res, next) {
            const connection = sqlConnection.createConnection();
            const query = 'SELECT * FROM ' + endpoint + ' WHERE id = ' + req.params.id + ';';
            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });
        });

        // PUT code was here.
    }
}
module.exports = BaseController;
