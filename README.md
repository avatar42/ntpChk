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

15:55:50.337 [main] ERROR SntpTest - pfsense.pool.ntp.org:[---- SNTP request failed for pfsense.pool.ntp.org]<br>
15:55:50.360 [main] ERROR SntpTest - opnsense.pool.ntp.org:[---- SNTP request failed for opnsense.pool.ntp.org]<br>
15:55:50.549 [main] ERROR SntpTest - 192.168.0.1:[Invalid response from NTP server. Root delay violation 190.5975341796875]<br>
15:55:51.575 [main] ERROR SntpTest - 192.168.0.14:[---- SNTP request failed for 192.168.0.14]<br>
15:55:52.578 [main] ERROR SntpTest - 10.10.2.197:[---- SNTP request failed for 10.10.2.197]<br>
15:55:52.744 [main] ERROR SntpTest - amazon.pool.ntp.org:[---- SNTP request failed for amazon.pool.ntp.org]<br>
15:55:53.042 [main] WARN  SntpTest - ---------------------------------------<br>
15:55:53.051 [main] WARN  SntpTest - Average offset from 10 good responders:-70 millisecs<br>
15:55:53.051 [main] WARN  SntpTest - host:	offset:	Deviation<br>
15:55:53.051 [main] WARN  SntpTest - pool.ntp.org:	-66:	4<br>
15:55:53.052 [main] WARN  SntpTest - time-a-g.nist.gov:	-64:	6<br>
15:55:53.066 [main] WARN  SntpTest - 10.10.0.1:	-75:	-5<br>
15:55:53.066 [main] WARN  SntpTest - 192.168.0.63:	-71:	-1<br>
15:55:53.066 [main] WARN  SntpTest - time.google.com:	-76:	-6<br>
15:55:53.067 [main] WARN  SntpTest - time.facebook.com:	-72:	-2<br>
15:55:53.067 [main] WARN  SntpTest - time.cloudflare.com:	-71:	-1<br>
15:55:53.077 [main] WARN  SntpTest - time.apple.com:	-66:	4<br>
15:55:53.077 [main] WARN  SntpTest - tick.usno.navy.mil:	-69:	1<br>
15:55:53.078 [main] WARN  SntpTest - time.windows.com:	-76:	-6<br>
