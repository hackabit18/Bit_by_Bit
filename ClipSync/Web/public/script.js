var firstTime=true;


function loadPosts(){
  checkIfDeviceRegistered();
	var refString="post";
	var query1 = firebase.database().ref(refString);

    var firstPromise=new Promise((resolve,reject)=>{

      query1.once("value",function(snapshot) {

      numberOfChildren = snapshot.numChildren(); 

      if(numberOfChildren!=null)
        resolve();


    }).then(()=>{

    var numberOfPostsLoaded=0;

    var secondPromise=new Promise((resolve,reject)=>{
    query1.on('child_added', function(childSnapshot, prevChildKey) {

    currentPostKey=childSnapshot.key;
    currentPost=childSnapshot.val();
    numberOfPostsLoaded++;

       $( ".posts-container" ).append('<button class="btn btn-primary" id="'+currentPostKey+'" style="border-radius: 500px;margin-bottom: 5px;">'+currentPost+'</button><br>');

      if(numberOfPostsLoaded==numberOfChildren) 
        {
          resolve();
        }
    });
  }).then(()=>{

    console.log("Successfully Completed");
  });

  });
  });
}

function paste(){
	var content=document.getElementById("postContainer").value;

	if(content||false)
	{
		var ref = firebase.database().ref("post");
    ref.once("value")
    .then(function(snapshot) {

        return firebase.database().ref('post').push(content).then(()=>{
            alert("Content Copied to Database");
          })
        .catch(function(error) {
            alert("Unfortunately there was an error, please try again");
          });
      });
	}
}

function clearAll(){
	var adaRef = firebase.database().ref('post/-LPmZpq-yvo4c7FSPjJJ');
		adaRef.remove().then(function() {
    		alert("All your history has been deleted !");
    		window.location.reload();
  })
  .catch(function(error) {
    console.log("Remove failed: " + error.message);
    alert("Unfortunately couldn't Clear All Data");
  });
}

function detectDevice(){

//For Detecting the Browser

var nVer = navigator.appVersion;
var nAgt = navigator.userAgent;
var browserName  = navigator.appName;
var fullVersion  = ''+parseFloat(navigator.appVersion); 
var majorVersion = parseInt(navigator.appVersion,10);
var nameOffset,verOffset,ix;

// In Opera, the true version is after "Opera" or after "Version"
if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
 browserName = "Opera";
 fullVersion = nAgt.substring(verOffset+6);
 if ((verOffset=nAgt.indexOf("Version"))!=-1) 
   fullVersion = nAgt.substring(verOffset+8);
}
// In MSIE, the true version is after "MSIE" in userAgent
else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
 browserName = "Microsoft Internet Explorer";
 fullVersion = nAgt.substring(verOffset+5);
}
// In Chrome, the true version is after "Chrome" 
else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
 browserName = "Chrome";
 fullVersion = nAgt.substring(verOffset+7);
}
// In Safari, the true version is after "Safari" or after "Version" 
else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
 browserName = "Safari";
 fullVersion = nAgt.substring(verOffset+7);
 if ((verOffset=nAgt.indexOf("Version"))!=-1) 
   fullVersion = nAgt.substring(verOffset+8);
}
// In Firefox, the true version is after "Firefox" 
else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
 browserName = "Firefox";
 fullVersion = nAgt.substring(verOffset+8);
}
// In most other browsers, "name/version" is at the end of userAgent 
else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < 
          (verOffset=nAgt.lastIndexOf('/')) ) 
{
 browserName = nAgt.substring(nameOffset,verOffset);
 fullVersion = nAgt.substring(verOffset+1);
 if (browserName.toLowerCase()==browserName.toUpperCase()) {
  browserName = navigator.appName;
 }
}
// trim the fullVersion string at semicolon/space if present
if ((ix=fullVersion.indexOf(";"))!=-1)
   fullVersion=fullVersion.substring(0,ix);
if ((ix=fullVersion.indexOf(" "))!=-1)
   fullVersion=fullVersion.substring(0,ix);

majorVersion = parseInt(''+fullVersion,10);
if (isNaN(majorVersion)) {
 fullVersion  = ''+parseFloat(navigator.appVersion); 
 majorVersion = parseInt(navigator.appVersion,10);
}

// This script sets OSName variable as follows:
// "Windows"    for all versions of Windows
// "MacOS"      for all versions of Macintosh OS
// "Linux"      for all versions of Linux
// "UNIX"       for all other UNIX flavors 
// "Unknown OS" indicates failure to detect the OS

var OSName="Unknown OS";
if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX";
if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";

var deviceName=browserName+'-V'+majorVersion+'-'+OSName;
return deviceName;
}

function checkIfDeviceRegistered(){
  var deviceName=detectDevice();

  var ref = firebase.database().ref("devices");
  ref.once("value")
  .then(function(snapshot) {
    var deviceIsRegistered = snapshot.hasChild(deviceName);

    if(!deviceIsRegistered)
    {
      ref.once("value")
      .then(function(snapshot) {

        return firebase.database().ref('devices').child(deviceName).set('online')
.then(()=>{
            alert("New Device Registered");
          })
        .catch(function(error) {
            alert("Unfortunately there was an error, please try again");
          });
      });
    } 
  });

}

function displaySyncedDevices(){
  var refString="devices";

  var query1 = firebase.database().ref(refString);

    var firstPromise=new Promise((resolve,reject)=>{

      query1.once("value",function(snapshot) {

      numberOfChildren = snapshot.numChildren(); 

      if(numberOfChildren!=null)
        resolve();


    }).then(()=>{

    var numberOfPostsLoaded=0;

    var secondPromise=new Promise((resolve,reject)=>{
    query1.on('child_added', function(childSnapshot, prevChildKey) {

    currentPostKey=childSnapshot.key;
    currentPost=childSnapshot.val();
    

    if(firstTime)
    {
      if(!firstTime)
      {
        $( ".modal-body" ).html('<h5></h5>');
        firstTime=true;
      }
       $( ".modal-body" ).append('<h5>'+currentPostKey+' : '+currentPost+'</h5>');
       numberOfPostsLoaded++;
       if(numberOfChildren==numberOfPostsLoaded)
       {
          firstTime=false;
          resolve();
       }
    }

      if(numberOfPostsLoaded==numberOfChildren) 
        {
          resolve();
        }
    });
  }).then(()=>{

    console.log("Successfully Completed");
  });

  });
  });
}
