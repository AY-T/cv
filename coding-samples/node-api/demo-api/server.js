'use strict';

const express = require('express');
const http = require('http');
const port = 3001;
const app = express();
const server = http.createServer(app);
const router = express.Router();
const bodyParser = require('body-parser');
const uuid = require('node-uuid');
/* declare controllers */
const SalesOrderController = require('./controllers/salesOrderController');
const ProductController = require('./controllers/productController');

// middleware to use for all requests
router.use(function (req, res, next) {
    next();
});
app.use(function (req, res, next) {
    req.uuid = uuid.v4();
    console.info('[ Incoming request ] [ %s ] %s %s %o', req.uuid, req.method, req.url, req.headers);
    if (req.body &&  Object.keys(req.body).length > 0) {
        console.info('[ %s ] [ Incoming request body ] %o', req.uuid, req.body);
    }
    next();
});
app.use(bodyParser.urlencoded({
        extended: true
}));
app.use(bodyParser.json({
        limit: '100mb'
}));

app.use('/demo-api', router);

app.use(function (err, req, res, next) {
    res.status(500)
    res.json({ error: err })
});

/* init controllers */
new SalesOrderController('salesorders', router);
new ProductController('products', router);

server.listen(port,  function () {
    // Check if we are running as root
    if (typeof process.getgid === 'function' && process.getgid() === 0) {
        process.setgid('www-data');
        process.setuid('www-data');
    }
    console.info('Server listening on port %d', port);
});

