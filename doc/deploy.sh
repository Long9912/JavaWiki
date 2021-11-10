#!/bin/bash
export ES_HOME=/usr/local/elasticsearch-7.15.1
echo "---publish---"

#清除旧的java进程
process_id=$(ps -ef | grep JavaWiki.jar | grep -v grep | awk '{print $2}')
if [ $process_id ]; then
  sudo kill -9 $process_id
fi

#更新配置文件
source /etc/profile
#elasticsearch启动
su elastic -c "${ES_HOME}/bin/elasticsearch -d"
#后台运行Java进程
nohup java -jar -Dspring.profiles.active=prod ~/wiki/JavaWiki.jar >/dev/null 2>&1 &
#启动Redis
redis-server /usr/local/redis/bin/conf/redis.conf
#启动Nginx
service nginx start

echo "---end publish---"
