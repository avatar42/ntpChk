# ntpChk
Quick check getting responses from ntp servers and see the deviation between them 

## config Set time out and servers to poll in servers.properties
timeoutInMillis=1000
g1=time.google.com
l1=10.10.0.1
l2=192.168.0.1
l3=192.168.0.14
l4=192.168.0.63
l5=10.10.2.197
c1=time.cloudflare.com
f1=time.facebook.com
w1=time.windows.com
a1=time.apple.com
n1=time-a-g.nist.gov
np=pool.ntp.org
op=opnsense.pool.ntp.org
ap=amazon.pool.ntp.org
pp=pfsense.pool.ntp.org
mil1=tick.usno.navy.mil

## sample output

<br>08:50:01.590 [main] ERROR SntpTest - opnsense.pool:opnsense.pool.ntp.org:[---- SNTP request failed for opnsense.pool.ntp.org]
<br>08:50:01.603 [main] ERROR SntpTest - amazon.pool:amazon.pool.ntp.org:[---- SNTP request failed for amazon.pool.ntp.org]
<br>08:50:02.603 [main] ERROR SntpTest - DNS2:192.168.0.14:[---- SNTP request failed for 192.168.0.14]
<br>08:50:03.653 [main] ERROR SntpTest - CentosLan2:192.168.2.197:[---- SNTP request failed for 192.168.2.197]
<br>08:50:03.694 [main] ERROR SntpTest - pfsense.pool:pfsense.pool.ntp.org:[---- SNTP request failed for pfsense.pool.ntp.org]
<br>08:50:03.775 [main] ERROR SntpTest - OPNsense:192.168.0.1:[Invalid response from NTP server. Root delay violation 132.4920654296875]
<br>08:50:04.109 [main] ERROR SntpTest - ntp.pool:pool.ntp.org:[Server response delay too large for comfort 210]
<br>08:50:05.148 [main] ERROR SntpTest - Homeseer:10.10.1.45:[---- SNTP request failed for 10.10.1.45]
<br>08:50:06.148 [main] ERROR SntpTest - Iris5:10.10.2.45:[---- SNTP request failed for 10.10.2.45]
<br>08:50:06.150 [main] ERROR SntpTest - Iris6:10.10.2.48:[Invalid response from NTP server. Root delay violation 132.99560546875, Invalid response from NTP server. Root dispersion violation 2752.62451171875]
<br>08:50:07.151 [main] ERROR SntpTest - Iris3:10.10.2.46:[---- SNTP request failed for 10.10.2.46]
<br>08:50:08.164 [main] ERROR SntpTest - Iris4:10.10.2.47:[---- SNTP request failed for 10.10.2.47]
<br>08:50:09.166 [main] ERROR SntpTest - CentosLan10:10.10.2.197:[---- SNTP request failed for 10.10.2.197]
<br>08:50:09.166 [main] WARN  SntpTest - ---------------------------------------
<br>08:50:09.166 [main] WARN  SntpTest - Average offset from 8 good responders:-83 millisecs
<br>08:50:09.175 [main] WARN  SntpTest - name:	host:	offset:	Deviation
<br>08:50:09.176 [main] WARN  SntpTest - navy:	tick.usno.navy.mil:	-79:	4
<br>08:50:09.176 [main] WARN  SntpTest - DNS1:	192.168.0.63:	-84:	-1
<br>08:50:09.176 [main] WARN  SntpTest - google:	time.google.com:	-87:	-4
<br>08:50:09.184 [main] WARN  SntpTest - cloudflare:	time.cloudflare.com:	-85:	-2
<br>08:50:09.184 [main] WARN  SntpTest - windows:	time.windows.com:	-85:	-2
<br>08:50:09.184 [main] WARN  SntpTest - nist:	time-a-g.nist.gov:	-83:	0
<br>08:50:09.185 [main] WARN  SntpTest - apple:	time.apple.com:	-78:	5
<br>08:50:09.194 [main] WARN  SntpTest - gateway:	10.10.0.1:	-85:	-2

## note 192.168.0.1 and 10.10.2.48 turned out the be the most reliable ones for my cameras to use depite errors seen here from a Windows PC.
See https://securitycam101.rmrr42.com/2020/09/syncing-time.html

