'use strict';


/**
 * 
 * Delete a customer according to its ID
 *
 * body GetUserById Id to search
 * returns Success
 **/
exports.deleteUserClientPOST = function(body) {
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
 * Retrieve all the customers present in the database
 *
 * returns getAllUser
 **/
exports.getAllUserClientGET = function() {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : [ {
    "mail" : "lilian.giraudo@swagger.fr",
    "phone" : "0601020304",
    "image_url" : "null",
    "name" : "lilian",
    "birth" : "12-12-1994",
    "id" : 0,
    "username" : "lilian.giraudo",
    "lastname" : "giraudo"
  }, {
    "mail" : "lilian.giraudo@swagger.fr",
    "phone" : "0601020304",
    "image_url" : "null",
    "name" : "lilian",
    "birth" : "12-12-1994",
    "id" : 0,
    "username" : "lilian.giraudo",
    "lastname" : "giraudo"
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
 * Retrieve a customer based on its ID    Calls corresponding to this function are secured. To access a user's information you must be that user and you will have to send your identification token in the Header :   Key : \"Authorization\",  Value : \"<your_token>\"
 *
 * body GetUserById Id to search
 * no response value expected for this operation
 **/
exports.getUserClientByIdGET = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 
 * Retrieve a customer based on its username
 *
 * body GetUserByName Username to search
 * no response value expected for this operation
 **/
exports.getUserClientByUsernameGET = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 
 * Login to an account
 *
 * body Login Parameters to add to the request
 * returns LoginSuccess
 **/
exports.loginClientPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : {
    "mail" : "lilian.giraudo@swagger.fr",
    "phone" : "0601020304",
    "image_url" : "null",
    "name" : "lilian",
    "birth" : "12-12-1994",
    "id" : 0,
    "username" : "lilian.giraudo",
    "lastname" : "giraudo",
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
 * Create a new client   Password must be hash in Bcrypt with a salt of 10
 *
 * body UserCreation Parameters to add to the request
 * returns getAllUser
 **/
exports.registerClientPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "result" : [ {
    "mail" : "lilian.giraudo@swagger.fr",
    "phone" : "0601020304",
    "image_url" : "null",
    "name" : "lilian",
    "birth" : "12-12-1994",
    "id" : 0,
    "username" : "lilian.giraudo",
    "lastname" : "giraudo"
  }, {
    "mail" : "lilian.giraudo@swagger.fr",
    "phone" : "0601020304",
    "image_url" : "null",
    "name" : "lilian",
    "birth" : "12-12-1994",
    "id" : 0,
    "username" : "lilian.giraudo",
    "lastname" : "giraudo"
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

