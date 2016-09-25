/**
 * Created by pratiksanglikar on 9/24/16.
 */
var MongoDB = require("../DAO/MongoDBHandler");
var Q = require("q");

/*
exports.find = function (latitude, longitude, radius) {
	var deferred = Q.defer();
	var locationList = [];
	var cursor = MongoDB.collection("users").find({"location": { $geoWithin: { $centerSphere: [ [ longitude, latitude ], radius/3963.2 ] } }});

	if(cursor != null)
	{
		cursor.each(function (err, doc) {
			if (err) {
				deferred.reject({
					statusCode: 500,
					error: "Not found"
				});
				//deferred.reject(err);
			}
			if (doc != null) {
				locationList.push(doc);
			}
			else
			{
				deferred.resolve(locationList);

				//  deferred.resolve(farmerList);
			}
		});
	}
	else
	{
		deferred.reject({
			statusCode: 500,
			"Error": "Error"
		});
	}
	return deferred;
}
*/

exports.enableNotifications = function (email, longitude,latitude ) {
	var deferred = Q.defer();
	var info =
	{
		"email" : email,
		"location" : [
		longitude,
		 latitude ]
	};

	var cursor = MongoDB.collection("consumer").insert(info);
	cursor.then(function (user) {
		deferred.resolve(user);
	}).catch(function (error) {
		deferred.reject(error);
	});
	return deferred.promise;
	//return true;
};

exports.disableNotifications = function (email) {
	var deferred = Q.defer();
	MongoDB.collection("consumer").remove({
		"email": email
	}, function (err, numberOfRemoved) {
		if (err) {
			deferred.reject(err);
		}
		if (numberOfRemoved.result.n) {
			deferred.resolve();
		} else {
			deferred.reject("consumer with given email not found in system!");
		}
	});
	return deferred.promise;
}

exports.notificationupdate = function (email, longitude,latitude){
	var deferred = Q.defer();
	var info =
	{
		"email" : email,
		"location" : [
			longitude,
			latitude]
	};
	var cursor = MongoDB.collection("consumer").update({"email" : email},
		{
			"email" : email,
			"location" : [
				longitude,
				latitude]

		});
	cursor.then(function (result) {
		deferred.resolve(result);
	}).catch(function (error) {
		deferred.reject(error);
	});
	return deferred.promise;
};




exports.find = function (latitude, longitude, radius) {
	var deferred = Q.defer();
	var locationList = [];
	var cursor = MongoDB.collection("users").find({"location": { $geoWithin: { $centerSphere: [ [ longitude, latitude ], radius/3963.2 ] } }});
	//var trucks = [];
	cursor.each(function (error, doc) {
		if (error) {
			deferred.reject(error);
		}
		if (doc != null) {
			locationList.push(doc);
		} else {
			deferred.resolve(locationList);
		}
	});
	return deferred.promise;
};

exports.registerProvider = function (info) {
	var deferred = Q.defer();
	var cursor = MongoDB.collection("users").insert(info);
	cursor.then(function (user) {
		deferred.resolve(user);
	}).catch(function (error) {
		deferred.reject(error);
	});
	return deferred.promise;
	//return true;
};

exports.getProvidersInZipcode = function (zipcode) {
	var deferred = Q.defer();
	var cursor = MongoDB.collection("users").find({
    "zipcode" : zipcode
	});
	var usersdata = [];
	cursor.each(function (error, doc) {
		if (error) {
			deferred.reject(error);
		}
		if (doc != null) {
			usersdata.push(doc);
		} else {
			deferred.resolve(usersdata);
		}
	});
	return deferred.promise;
};