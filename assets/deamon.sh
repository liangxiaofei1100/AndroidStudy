#!/bin/sh

/system/bin/am startservice -n com.zhaoyan.floatwindowdemo/com.zhaoyan.floatwindowdemo.FloatingService
logcat -v time -d > /data/data/com.example.alex/files/log2.txt
sleep 5;

while true
do
/system/bin/am startservice -n com.zhaoyan.floatwindowdemo/com.zhaoyan.floatwindowdemo.FloatingService
echo 222 >> /data/data/com.example.alex/files/log.txt
sleep 5;
done