'use strict';

const BaseController = require('./baseController');

const sqlConnection = require('../database/sqlConnection');

class SalesOrderController extends BaseController {
    constructor(endpoint, router) {
        super(endpoint, router);
        this.addRoutes(endpoint, router);
    }


    addRoutes(endpoint, router) {

        // Added fuctionality to GET sales order lines belonging to a specific sales order ID.
        router.get('/' + endpoint + '/:id/salesorderlines', function (req, res, next) {
            // CODE MADE BY REPO OWNER
            const connection = sqlConnection.createConnection();
            let query = 'SELECT * FROM ' + 'salesorderlines' + ' WHERE sales_order_id = \'' + req.params.id + '\'';

            connection.query(query, function (error, results, fields) {
                connection.end();
                if (error) next(error);
                res.json(results);
            });

            // END OF: CODE MADE BY REPO OWNER
        });



        // CODE MADE BY REPO OWNER
        // Added fuctionality to POST changes to sales orders.

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
                if (key.localeCompare("number") === 0 || key.localeCompare("customer") === 0 || key.localeCompare("status") === 0) {
                    salesOrderColumns.push(key);
                    salesOrderValues.push('\'' + body[key] + '\'');
                }

                // Save order number for later use
                if (key.localeCompare("number") === 0) {
                    salesOrderNumber = body[key];
                }

                // Gather information to be added to table "salesorderlines".
                if (key.localeCompare("lines") === 0) {
                    includesLines = 1;
                    const allLines = body[key];

                    for (let i = 0; i < allLines.length; i++) {
                        const quantity = allLines[i]["quantity"];
                        const productId = allLines[i]["product_id"];

                        salesOrderQuantities.push(quantity);
                        salesOrderProductIds.push(productId);
                    }
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
                    if (error) {
                        next(error);
                        reject(error);
                    }
                    // Get "id" from just added salesorder entry to be used when adding to table salesorderlines.
                    salesOrderId = results["insertId"];
                    resolve(salesOrderId);

                    // If true, there's nothing else to do. Close SQL connection and return results.
                    // Otherwise we do additional stuff in promise.then() and do these steps there.
                    if (includesLines == 0) {
                        connection.end();
                        res.json(results);
                    }
                })
            });

            // Wait 'quantity', 'customer' and 'status' to be added to database. Then if needed, add 'lines' to salesorderlines.
            promise.then((message) => {
                // Only add product lines to salesorderlines if POST included product lines.
                if (includesLines == 1) {
                    let valuesString = '(';
                    for (let i = 0; i < salesOrderProductIds.length; i++) {
                        valuesString += salesOrderQuantities[i] + ', ' + salesOrderProductIds[i] + ', ' + salesOrderId + '), (';
                    }

                    // Remove trailing extra characters ', ('
                    valuesString = valuesString.slice(0, -3) + ';';

                    query = 'INSERT INTO salesorderlines (quantity, product_id, sales_order_id) VALUES ';
                    query += valuesString;

                    connection.query(query, function (error, results, fields) {
                        connection.end();
                        if (error) next(error);
                        res.json(results);
                    });
                }
            }).catch((message) => {
                console.info("Error inside Promise: " + message);
            });

        });
        // END OF: CODE MADE BY REPO OWNER

        // CODE MADE BY REPO OWNER
        // Added functionality for PUT.
        /* update existing one */
        router.put('/' + endpoint + '/:id', function (req, res, next) {
            const body = req.body;
            const connection = sqlConnection.createConnection();
            let includesLines = 0;
            let allLines = [];
            let salesOrderId  = req.params.id;

            let query = 'UPDATE ' + endpoint + ' SET ';

            for (let key in body) {
                if (key.localeCompare("lines") === 0) {
                    includesLines = 1;
                    allLines = body[key];
                }
                else if (key.localeCompare("number") === 0 || key.localeCompare("customer") === 0 || key.localeCompare("status") === 0) {
                    query += '' + key + ' = \'' + body[key] + '\'';
                    query += ', ';
                }
            }

            // Remove trailing comma
            query = query.slice(0, -2) + ' ';

            query += 'WHERE id = \'' + req.params.id + '\';';

            const promise = new Promise((resolve, reject) => {
                connection.query(query, function (error, results, fields) {
                    if (error) {
                        next(error);
                        reject(error);
                    }

                    resolve(salesOrderId);

                    // If true, there's nothing else to do. Close SQL connection and return results.
                    // Otherwise we do additional stuff in promise.then() and do these steps there.
                    if (includesLines == 0) {
                        connection.end();
                        res.json(results);
                    }
                })

            });

            // Wait for 'quantity', 'customer' and 'status' to be added to database. Then if needed, process salesorderlines.
            // NOTE: Should "if (includesLines == 1)" be before this for better performance?
            promise.then((message) => {
                // Only add product lines to salesorderlines if POST included product lines.
                if (includesLines == 1) {

                    query = '';

                    for (let i = 0; i < allLines.length; i++) {
                        const operation = allLines[i]["operation"];
                        const quantity = allLines[i]["quantity"];
                        const productId = allLines[i]["product_id"];
                        const id = allLines[i]["id"];

                        // "Operation" always needs to be specified.
                        if (operation === undefined) {
                            throw new Error('Operation on specified for salesorderlines line!');
                        }
                        // "Update" and "Delete" require a valid id.
                        else if ( (operation === "update" || operation === "delete") && id === undefined ) {
                            throw new Error('ID not specified for salesorderlines line update/delete!');
                        }
                        else if (operation === "update" && (quantity === undefined || productId === undefined) ) {
                            throw new Error('UPDATE for salesorderlines line requires all the following defines: quantity, product_id');
                        }
                        
                        if (operation === "update") {

                            // TODO: Need to check if product_id matches a valid ID?
                            query = 'UPDATE salesorderlines SET';
                            query += ' quantity = \'' + quantity + '\', ' + 'product_id = \'' + productId + '\'';
                            query += ' WHERE id = ' + id + ';';
                        } 
                        else if (operation === "delete") {
                            query = 'DELETE FROM salesorderlines WHERE id = \'' + id + '\';';
                        } 
                        else if (operation === "create") {
                            query = 'INSERT INTO salesorderlines (quantity, product_id, sales_order_id) VALUES (' + quantity + ', ' + productId + ', ' + salesOrderId + ');';
                        }

                        // Assuming salesorderlines changes are mutually exclusive. Otherwise need to add waiting for previous query to finish.
                        connection.query(query, function (error, results, fields) {
                            if (error) next(error);
                        });

                    }

                    // Query to return original salesorder as results for whole POST.
                    query = 'SELECT * FROM ' + endpoint + ' WHERE id = ' + req.params.id + ';';      

                    connection.query(query, function (error, results, fields) {
                        connection.end();
                        if (error) next(error);
                        res.json(results);
                    });


                }
                
            }).catch((message) => {
                console.info("Error inside Promise: " + message);
            });

        });
        // CODE MADE BY REPO OWNER
    }
}

module.exports = SalesOrderController;

