'use strict';

const BaseController = require('./baseController');

const sqlConnection = require('../database/sqlConnection');

class SalesOrderController extends BaseController {
    constructor(endpoint, router) {
        super(endpoint, router);
        this.addRoutes(endpoint, router);
    }

    addRoutes(endpoint, router) {
        router.get('/' + endpoint + '/:id/salesorderlines', function (req, res, next) {
            /* Tehtävä 5 */
        });
    }
}

module.exports = SalesOrderController;

