const exec = require("cordova/exec");

const PLUGIN_NAME = "Truetime";

module.exports = {
  now: () => {
    return new Promise((resolve, reject) => {
      exec(resolve, reject, PLUGIN_NAME, "now", []);
    });
  },
  init: () => {
    return new Promise((resolve, reject) => {
      exec(resolve, reject, PLUGIN_NAME, "reInit", []);
    });
  },
};
