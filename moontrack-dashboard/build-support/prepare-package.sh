#!/bin/env bash

set -e
project_dir=/c/_Projects/moontrack
deployer_host=jolly.roger.moonmana.com
cluster_name=mt-default

mkdir -p "$project_dir/src/main/webapp/WEB-INF"
ssh $deployer_host "deploy-config $cluster_name logs" > "$project_dir/src/main/webapp/WEB-INF/logback.xml"
mkdir -p "$project_dir/src/main/webapp/conf"
ssh $deployer_host "deploy-config $cluster_name" > "$project_dir/src/main/webapp/conf/services.json"
rsync -av "$project_dir/"*.jsp "$project_dir/src/main/webapp"