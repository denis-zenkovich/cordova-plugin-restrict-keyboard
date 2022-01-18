var exec = require('cordova/exec');

module.exports = {
  isDefaultKeyboard: function(onSuccess, onError) {
    exec(onSuccess, onError, 'RestrictKeyboard', 'isDefaultKeyboard', []);
  },
};