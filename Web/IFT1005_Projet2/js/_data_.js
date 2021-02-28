// ======================================================
// js / _data_.js
// ======================================================

// Helpers
// ======================================================

var info = function(msg) { 
  console.log("%c > "+msg, "color:#03a9f4;"); 
};

// Local Storage
// ======================================================

// To save resources (time & computations), we will try to
// retrieve the data from our local storage (if it exists).
var storedData = localStorage.getItem('data');
var data = storedData ? JSON.parse(storedData) : null; 

// Fetch Data
// ======================================================

var url = "https://api.jsonbin.io/b/5c951b129c83133c027b0810";

/**
 * Fetch data from the above url, if we didn't found our  
 * in the cookie object.
 */
if(data === null) {
  $.ajax({
    url: url,
    type: "GET",
    contentType: "application/json; charset=utf-8",
    dataType: 'json',
  })
    .done(function(result) {
      // Save data in global variable.
      data = result;
      // Save data in the local storage to save resources 
      // on the next reload.
      localStorage.setItem('data', JSON.stringify(result));
      info("SAVED DATA IN LOCAL STORAGE");
      // Execute dynamic actions to update the DOM.
      dynamicActions(data);
    })
    .catch(function() {
      console.error("ERROR IN AJAX REQUEST");
    });
} else {
  info("DATA LOADED FROM LOCAL STORAGE");
  dynamicActions(data);
}