# Omnomagon

Omnomagon is a small Android app that shows you the current menu of your university cafeteria - including useful information for allergy sufferers, vegetarians and vegans.

* [PlayStore](https://play.google.com/store/apps/details?id=net.pherth.omnomagon)
* [Website](http://omnomagon.de)

## Dependencies

* Android Support Libraries (compatibility-v4, appcompat-v7, recyclerview-v7)
* [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)
* [jsoup](http://jsoup.org)

## Build (Maven)

0. Install JAVA and set your  ````$JAVA_HOME ```` environment variable

1. Download [Android SDK](http://developer.android.com/sdk/index.html). Don’t forget to set your  ````$ANROID_HOME ```` environment variable.

2. Install [Android SDK Deployer](https://github.com/simpligility/maven-android-sdk-deployer). You may not have to install all SDK components. Uncomment the unwanted modules in the ````pom.xml```` files.
	
		$ git clone *REPO*
		$ cd maven-android-sdk-deployer/
		$ mvn install

3. Build Omnomagon

		$ git clone *REPO*
		$ cd Omnomagon/
		$ mvn clean
		$ mvn install

	Now an .apk is build under  ````./target ````

4. Install on device or emulator (optional)

		$ mvn android:deploy
		$ mvn android:run

## Licence

Copyright (c) 2015, [Phillip Thelen](https://github.com/vIiRuS), [Tim Nowaczynski](https://github.com/TimNowaczynski), [Peter Amende](https://github.com/zutrinken)  
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## Support

You can also flattr the code: [![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=viirus&url=https://github.com/vIiRuS/omnomagon&title=Omnomagon Android App&language=de_DE&tags=github&category=software)
