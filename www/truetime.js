const exec = require("cordova/exec");

const PLUGIN_NAME = "Truetime";

function now() {
  return new Promise((resolve, reject) => {
    const parse = (timeString) => resolve(new Date(Number(timeString)));
    exec(parse, reject, PLUGIN_NAME, "now", []);
  });
}

module.exports = {
  now,
};
