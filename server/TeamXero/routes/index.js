var express = require('express');
var router = express.Router();
var LocationHandler = require("../js/DAO/LocationHandler");
var MongoDB = require("../js/DAO/MongoDBHandler");
var Q = require("q");
/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'TeamXero REST APIs'});
});

router.post('/find', function (req, res, next) {
    var latitude = req.body.latitude,
        longitude = req.body.longitude,
        radius = req.body.radius;
    var promise = LocationHandler.find(latitude, longitude, radius);

    promise.done(function (response) {
        res.send(response);
    }, function (error) {
        res.send(error);
    });
});

router.post('/getProvidersInZipCode', function (req, res, next) {

       var zipcode = req.body.zipcode;
    var promise = LocationHandler.find(zipcode);

    promise.done(function (response) {
        res.send(response);
    }, function (error) {
        res.send(error);
    });
});

router.post('/notifications', function (req, res, next) {

    var latitude = req.body.latitude,
        longitude = req.body.longitude,
        token = req.body.token;
    if(token != null)
    {
    var promise = LocationHandler.enableNotifications(token, longitude, latitude);

    promise.done(function (response) {
        res.send({
            "statuscode": 200
        });
    }, function (error) {
        res.send({
            "statuscode": 500
        });
    });
}
});

router.delete('/notifications', function (req, res, next) {
    var token = req.body.token;
    if(token != null)
    {
    var promise = LocationHandler.disableNotifications(token);
    promise.done(function (response) {
        res.send(
            {
                "statuscode": 200
            });
    }, function (error) {
        res.send({
            "statuscode": 500
        });
    });
}
});

router.post('/notificationupdate', function (req, res, next) {
    var latitude = req.body.latitude,
        longitude = req.body.longitude,
        token = req.body.token;
    if(token != null) {
        var promise = LocationHandler.notificationupdate(token, longitude, latitude);
        promise.done(function (response) {
            res.send({
                "statuscode": 200
            });
        }, function (error) {
            res.send({
                "statuscode": 500
            });
        });
    }
});

router.post('/registerProvider', function (req, res, next) {

    var info =
    {
        "firstName": req.body.firstName,
        "lastName": req.body.lastName,
        "location": [
            req.body.location[0],
            req.body.location[1]
        ],
        "address": {
            "address": req.body.address.address,
            "city": req.body.address.city,
            "State": req.body.address.State,
            "zipcode": req.body.address.zipcode
        },
        "contact": req.body.contact,
        "available": true,
        "foodtype": "Indian",
        "quantity": req.body.quantity,
        "description": req.body.description,
        "imageLink": req.body.imageLink
    };

    info.timestamp = (new Date).getTime();
    info.expirydate = info.timestamp + 10000;

    var promise = LocationHandler.registerProvider(info);

    promise.done(function (response) {
        var promise1 = notifyConsumers(req.body.location[0], req.body.location[1]);
        //promise1.done(function (response)
        promise1.done(function (response) {
            res.send({
                "statuscode": 200,
                data: response
            });
        }, function (error) {
            res.send({
                "statuscode": 500
            });
        }), function (error) {
            res.send({
                "statusCode": 500
            });
        }
    });

});

notifyConsumers = function (longitude, latitude) {
    var deferred = Q.defer();
    var consumer = [];
    console.log(longitude + " " + latitude);
    var cursor = MongoDB.collection("consumer").find({"location": {$geoWithin: {$centerSphere: [[longitude, latitude], 10000000000000 / 3963.2]}}});
    cursor.each(function (error, doc) {
        if (error) {
            deferred.reject(error);
        }
        if (doc != null) {
            consumer.push(doc);
        } else {
            deferred.resolve(consumer);
        }
    });
    return deferred.promise;
}
module.exports = router;
