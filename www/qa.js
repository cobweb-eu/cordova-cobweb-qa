var exec = require('cordova/exec');

exports.isBlurred = function(success, error, params) {
    console.log("=>");
    exec(success, error, "CobwebQA", "isBlurred", []);
};
