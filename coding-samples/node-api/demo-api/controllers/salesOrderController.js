'use strict';

const BaseController = require('./baseController');

const sqlConnection = require('../database/sqlConnection');

class SalesOrderController extends BaseController {
    constructor(endpoint, router) {
        super(endpoint, router);
        this.addRoutes(endpoint, router);
    }

    
    executeSqlQuery(query) {
        let salesOrderId = '';
        console.info("started executeSqlQuery()");

        const connection = sqlConnection.createConnection();
        connection.query(query, function (error, results, fields) {
            // connection.end();
            if (error) next(error);
            // res.json(results);
            // Get "id" from just added salesorder entry to be used when adding to table salesorderlines.
            salesOrderId = results["insertId"];
            console.info("Inside query: " + salesOrderId);
        })

        return salesOrderId;

    }
    
    addRoutes(endpoint, router) {
        // Added fuctionality to GET sales order lines belonging to a specific sales order ID.
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
        // END OF: Added fuctionality to GET sales order lines belonging to a specific sales order ID.


        // Added fuctionality to POST changes to sales orders.
        // TODO: Check that post for Task 1 works (and Task 2)

        router.post('/' + endpoint, function (req, res, next) {
            const body = req.body;
            let query = 'INSERT INTO ' + endpoint + ' (';
                                  
            const salesOrderColumns = [];
            const salesOrderValues = [];
            const salesOrderQuantities = [];
            const salesOrderProductIds = [];
            let salesOrderNumber = '';
            let includesLines = 0;

            for (let key in body) {
                // Gather information to be added to table "salesorders".
                if ( key.localeCompare("number") === 0 || key.localeCompare("customer")  === 0 || key.localeCompare("status")  === 0 ) {
                    salesOrderColumns.push(key);
                    salesOrderValues.push('\'' + body[key] + '\'');

                    // console.info(key + ': ' + body[key]);
                }

                // Save order number for later use
                if ( key.localeCompare("number") === 0 ) {
                    salesOrderNumber = body[key];
                }

                // Gather information to be added to table "salesorderlines".
                if ( key.localeCompare("lines") === 0 ) {
                    includesLines = 1;
                    // console.info("Found Lines");
                    const allLines = body[key];
                    
                    for (let i = 0; i < allLines.length; i++) {
                        const quantity = allLines[i]["quantity"];
                        const productId = allLines[i]["product_id"];

                        salesOrderQuantities.push(quantity);
                        salesOrderProductIds.push(productId);
                    }

                    // Should also check if product_id is found in table "producs" i.e. is valid.
                }

                // Process product quantities and product_ids.
                for (let i = 0; i < salesOrderProductIds.length; i++) {
                    // console.info('OK: Line ' + i + ' quantity is ' + salesOrderQuantities[i] + ' and product_id is ' + salesOrderProductIds[i]);
                }
            }

            // Query to add to table "salesorders".
            let addToSalesordersTableQuery = 'INSERT INTO ' + endpoint + ' (';
            addToSalesordersTableQuery += salesOrderColumns.toString() + ')';
            addToSalesordersTableQuery += ' VALUES (' + salesOrderValues.toString() + '); ';

            // Need to query table "salesorders" (for salesOrderId) to match to salesorderlines (quantity and product_id).
            let salesOrderId = '';

            const connection = sqlConnection.createConnection();
            
            const promise = new Promise((resolve, reject) => {
                connection.query(addToSalesordersTableQuery, function (error, results, fields) {
                    // connection.end();
                    if (error) {
                        next(error);
                        reject(error);
                    }
                    // res.json(results);
                    // Get "id" from just added salesorder entry to be used when adding to table salesorderlines.
                    salesOrderId = results["insertId"];
                    // console.info("Inside query: " + salesOrderId);
                    resolve(salesOrderId);
                    if (includesLines == 0) {
                        connection.end();
                        res.json(results);
                    }
                })
            });

            // Wait 'quantity', 'customer' and 'status' to be added to database. Then if needed, ass 'lines' to salesorderlines.
            promise.then((message) => {
                // Only add to databes if POST included lines to be added to salesorderlines.
                if (includesLines == 1) {
                    let valuesString = '(';
                    for  (let i = 0; i < salesOrderProductIds.length; i++) {
                        valuesString += salesOrderQuantities[i] + ', ' + salesOrderProductIds[i] + ', ' + salesOrderId + '), (';
                    }

                    // Remove trailing extra characters ', ('
                    valuesString = valuesString.slice(0, -3) + ';';

                    query = 'INSERT INTO salesorderlines (quantity, product_id, sales_order_id) VALUES ';
                    query += valuesString;

                    // Working query: 
                    // query = 'INSERT INTO salesorderlines (quantity, product_id, sales_order_id) VALUES (101, 1, ' + salesOrderId + ');';
                    // console.info(query);
                    connection.query(query, function (error, results, fields) {
                        connection.end();
                        if (error) next(error);
                        res.json(results);
                    });
                }
            }).catch((message) => {
                console.info("Error inside Promise: " + message);
            });
            
            // console.info("Tried to POST salesorders");
        });
        // END OF: Added fuctionality to POST changes to sales orders.
    }
}

module.exports = SalesOrderController;

