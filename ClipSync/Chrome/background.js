chrome.extension.onRequest.addListener(
  function(request, sender, sendResponse) {
    if (request.event == "copy") {
       alert("copy detected");
    }
    sendResponse({});
  });
