var exec = cordova.require("cordova/exec");

/**
 * Constructor.
 *
 * @returns {Xt30Scanner}
 */
function Xt30Scanner() {
}

/**
 * Read code from scanner.
 *
 * @param {Function} successCallback
 * @param {Function} errorCallback
 * @param config
 */
Xt30Scanner.prototype.scan = function (successCallback, errorCallback, config) {
      if (successCallback != null) {
        xt30Scanner.set(successCallback, errorCallback);
      }
      exec(null, null, 'XT30Scanner', "scan", null);
};

/**
 * Cancel.
 */
Xt30Scanner.prototype.cancel = function () {
      exec(null, null, 'XT30Scanner', 'cancel', null);
};

/**
 * Beep.
 */
Xt30Scanner.prototype.beep = function (mode) {
      exec(null, null, 'XT30Scanner', 'beep', [mode ? 1 : 0]);
};

/**
 * Register Callback.
 */
Xt30Scanner.prototype.set = function (successCallback, errorCallback) {
      exec(
        function(resultObj) {
            try {
              successCallback(resultObj.result, resultObj);
            } catch (err) {}
            xt30Scanner.set(successCallback, errorCallback);
        }
        , function(error) {
            try {
              errorCallback(error);
            } catch (err) {}
            xt30Scanner.set(successCallback, errorCallback);
        }, 'XT30Scanner', 'set', null);
};

/**
 * Set Trigger Mode.
 */
Xt30Scanner.prototype.setTriggerMode = function (mode) {
      if (mode == xt30Scanner.TRIGGER_MODE.ONESHOT || mode == xt30Scanner.TRIGGER_MODE.AUTO || mode == xt30Scanner.TRIGGER_MODE.CONTINUOUS) {
          exec(null, null, 'XT30Scanner', 'triggerMode', [mode]);
      }
};

Xt30Scanner.prototype.TRIGGER_MODE = {
    ONESHOT            : 2
    , AUTO             : 0
    , PULSE            : 0
    , CONTINUOUS       : 1
};

var xt30Scanner = new Xt30Scanner();
module.exports = xt30Scanner;
