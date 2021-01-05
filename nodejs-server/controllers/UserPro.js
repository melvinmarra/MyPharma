'use strict';

var utils = require('../utils/writer.js');
var UserPro = require('../service/UserProService');

module.exports.deleteUserProPOST = function deleteUserProPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserPro.deleteUserProPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getAllUserProGET = function getAllUserProGET (req, res, next) {
  UserPro.getAllUserProGET()
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getUserProByIdGET = function getUserProByIdGET (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserPro.getUserProByIdGET(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.getUserProByPharmacyGET = function getUserProByPharmacyGET (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserPro.getUserProByPharmacyGET(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.loginProPOST = function loginProPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserPro.loginProPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.registerProPOST = function registerProPOST (req, res, next) {
  var body = req.swagger.params['body'].value;
  UserPro.registerProPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
