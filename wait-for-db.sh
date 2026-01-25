#!/bin/sh
# wait-for-db.sh

set -e

host="$1"
shift
cmd="$@"

until mysql -h "$host" -u"$DB_USER" -p"$DB_PASSWORD" -e 'select 1' > /dev/null 2>&1; do
  echo "Waiting for MySQL at $host..."
  sleep 2
done

exec $cmd
