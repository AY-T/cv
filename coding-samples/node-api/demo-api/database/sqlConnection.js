'use strict';

const createConnection = () => {
    const mysql      = require('mysql');
    const connection = mysql.createConnection({
        host     : 'localhost',
        user     : 'demo-api',
        password : 'demo-api',
        database : 'demo-api'
    });

    connection.connect();

    return connection;
}

exports.createConnection = createConnection;
