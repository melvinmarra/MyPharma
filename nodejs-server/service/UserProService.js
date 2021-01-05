'use strict';


/**
 * 
 * Delete a professional according to its ID
 *
 * body GetUserById Id to search
 * returns Success
 **/
exports.deleteUserProPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : 0,
  "succes" : true
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 
 * Retrieve all the professional present in the database
 *
 * returns getAllUserPro
 **/
exports.getAllUserProGET = function() {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : [ {
    "is_admin" : 1,
    "pharmacy_id" : 6,
    "id" : 0,
    "username" : "melvin.marra"
  }, {
    "is_admin" : 1,
    "pharmacy_id" : 6,
    "id" : 0,
    "username" : "melvin.marra"
  } ],
  "success" : true
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 
 * Retrieve a professionnal based on its ID    Calls corresponding to this function are secured. To access a user's information you must be that user and you will have to send your identification token in the Header :   Key : \"Authorization\",  Value : \"<your_token>\"
 *
 * body GetUserById Id to search
 * no response value expected for this operation
 **/
exports.getUserProByIdGET = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 
 * Retrieve a customer based on its pharmacy
 *
 * body GetUserProByPharmacy Pharmacy id to search
 * no response value expected for this operation
 **/
exports.getUserProByPharmacyGET = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 
 * Login to a professionnal account
 *
 * body Login Parameters to add to the request
 * returns LoginPro
 **/
exports.loginProPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : {
    "pharmacy_id" : 6,
    "id" : 0,
    "username" : "melvin.marra",
    "token" : "eyFndeodojdTdfsFRSkfjspkfsjdbflsdnlsfnlsbfpsinflsfbs"
  },
  "succes" : true
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 
 * Create a professional according to its name and pharmacy id
 *
 * body ProCreation Parameters of the request
 * returns ProCreationSuccess
 **/
exports.registerProPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : {
    "is_admin" : 1,
    "pharmacy_id" : 6,
    "id" : 0,
    "username" : "melvin.marra"
  },
  "succes" : true
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

