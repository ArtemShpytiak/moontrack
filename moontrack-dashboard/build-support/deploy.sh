#!/bin/env bash

set -e
project_dir=/c/_Projects/moontrack
deployer_host=jolly.roger.moonmana.com

target=$(find "$project_dir/target" -name *.war)
war_name=$(echo $target | rev | cut -d'/' -f1 | rev)
rsync -v $target $deployer_host:~/build
ssh $deployer_host sudo deploy-war mt-default "~/build/$war_name"
rm -r "$project_dir/src/main/webapp"