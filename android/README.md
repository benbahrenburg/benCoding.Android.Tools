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
* Install the bencoding.android.tools module. If you need help here is a "How To" [guide](https://wiki.appcelerator.org/display/guides/Configuring+Apps+to+Use+Modules). 
* You can now use the module via the commonJS require method, example shown below.

<pre><code>
var tools = require('bencoding.android.tools');
</code></pre>


<h2>Platform Methods</h2>
The platform methods proxy provides access to several helpful Android apis not currently exposed in the core Titanium SDK.

To learn more about these please visit the documentation [here](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/documentation/platform.md) or the examples [here](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/example/platform).

<h2>BootReceiver</h2>
The BootReceiver proxy provides extended functionality for handling the BOOT_COMPLETED system broadcast.

To learn more about these please visit the documentation [here](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/documentation/bootreceiver.md) or the examples [here](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/example/BootReceiver).

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
