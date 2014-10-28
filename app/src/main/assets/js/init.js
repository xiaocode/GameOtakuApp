var DuduluJSBridgeReady = function() {
	window.DuduluJSBridge.scriptOnLoad();
};

var JSBridgeReadyEvent = document.createEvent('HTMLEvents');
JSBridgeReadyEvent.initEvent('DuduluJSBridgeReady', false, false);
document.addEventListener('DuduluJSBridgeReady', DuduluJSBridgeReady, false);