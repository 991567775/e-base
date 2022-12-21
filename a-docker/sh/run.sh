#!/bin/sh
# shellcheck shell=dash
set -eo pipefail

touch /tmp/init

#检查是否使用自己配置
if [ -z "$(ls -A /etc/my.cnf.d/* 2>/dev/null)" ]; then
  cp /tmp/my.cnf /etc/my.cnf.d/
  [ -n "${SKIP_INNODB}" ] || [ -f "/var/lib/mysql/noinnodb" ] &&
    sed -i -e '/\[mariadb\]/a skip_innodb = yes\ndefault_storage_engine = Aria\ndefault_tmp_storage_engine = Aria' \
      -e '/^innodb/d' /etc/my.cnf.d/my.cnf
fi




# No previous installation
if [ -z "$(ls -A /var/lib/mysql/ 2>/dev/null)" ]; then
  [ -n "${SKIP_INNODB}" ] && touch /var/lib/mysql/noinnodb
  [ -f "/run/secrets/MYSQL_ROOT_PASSWORD" ] && MYSQL_ROOT_PASSWORD="$(cat /run/secrets/MYSQL_ROOT_PASSWORD)"
  [ -n "${MYSQL_ROOT_PASSWORD}" ] &&
    echo "set password for 'root'@'%' = PASSWORD('${MYSQL_ROOT_PASSWORD}');" >>/tmp/init
  INSTALL_OPTS="--user=mysql"
  INSTALL_OPTS="${INSTALL_OPTS} --cross-bootstrap"
  INSTALL_OPTS="${INSTALL_OPTS} --rpm"
  # https://github.com/MariaDB/server/commit/b9f3f068
  INSTALL_OPTS="${INSTALL_OPTS} --auth-root-authentication-method=normal"
  INSTALL_OPTS="${INSTALL_OPTS} --skip-test-db"
  INSTALL_OPTS="${INSTALL_OPTS} --datadir=/var/lib/mysql"
  eval /usr/bin/mariadb-install-db "${INSTALL_OPTS}"

  #初始化脚本
  if [ "$(ls -A /docker-entrypoint-initdb.d 2>/dev/null)" ]; then
    MYSQL_CMD="mariadb"
    MARIADBD_OUTPUT=/tmp/mysqldoutput
    mariadbd --user=mysql --silent-startup >"${MARIADBD_OUTPUT}" 2>&1 &
    PID="$!"
    until tail "${MARIADBD_OUTPUT}" | grep -q "Version:"; do
      sleep 0.2
    done
    echo "init: updating system tables"
    eval "${MYSQL_CMD}" </tmp/init
    MYSQL_CMD="${MYSQL_CMD} ${MYSQL_DATABASE} "
    for f in /docker-entrypoint-initdb.d/*; do
      case "${f}" in
      *.sh)
        echo "init: executing ${f}"
        grep -q bash "${f}" && echo "Bash shell scripts are not supported - use busybox sh syntax instead." && exit 1
        /bin/sh "${f}"
        ;;
      *.sql)
        echo "init: adding ${f}"
        eval "${MYSQL_CMD}" <"${f}"
        ;;
      *.sql.gz)
        echo "init: adding ${f}"
        gunzip -c "${f}" | eval "${MYSQL_CMD}"
        ;;
      *) echo "init: ignoring ${f}: not a recognized format" ;;
      esac
    done
    # shutdown temporary mariadbd
    kill -s TERM "${PID}"
    wait "${PID}"

  else
    MYSQLD_OPTS="${MYSQLD_OPTS} --init-file=/tmp/init"
  fi
fi

# make sure directory permissions are correct before starting up
# https://github.com/jbergstroem/mariadb-alpine/issues/54
chown -R mysql:mysql /var/lib/mysql

sh /root/start.sh all start
#eval exec    /usr/bin/mariadbd --user=mysql --skip-name-resolve --skip-host-cache --skip-slave-start