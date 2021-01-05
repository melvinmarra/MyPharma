'use strict';

var utils = require('../utils/writer.js');
var UserClient = require('../service/UserClientService');

module.exports.deleteUserClientPOST = function deleteUserClientPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserClient.deleteUserClientPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getAllUserClientGET = function getAllUserClientGET (req, res, next) {
  UserClient.getAllUserClientGET()
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getUserClientByIdGET = function getUserClientByIdGET (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserClient.getUserClientByIdGET(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getUserClientByUsernameGET = function getUserClientByUsernameGET (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserClient.getUserClientByUsernameGET(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.loginClientPOST = function loginClientPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserClient.loginClientPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.registerClientPOST = function registerClientPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserClient.registerClientPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
