benCoding.Android.Tools

A collection of utilities designed to make working with Titanium on Android alittle easier.

<h1>BootReceiver</h1>
The BootReceiver proxy allows you to subscribe to the BOOT_COMPLETED broadcast and perform specific events upon receipt.

<h2>Features</h2>

* On BOOT_COMPLETED restart your application in the foreground or background
* On BOOT_COMPLETED create a notification with information defined in your project's tiapp.xml
* Use Titanium Properties to configure a BOOT_COMPLETED action you wish to be taken.

<h2>Module Setup</h2>

* Download the latest release from the releases folder ( or you can build it yourself )
* Install the bencoding.sms module. If you need help here is a "How To" [guide](https://wiki.appcelerator.org/display/guides/Configuring+Apps+to+Use+Modules). 
* You can now use the module via the commonJS require method, example shown below.

<h2>How does it work?</h2>
The BootReceiver is configured to work similar to how a native app would when it receives the BOOT_COMPLETED broadcast.  Since Titanium might not yet be loaded, the module will help bootstrap your app based on the configuration elements in your tiapp.xml.

<u><strong>Warning:</strong></u> This functionality requires you update your tiapp.xml file with a few specific elements. I’ve included samples for each scenario, but please plan on spending alittle time exploring in order to get the configurations for your app working properly.


<h2>Finding the correct tiapp.xml entries</h2>
The below steps cover how to create the tiapp.xml entries needed for this module to work.

* Before installing the module, you will want to build your project for the simulator.  It doesn’t matter if the app is empty or event runs. The goal is to simply have Titanium generate a AndroidManifest.xml file.  You can find this file in your Project/build/android folder.
* To avoid the 2373 restart bug, you will need to add the following properties into your tiapp.xml file. 

```xml
    <property name="ti.ui.defaultunit" type="string">system</property>
    <property name="ti.android.bug2373.finishfalseroot" type="bool">true</property>
    <property name="ti.android.bug2373.disableDetection" type="bool">true</property>
    <property name="ti.android.bug2373.restartDelay" type="int">500</property>
    <property name="ti.android.bug2373.finishDelay" type="int">0</property>
    <property name="ti.android.bug2373.skipAlert" type="bool">true</property>
    <property name="ti.android.bug2373.message">Initializing</property>
    <property name="ti.android.bug2373.title">Restart Required</property>
    <property name="ti.android.bug2373.buttonText">Continue</property>
```

Using the information from step #1’s AndroidManifest.xml add an android configuration node to your tiapp.xml.  The below snippet show the tiapp.xml configuration node for a sample app, make sure you update this template with the correct variables from your project.

```xml
    <android
        android:permission="android.permission.RECEIVE_BOOT_COMPLETED" xmlns:android="http://schemas.android.com/apk/res/android">
        <manifest>
            <supports-screens android:anyDensity="false"/>
            <application>
                <activity android:alwaysRetainTaskState="true"
                    android:configChanges="keyboardHidden|orientation"
                    android:label="RandomTest"
                    android:launchMode="singleTop"
                    android:name=".RandomtestActivity"
                    android:persistent="true" android:theme="@style/Theme.Titanium">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN"/>
                        <category android:name="android.intent.category.LAUNCHER"/>
                    </intent-filter>
                </activity>
				<service android:name="bencoding.android.services.MainMenuService" ></service>
            </application>
			<uses-permission android:name="android.permission.WAKE_LOCK" />
            <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
        </manifest>
    </android>
```


(!) Make sure to add the two permission lines show above.

<h3>Scenario 1: Restart</h3>
The first scenario supported by the module is to restart your Titanium app upon receipt of the BOOT_COMPLETED broadcast.

The following shows how to add a receiver entry in your tiapp.xml to use the BootReceiver module.

```xml
<android 
    android:permission="android.permission.RECEIVE_BOOT_COMPLETED" 
    ...
            <application>
                <activity android:alwaysRetainTaskState="true"
                ...
                </activity>
  		     ...
                <receiver android:exported="true" android:name="bencoding.android.receivers.BootReceiver">
                    <intent-filter>
                        <action android:name="android.intent.action.BOOT_COMPLETED"/>
                        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
                    </intent-filter>

                    <meta-data android:name="bootType" android:value="restart"/>
                    <meta-data android:name="sendToBack" android:value="true"/>
                </receiver>

            </application>
            <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
        </manifest>
</android>
```
<h4>Meta-Data Elements</h4>
* bootType – This meta-data element is required and tells the module which action to take. By using the restart option, the module will restart your application upon receipt of the BOOT_COMPLETED broadcast.
* sendToBack - This meta-data element is required if using the bootType of restart.  If true, your app will be restarted in the background upon receipt of the BOOT_COMPLETED broadcast. If false, your app will be restarted in the foreground and the user will be presented with the first window of your app.

Please see the sample [tiapp.xml](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Restart/tiapp.xml) and [app.js](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Restart/app.js) for the full example files.


<h3>Scenario 2: Notification</h3>
The second scenario supported by the module is publish a notification upon receipt of the BOOT_COMPLETED broadcast.

The following shows how to add a receiver entry in your tiapp.xml to use the BootReceiver module.
```xml
<android 
    android:permission="android.permission.RECEIVE_BOOT_COMPLETED" 
    ...
            <application>
                <activity android:alwaysRetainTaskState="true"
                ...
                </activity>
    	     ...
                <receiver android:exported="true" android:name="bencoding.android.receivers.BootReceiver">
                    <intent-filter>
                        <action android:name="android.intent.action.BOOT_COMPLETED"/>
                        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
                    </intent-filter>

                    <meta-data android:name="bootType" android:value="notify"/>
                    <meta-data android:name="title" android:value="Title Sample from tiapp.xml"/>
                    <meta-data android:name="message" android:value="Message Sample from tiapp.xml"/>
                </receiver>

            </application>
            <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
        </manifest>
</android>
```

<h4>Meta-Data Elements</h4>
* bootType – This meta-data element is required and tells the module which action to take. By using the notify option, the module will publish a notification using the title and message properties defined in the tiapp.xml receipt of the BOOT_COMPLETED broadcast.
* title - This meta-data element is required if using the bootType of notify.  The title is used to create the title for the notification that will be published after receipt of the BOOT_COMPLETED broadcast.
* message - This meta-data element is required if using the bootType of notify.  The message is used to create the message body for the notification that will be published after receipt of the BOOT_COMPLETED broadcast.


Please see the sample [tiapp.xml](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Notify/tiapp.xml) and [app.js](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Notify/app.js) for the full example files.

<h3>Scenario 3: Using Properties</h3>
Allowing for the app at runtime to how to handle the BOOT_COMPLETED broadcast allows for handling more complex use cases but requires additional setup.  Titanium Properties are mapped to configuration elements in your tiapp.xml.  The value from these specific Titanium properties are then to determine the correct action to be taken upon receiving the BOOT_COMPLETED broadcast.

The following shows how to add a receiver entry in your tiapp.xml to enable the BootReceiver module to use Titanium Properties to direct the action after receipt of a BOOT_COMPLETED broadcast.

```xml
<android 
    android:permission="android.permission.RECEIVE_BOOT_COMPLETED" 
    ...
            <application>
                <activity android:alwaysRetainTaskState="true"
                ...
                </activity>
           ...
                <receiver android:exported="true" android:name="bencoding.android.receivers.BootReceiver">
                    <intent-filter>
                        <action android:name="android.intent.action.BOOT_COMPLETED"/>
                        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
                    </intent-filter>

                    <meta-data android:name="bootType" android:value="propertyBased"/>
                    <meta-data
                        android:name="enabled_property_to_reference" android:value="my_enabled"/>
                   <meta-data
                        android:name="bootType_property_to_reference" android:value="my_bootType"/>
                   <meta-data
                        android:name="sendToBack_property_to_reference" android:value="my_sendtoback"/>
                   <meta-data android:name="icon_property_to_reference" android:value="my_notify_icon"/>
                   <meta-data
                        android:name="title_property_to_reference" android:value="my_notify_title"/>
                   <meta-data
                        android:name="message_property_to_reference" android:value="my_notify_message"/>
                </receiver>

            </application>
            <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
        </manifest>
</android>
```

<h4>Meta-Data Elements</h4>
* bootType – This meta-data element is required and tells the module which action to take. By using the propertyBased option, the module will look at the following properties to determine which action to take.
* enabled_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to see if this feature has been enabled. This property must contain a Boolean value and is false by default. 
* bootType_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to see what action to perform. Just like the primary bootType element, you can use restart or notify to perform the desired actions. Please note all configuration elements such as title, message, sendToBack will be read from the Titanium Properties mapped in your tiapp.xml file. 
* sendToBack_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to see if the app should be restarted in the foreground or background.. This property must contain a Boolean value and is true by default. Please note: This property is only used if the Titanium Property define in bootType_property_to_reference is set to restart. 
* icon_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to determine which Android Resource Id to use when creating the icon for the notification created on receipt of the BOOT_COMPLETED broadcast.  Please note: This property is only used if the Titanium Property define in bootType_property_to_reference is set to notify. 
* title_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to determine which Android Resource Id to use when creating the title for the notification created on receipt of the BOOT_COMPLETED broadcast.  Please note: This property is only used if the Titanium Property define in bootType_property_to_reference is set to notify. 
* message_property_to_reference – This android:value element contains a reference to the Titanium Property the module will reference to determine which Android Resource Id to use when creating the message for the notification created on receipt of the BOOT_COMPLETED broadcast.  Please note: This property is only used if the Titanium Property define in bootType_property_to_reference is set to notify. 

Please see the sample [tiapp.xml](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Properties/tiapp.xml) and [app.js](https://github.com/benbahrenburg/benCoding.Android.Tools/blob/master/example/BootReceiver/Properties/app.js) for the full example files.


<h2>Any examples?</h2>
Usage examples are available on Github at [here](https://github.com/benbahrenburg/benCoding.Android.Tools/tree/master/example).

