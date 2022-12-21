#基础jre
FROM openjdk:8-jre-alpine as jre

FROM alpine
#MAINTAINER wz <991567775@qq.com>
##jre设置
COPY  --from=jre ["/usr/lib/jvm/java-1.8-openjdk/jre", "/root/jre"]
ENV JAVA_HOME=/root/jre
ENV PATH=$JAVA_HOME/bin:$PATH
#设置目录
ENV dir /root
ENV temp_dir /temp
#运行
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    #jdk 依赖 以及数据库 \
    && apk add     --no-cache su-exec libstdc++ pcre-dev nss  tini libc6-compat linux-pam krb5 krb5-libs\
    #nacos 启动依赖\
    && ln -s /lib64/ld-linux-x86-64.so.2 /lib/ld-linux-x86-64.so.2\
    #添加用户  \
    &&  addgroup -S -g 1000 edp && adduser -S -G edp -u 999 edp \
    #调整时区
    && apk add --no-cache  --virtual .build-tzdata tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && apk del .build-tzdata \
    #安装依赖
    && set -eux; apk add --no-cache --virtual .build-deps  zlib-dev coreutils  dpkg-dev dpkg   gcc  linux-headers  make  musl-dev  openssl-dev  wget \
    #创建工作目录
    && mkdir -p $temp_dir && cd  $temp_dir \
    #安装redis
    && wget https://download.redis.io/redis-stable.tar.gz    &&   tar -xzf redis-stable.tar.gz   &&  rm redis-stable.tar.gz  \
    #禁用 Redis 的保护模式;修改端口为6380
    &&  grep -E '^ *createBoolConfig[(]"protected-mode",.*, *1 *,.*[)],$' $temp_dir/redis-stable/src/config.c; \
        sed -ri 's!^( *createBoolConfig[(]"protected-mode",.*, *)1( *,.*[)],)$!\10\2!' $temp_dir/redis-stable/src/config.c; \
        grep -E '^ *createBoolConfig[(]"protected-mode",.*, *0 *,.*[)],$' $temp_dir/redis-stable/src/config.c; \
        sed -i "s/6379/6380/g" $temp_dir/redis-stable/src/config.c \
    && cd  redis-stable && make -j 10 && make install \
    #安装nginx \
    && cd  $temp_dir   && wget http://nginx.org/download/nginx-1.22.1.tar.gz  &&   tar -xzf nginx-1.22.1.tar.gz &&  rm nginx-1.22.1.tar.gz \
    && cd nginx-1.22.1  && ./configure --user=root  --prefix=/usr/local/nginx  --with-stream_ssl_module --with-stream_realip_module --with-http_stub_status_module --with-http_realip_module --with-http_gzip_static_module --with-stream_ssl_module --with-http_v2_module --with-http_ssl_module \
    && make -j 10 && make install   &&  ln -s /usr/local/nginx/sbin/nginx /usr/sbin/ \
#    # 基于前面安装卸载 .build-deps 虚拟包
    && rm -r $temp_dir \
    &&  apk del --no-network .build-deps
#安装数据mariadb
RUN  apk add --no-cache mariadb mariadb-client && \
    TO_KEEP=$(echo " \
    etc/ssl/certs/ca-certificates.crt$ \
    usr/bin/mariadb$ \
    usr/bin/mariadbd$ \
    usr/bin/getconf$ \
    usr/bin/getent$ \
    usr/bin/mariadb-install-db$ \
    usr/share/mariadb/charsets \
    usr/share/mariadb/english \
    usr/share/mariadb/mysql_system_tables.sql$ \
    usr/share/mariadb/mysql_performance_tables.sql$ \
    usr/share/mariadb/mysql_system_tables_data.sql$ \
    usr/share/mariadb/maria_add_gis_sp_bootstrap.sql$ \
    usr/share/mariadb/mysql_sys_schema.sql$ \
    usr/share/mariadb/fill_help_tables.sql$" | \
    tr -d " \t\n\r" | sed -e 's/usr/|usr/g' -e 's/^.//') && \
    INSTALLED=$(apk info -q -L mariadb-common mariadb mariadb-client linux-pam ca-certificates | grep "\S") && \
    for path in $(echo "${INSTALLED}" | grep -v -E "${TO_KEEP}"); do \
      eval rm -rf "${path}"; \
    done && \
    touch /usr/share/mariadb/mysql_test_db.sql && \
    # this file is removed since we remove most things from mariadb-common
    echo "!includedir /etc/my.cnf.d" > /etc/my.cnf && \
    # allow anyone to connect by default
    sed -ie 's/127.0.0.1/%/' /usr/share/mariadb/mysql_system_tables_data.sql && \
    mkdir /run/mysqld && \
    chown mysql:mysql /etc/my.cnf.d/ /run/mysqld /usr/share/mariadb/mysql_system_tables_data.sql

#nacos xxl-job
COPY gz/xxl-job.jar  $dir/xxl-job.jar
ADD gz/nacos-server-2.2.0.tar.gz  $dir
#数据库相关sh
COPY sh/resolveip.sh /usr/bin/resolveip
COPY sh/my_print_defaults.sh /usr/bin/my_print_defaults
COPY sh/run.sh /run.sh
COPY conf/db.cnf /tmp/my.cnf
#数据库脚本
COPY sql/ /docker-entrypoint-initdb.d/
#启动脚本
COPY sh/start.sh $dir/start.sh
# Below is in my opinion better than no health check.
HEALTHCHECK --start-period=5s CMD pgrep mariadbd
VOLUME ["/var/lib/mysql"]
# 启动
ENTRYPOINT ["/run.sh"]
#暴露对外端口nginx、job、redis  mariadb
EXPOSE 80 443 8080 6380 3307 8848 9848

