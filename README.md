![ScreenShot](https://github.com/pubnative/pubnative-ios-library/blob/master/PNLogo.png)

PubNative is an API-based publisher platform dedicated to native advertising which does not require the integration of an Library.
Through PubNative, publishers can request over 20 parameters to enrich their ads and thereby create any number of combinations for unique and truly native ad units.

PubNative Android Library simplifies getting ad images, texts and sending confirmation.\n
PubNative Interstitials provides ready formats & widgets .

Requirements
============
* Android 4.0+.
* An App Token provided in PubNative Dashboard.
* The following permissions in AndroidManifest.xml:

   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> (optional)
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> (optional)
   <uses-permission android:name="android.permission.READ_PHONE_STATE" /> (optional)
   
Getting Started
===============
Clone the repository and import
* pubnative-library if you need to create custom ad views
* pubnative-interstitials to use the pre-built formats.

PubNative Library
=============
PubNative Library provides a simple Java API to get, show and confirm ads.\n
The basic principle is that you create special objects containing references to Views that should be populated (e.g. banner image, app title, etc.) and ask PubNative to fill them.
After the Views are filled in and displayed, an impression confirmation is to PubNative server.
Then you need to handle a click on the View, but there's a helper for that.

Setup
-----
Add the following inside an Activity that will show ads:
```
	@Override
	protected void onPause() {
		super.onPause();
		PubNative.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		PubNative.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PubNative.onDestroy();
	}
```

Key Classes
-----------
* ```AdRequest``` - contains request data including device ids, image size.
It's created using the ```public AdRequest(String appToken, AdFormat format)```. AdFormat is either NATIVE or IMAGE.
Calling ```public void fillInDefaults(Context ctx)``` will make a request ready to use.
* ```ImageAdHolder```, ```NativeAdHolder``` - hold references to Views that should be populated.
* ```PubNative``` - the main class used to make the calls.
    * ```public static void showAd(AdRequest req, AdHolder<?>... holders)``` - execute the AdRequest and fill in the provided AdHolders.
    * ```public static void showAd(String appToken, AdHolder<?>... holders)``` - same as previous but automatically creates AdRequests.
    * ```public static void setListener(PubNativeListener listener)``` - sets a listener that will be notified on events.
    * ```public static void showInPlayStore***(Activity act, Ad ad)``` - displays an an in the Play Store.

Simple ImageAd Example
----------------------
1. Create an instance of ImageAdHolder, reference an ImageView.
2. Call PubNative.showAd("your_app_id", imageAdHolder).

Advanced NativeAd Example
-------------------------
1. Create several NativeAdHolder instances with references to the Views required.
2. Create an instance of AdRequest specifying app id. Call fillInDefaults(Context ctx) on it.
   Use setParam(String key, String val) to set any custom values.
3. Call PubNative.showAd(AdRequest req, AdHolder<?>... holders).

For more examples, examine the code of pubnative-library-tester.

Pubnative Interstitials
=======================

The class used to display interstitials is ```PubNativeInterstitials```.

Setup
-----
* Declare the following Activity in you app's AndroidManifest.xml:
```
<activity
            android:name="net.pubnative.interstitials.PubNativeInterstitialsActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:taskAffinity="net.pubnative.interstitials"
            android:theme="@style/Theme.PubNativeInterstitials" />
```
* Call public static void init(Context ctx, String appKey) before calling any other PubNativeInterstitials methods.

Usage
-----
Calling ```public static void show(Activity act, PubNativeInterstitialsType type, int adCount)```
where ```PubNativeInterstitialsType``` can be ```INTERSTITIAL```, ```NATIVE```, ```LIST``` or ```CAROUSEL```
will display an Activity with an ad of appropriate format.

Please check out pubnative-interstitials-tester for code examples.


ProGuard
========
If using ProGuard, please include pubnative-proguard.cfg.