
benCoding.Android.Tools

A collection of utilities designed to make working with Titanium on Android alittle easier.

<h2>Before you start</h2>
* This is an Android only module
* Before using this module you first need to install the package. If you need instructions on how to install a 3rd party module please read this installation guide.

<h2>Download the release</h2>

The go to the [dist](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/dist) folder. This will have a release compiled for anyone download it from github.


<h2>Building from source?</h2>

If you are building from source you will need to do the following:
* Modify your build.properties to reflect the path to your android and other directors
* You might need to update your .classpath depending on your setup.

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

<h2>Licensing & Support</h2>

This project is licensed under the OSI approved Apache Public License (version 2). For details please see the license associated with each project.

Developed by [Ben Bahrenburg](http://bahrenburgs.com) available on twitter [@benCoding](http://twitter.com/benCoding)

<h2>Learn More</h2>

<h3>Examples</h3>
Please check the module's example folder or view the [sample on github](https://github.com/benbahrenburg/benCoding.Android.Extras/tree/master/example) .


<h3>Twitter</h3>

Please consider following the [@benCoding Twitter](http://www.twitter.com/benCoding) for updates 
and more about Titanium.

<h3>Blog</h3>

For module updates, Titanium tutorials and more please check out my blog at [benCoding.Com](http://benCoding.com).