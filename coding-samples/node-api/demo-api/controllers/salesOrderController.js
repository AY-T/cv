'use strict';

const BaseController = require('./baseController');

const sqlConnection = require('../database/sqlConnection');

class SalesOrderController extends BaseController {
    constructor(endpoint, router) {
        super(endpoint, router);
        this.addRoutes(endpoint, router);
    }

    // Added fuctionality to get sales order lines belonging to a specific sales order ID.
    addRoutes(endpoint, router) {
        router.get('/' + endpoint + '/:id/salesorderlines', function (req, res, next) {
            /* Tehtävä 5 */
            const connection = sqlConnection.createConnection();
            let query = 'SELECT * FROM ' + 'salesorderlines' + ' WHERE sales_order_id = \'' + req.params.id + '\'';

            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });
        });
    }
}

module.exports = SalesOrderController;

