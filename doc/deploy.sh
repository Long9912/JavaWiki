#!/bin/bash
echo "---publish---"

process_id=`ps -ef | grep JavaWiki.jar | grep -v grep |awk '{print $2}'`
if [ $process_id ] ; then
sudo kill -9 $process_id
fi

source /etc/profile
nohup java -jar -Dspring.profiles.active=prod ~/wiki/JavaWiki.jar > /dev/null 2>&1 &
redis-server /usr/local/redis/bin/conf/redis.conf
service nginx start

echo "end publish"
