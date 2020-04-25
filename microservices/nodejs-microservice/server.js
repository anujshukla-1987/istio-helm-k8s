var express = require('express');
var request = require('request');
var app = express();

app.get('/boot-node', function (req, res) {
   res.send('Response form chain Boot --> Node');
})

app.get('/directNode', function (req, res) {
   res.send('Direct response from node server');
})

app.get('/boot-node-py', function (req, res) {
	request.get('http://python-backend-service:5000/boot-node-py', function (error, response, body) {
		console.log('error:', error); // Print the error if one occurred
		  console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
		  console.log('body:', body);
	  res.send('Response form Chain Boot --> Node --> Python  '+ body );	  
	});
	
})

var server = app.listen(4000, function () {
   var host = server.address().address
   var port = server.address().port
   
   console.log("Example app listening at http://%s:%s", host, port)
})