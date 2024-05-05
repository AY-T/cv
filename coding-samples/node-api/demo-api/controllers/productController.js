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
/*
        router.get('/' + endpoint + '/:id/productlines', function (req, res, next) {
            // Tehtävä 5
        }); 
*/

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
    }


}

module.exports = ProductController;