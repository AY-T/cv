/* NOTE: This class WAS created by me, the repo owner, AYT. 
 *
 */

const BaseController = require('./baseController');

const sqlConnection = require('../database/sqlConnection');

class ProductController extends BaseController {
    constructor(endpoint, router) {
        super(endpoint, router);
        this.addRoutes(endpoint, router);
    }


    addRoutes(endpoint, router) {
        /* create new product */

        router.post('/' + endpoint, function (req, res, next) {
            const body = req.body;
            const columns = [];
            const values = [];
            const connection = sqlConnection.createConnection();
            let query = 'INSERT INTO ' + endpoint + ' (';
            for (let key in body) {
                columns.push(key);
                values.push('\'' + body[key] + '\'');
            }
            query += columns.toString() + ')';
            query += ' VALUES (' + values.toString() + ');';
            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });
        });

        // CODE MADE BY REPO OWNER
        // Added functionality for PUT.
        /* update existing one */
        router.put('/' + endpoint + '/:id', function (req, res, next) {
            const body = req.body;
            const columns = [];
            const values = [];
            const connection = sqlConnection.createConnection();

            let query = 'UPDATE ' + endpoint + ' SET ';
            for (let key in body) {
                query += '' + key + ' = \'' + body[key] + '\'';
                query += ', ';
            }

            // Remove trailing comma
            query = query.slice(0, -2) + ' ';

            query += 'WHERE id = \'' + req.params.id + '\';';

            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });

        });
        // CODE MADE BY REPO OWNER
    }


}

module.exports = ProductController;