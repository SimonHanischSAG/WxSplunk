# WxSplunk
This package encapsulate the process of sending your log messages as events to Splunk in an asynchronous and performant way. 

For better performance it buffers the log messages in a queue in memory. A thread in background works through the queue and sending the events in batches to Splunk (DO NOT KILL THIS THREAD!).
Between the batches this "continuousSplunkLoggerThread" will sleep a period of time. This time is depending on the load and the min and max which is configurable.

It is designed for usage together with the official packages WxConfig (or the free alternative https://github.com/SimonHanischSAG/WxConfigLight) and optionally together with the official packages WxLog or WxLog2.

MANY THANKS TO LIDL AND SCHWARZ IT, who kindly allowed to provide the template for this package and make it public.

<h2>How to use</h2>

<h3>Deploy/checkout WxSplunk</h3>

Check under releases for a proper release and deploy it. Otherwise you can check out the latest version from GIT and create a link like this:

mklink /J F:\\SoftwareAG\\IntegrationServer\\instances\\default\\packages\\WxSplunk F:\\GIT-Repos\\WxSplunk\\packages\\WxSplunk

For demonstration purpose you can also link the Test-package which simulates the Splunk endpoint:
mklink /J F:\\SoftwareAG\\IntegrationServer\\instances\\default\\packages\\WxSplunk_Test F:\\GIT-Repos\\WxSplunk\\packages\\WxSplunk_Test

<h4>Build & Reload</h4>

If you checkout the sources from GitHub you have to compile the source, e.g. with:

C:\SoftwareAG\IntegrationServer\instances\default\bin\jcode.bat makeall WxSplunk

Reload WxSplunk

<h3>Configure Environment</h3>

You have to configure WxSplunk in ../../../config/packages/WxSplunk/wxconfig-<environment>.cnf (example when you are using the simulation in WxSplunk_Test. 
In that case you have to adjust the permissions of wx.splunk.ws:_post e.g. to Anonymous):

splunk.logging.url=http://localhost:5555/rest/wx.splunk.ws
splunk.logging.token=Splunk XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
splunk.logging.enabled=true

Reload WxSplunk. The startup will start the "continuousSplunkLoggerThread" with that configuration. Check the server.log for:
  
2022-06-29 09:38:00 CEST [ISS.0028.0012I] (tid=86) WxSplunk: Startup service (wx.splunk.admin:startUp) 
2022-06-29 09:38:01 CEST [ISP.0090.0004I] (tid=86) WxSplunk -- startSplunkLoggerThread: Started 
2022-06-29 09:38:01 CEST [ISP.0090.0004I] (tid=86) WxSplunk -- continuousSplunkLoggerThread: Thread started 

CONSIDER THAT ALL CONFIG VALUES ARE CACHED except splunk.logging.enabled which is checked for each log statement.

<h3>Test Configuration</h3>

You can invoke wx.splunk.test:sendEventDirectlyToSplunk in order to test your configuration directly. 
  
Furthermore you can invoke wx.splunk.test:testContinuousSplunkLogger in order to test the continuousSplunkLogger
<h3>Advanced</h3>

Check WxSplunk_Test/wx.splunk.pub:test_log_with_more_fields and see how you can improve your logging. Consider that outside from event and fields you should not add 
your own fields as otherwise Splunk will drop your event (compare with https://docs.splunk.com/):
<ul>
  <li>source: Where is the event from?</li>
  <li>sourcetype: Which type has this event?</li>
  <li>event: Add your own variables and documents under "event" in order to see it in Splunk in preview (searchable)</li>
  <li>fields: Add your own variables under "fields" in order to have additional fields in Splunk (searchable)</li>
</ul>

