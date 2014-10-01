
benCoding.Android.Tools

A collection of utilities designed to make working with Titanium on Android alittle easier.

<h1>Platform</h1>
The Platform proxy provides access to several helpful Android APIs that are currently not available in the Titanium SDK.

<h2>Setup</h2>

* Download the latest release from the releases folder ( or you can build it yourself )
* Install the bencoding.sms module. If you need help here is a "How To" [guide](https://wiki.appcelerator.org/display/guides/Configuring+Apps+to+Use+Modules). 
* You can now use the module via the commonJS require method, example shown below.

<pre><code>
var tools = require('bencoding.android.tools');
</code></pre>

<h2>Methods</h2>

<b>intentAvailable</b>( Ti.Android.Intent )
This method checks if there is an app on your device that can handle a specific intent.

<b>Parameters</b>
intent : Ti.Android.Intent
Ti.Android.Intent to be used in checking for availability.

<b>Returns</b>
Boolean

<b>Example</b>
<pre><code>
	var platformTools = tools.createPlatform();
	var intent = Ti.Android.createIntent({
		action: Ti.Android.ACTION_VIEW,
		type: "application/pdf",
		data: session.tempFile.nativePath
	});

	if(platformTools.intentAvailable(intent)){
		try {
			Ti.Android.currentActivity.startActivity(intent);
		} catch(e) {
			Ti.API.debug(e);
			alert('Something went wrong');
		}
	}else{
		alert("Please go to the Google Play Store and download a PDF reader");			
	}

</code></pre>

----

<b>restartApp</b>
This method restarts your Titanium app

<b>Parameters</b>
None

<b>Returns</b>
None

<b>Example</b>
<pre><code>
	var platformTools = tools.createPlatform();
	//Restarts your Titanium App
	platformTools.restartApp();

</code></pre>

----

<b>exitApp</b>
This method closes your Titanium app

<b>Parameters</b>
None

<b>Returns</b>
None

<b>Example</b>
<pre><code>
	var platformTools = tools.createPlatform();
	//Restarts your Titanium App
	platformTools.exitApp();

</code></pre>

----

<b>isAirplaneModeOn</b>
This method returns a boolean if the device is in airplane mode

<b>Parameters</b>
None

<b>Returns</b>
Boolean (true/false)

<b>Example</b>
<pre><code>
	var platformTools = tools.createPlatform();
	var onPlane = platformTools.isAirplaneModeOn();
	alert('Your device is in airplane mode: ' + (onPlane ? 'Yes' :'No'));

</code></pre>

----

<b>getSystemDateTime</b>
This method returns an object representing the current time on the device, useful when you need to know if the timezone has changed, since the JavaScript engine will often initialize this value up front and never check the OS.

<b>Parameters</b>
None

<b>Returns</b>
HashMap containing the following keys:
<ul>
	<li>TZOffset: The timezone offset of the device, in minutes.</li>
	<li>Year</li>
	<li>Month</li>
	<li>Day</li>
	<li>Hour</li>
	<li>Minutes</li>
	<li>Seconds</li>
	<li>Date: In the format yyyy/MM/ss HH:mm:ss</li>
</ul>

----
