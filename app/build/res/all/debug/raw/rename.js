var fs = require('fs');
fs.readdir(__dirname, function(err, files) {
  files.forEach(function(name) {
    fs.rename(__dirname + '/' + name, __dirname + '/' + name.toLowerCase());
  });
});