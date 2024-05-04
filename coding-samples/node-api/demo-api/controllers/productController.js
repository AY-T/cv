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
        router.get('/' + endpoint + '/:id/productlines', function (req, res, next) {
            /* Tehtävä 5 */
        });
    }


}

module.exports = ProductController;