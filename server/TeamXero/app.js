var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');
var SMSRoute = require("./routes/SMSRoute");
var MongoDB = require("./js/DAO/MongoDBHandler");

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);
app.use('/sms', SMSRoute);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

MongoDB.connect(MongoDB.MONGODB_URL, function () {
  console.log('Connected to mongo at: ' + MongoDB.MONGODB_URL);
});

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

var accountSid = 'AC495305136b825d112cd0e7523159cce9';
var authToken = '7de49b648c53c1df8323bacde02ee7f4';

//require the Twilio module and create a REST client
var client = require('twilio')(accountSid, authToken);

/*client.incomingPhoneNumbers.create({
  phoneNumber: "4089048415",
  areaCode: "408",
  friendlyName: "TeamXero",
}, function(err, number) {
	if(err) {
		console.log(err);
	} else {
		console.log(number.sid);
	}
});*/



module.exports = app;
