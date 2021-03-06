/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package bencoding.android.tools;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;

import bencoding.android.Common;


@Kroll.module(name="Androidtools", id="bencoding.android.tools")
public class AndroidtoolsModule extends KrollModule
{
	public static final String MODULE_FULL_NAME = "bencoding.android.tools";
	
	public AndroidtoolsModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
	}
	
	@Kroll.method
	public void disableLogging()
	{
		Common.setDebug(false);
	}
	@Kroll.method
	public void enableLogging()
	{
		Common.setDebug(true);
	}	
}

