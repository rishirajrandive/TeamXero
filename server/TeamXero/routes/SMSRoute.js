/**
 * Created by pratiksanglikar on 9/25/16.
 */
var express = require('express');
var router = express.Router();
var accountSid = 'AC495305136b825d112cd0e7523159cce9';
var authToken = '7de49b648c53c1df8323bacde02ee7f4';
var client = require('twilio')(accountSid, authToken);
var MongoDB = require('../js/DAO/MongoDBHandler');

router.get('/', function (req, res, next) {
	res.render('index', {title: 'TeamXero REST APIs'});
});

router.get('/messages', function (req, res, next) {
	client.messages.list({}, function (err, data) {
		if (err) {
			res.send("Error: " + err);
		} else {
			data.messages.forEach(function (message) {
				console.log(message.friendlyName);
				res.send("OK");
			});
		}
	});
});

router.post('/messages', function (req, res, next) {
	MongoDB.collection("sms").insert({
		'message':'message received'
	});
	res.send({});
});

module.exports = router;
