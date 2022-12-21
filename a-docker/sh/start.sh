#!/bin/bash
#使用说明，用来提示输入参数
help(){
    echo "ways: sh startup.sh service[redis|nacos|nginx|mysql|job｜all] [start|stop|restart|status]"
    exit 1
}
#检查程序是否在运行
#定义变量
app="";
redis_pid=`ps -e | grep redis |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
nginx_pid=`ps -e | grep nginx |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
nacos_pid=`ps -e | grep nacos |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
mysql_pid=`ps -e | grep mariadb |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
job_pid=`ps -e | grep job |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
#启动
start(){
  echo "[$app] starting"
  #全部启动
  if [ "$app" = 'all' ]; then
    redis-server   >> /root/logs.log 2>&1 &
    nginx -g 'daemon on;'   >> /root/logs.log 2>&1 &
   /usr/bin/mariadbd --user=mysql --skip-name-resolve --skip-host-cache --skip-slave-start  >> /root/logs.log 2>&1 &
    while   true
          do
            #ps -ef | grep mysql | grep -v grep | wc -l
            mysql=`ps -ef | grep mariadb | grep -v grep | wc -l`
            if [ $mysql -eq 0 ]; then
              echo "等待mysql启动";
               sleep 10
            else
              echo "等待mysql初始化或启动";
              echo "job and nacos start"
              sleep 10
              java -jar /root/xxl-job.jar >>  /root/logs.log 2>&1 &
              sh /root/nacos/bin/startup.sh -m standalone >> /root/logs.log 2>&1 &
             break;
            fi
          done
  #redis启动
  elif   [ "$app" = 'redis' ]; then
 	  redis-server  >> /root/logs.log 2>&1 &
 	  sleep 3
 	  redis_pid=`ps -e | grep redis |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
 	  echo "$app-pid-$redis_pid"
 	#job启动
    elif   [ "$app" = 'job' ]; then
   	  java -jar /root/job/xxl-job.jar >>  /root/logs.log 2>&1 &
   	  sleep 3
   	  job_pid=`ps -e | grep job |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
   	  echo "$app-pid-$job_pid"
 	#nginx启动
 	elif [ "$app" = 'nginx' ]; then
 	  nginx -g 'daemon on;'  >> /root/logs.log 2>&1 &
 	  sleep 3
 	  nginx_pid=`ps -e | grep nginx |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
 	  echo "$app-pid-$nginx_pid"
 	#nacos启动
 	elif [ "$app" = 'nacos' ]; then
 	 	sh /root/nacos/bin/startup.sh -m standalone >> /root/logs.log 2>&1 &
 	 	sleep 3
 	 	nacos_pid=`ps -e | grep java |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
 	 	echo "$app-pid-$nacos_pid"
 	#mysql启动
 	elif [ "$app" = 'mysql' ]; then
    /usr/bin/mariadbd --user=mysql --skip-name-resolve --skip-host-cache --skip-slave-start
 	  sleep 3
 	  mysql_pid=`ps -e | grep mariadb |grep -v grep | awk '{print $1}' | grep -v "^$$\$" `
 	  echo "$app-pid-$mysql_pid"
 fi
 echo "[$app] started"
}
#停止
stop(){
  echo "[$app] stopping"
    #全部启动
    if [ "$app" = 'all' ]; then
      echo "kill reids-pid-$redis_pid:nginx-pid-$nacos_pid:naocs-pid-$nacos_pid:mysql-pid-$mysql_pid"
      kill -9 $redis_pid $nginx_pid $nacos_pid $mysql_pid &
    #redis停止
    elif   [ "$app" = 'redis' ]; then
      echo "kill $app-pid-$redis_pid"
      kill -9 $redis_pid
   	#nginx停止
   	elif [ "$app" = 'nginx' ]; then
   	  echo "kill $app-pid-$nginx_pid"
   	  kill -9 $nginx_pid
   	#job停止
    elif [ "$app" = 'job' ]; then
      echo "kill $app-pid-$job_pid"
       kill -9 $job_pid
   	#nacos停止
   	elif [ "$app" = 'nacos' ]; then
   		echo "kill $app-pid-$nacos_pid"
   	 	kill -9 $nacos_pid
   	#mysql停止
   	elif [ "$app" = 'mysql' ]; then
   	  echo "kill $app-pid-$mysql_pid"
   	  kill -9 $mysql_pid
   		#mysql停止
       	elif [ "$app" = 'job' ]; then
       	  echo "kill $app-pid-$mysql_pid"
       	  kill -9 $mysql_pid
   fi
   sleep 3
   echo "[$app] stopped"
}
restart(){
  stop
  sleep 3
  start
}
status(){
  redis_pid=$(ps -aux | grep redis | wc -l)
  nginx_pid=$(ps -aux | grep nginx | wc -l)
  nacos_pid=$(ps -aux | grep nacos | wc -l)
  mysql_pid=$(ps -aux | grep mariadb | wc -l)
  if [ "$app" = 'all' ]; then
    if [ $redis_pid -le 1  ]; then
      echo "redis status stopped"
    else
      echo "redis status started"
    fi
    if [ $nginx_pid -le 1  ]; then
      echo "nginx status stopped"
    else
      echo "nginx status started"
    fi
    if [ $nacos_pid -le 1 ]; then
      echo "nacos status stopped"
    else
      echo "nacos status started"
    fi
     if [ $job_pid -le 1 ]; then
          echo "nacos status stopped"
        else
          echo "nacos status started"
        fi
    if [ $mysql_pid -le 1 ]; then
      echo "mysql status stopped"
    else
      echo "mysql status started"
    fi
  #redis状态
  elif   [ "$app" = 'redis' ]; then
    if [ $redis_pid -le 3 ]; then
      echo "redis status stopped"
    else
      echo "redis status started"
    fi
  #nginx状态
  elif [ "$app" = 'nginx' ]; then
    if [ $nginx_pid -le 3  ]; then
      echo "nginx status stopped"
    else
      echo "nginx status started"
    fi
  #nacos状态
  elif [ "$app" = 'nacos' ]; then
    if [  $nacos_pid -le 3 ]; then
      echo "nacos status stopped"
    else
      echo "nacos status started"
    fi
  #mysql状态
  elif [ "$app" = 'mysql' ]; then
    if [ $mysql_pid -le 3 ]; then
      echo "mysql status stopped"
    else
      echo "mysql status started"
    fi
  fi
}
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$2" in
  "start")
      app=$1;
      start
      if [ "$app" = "all" ]; then
          tail -f /root/logs.log
      fi
    ;;
  "stop")
      app=$1;
      stop
      exit 1
    ;;
  "status")
      app=$1;
      status
      exit 1
    ;;
  "restart")
      app=$1;
      restart
      exit 1
    ;;
  *)
    help
    ;;
esac

