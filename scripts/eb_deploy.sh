#!/bin/bash

#exit if there is an error
set -e

if [ -z "$2" ]
then
  echo "usage: $0 eb_app_name eb_env_name"
  exit 1
fi

app_name=$1
eb_env=$2

rm -rf .elasticbeanstalk
#init eb for the project
eb init ${app_name} --platform java --region us-east-1
eb use ${eb_env}

#Edit EB config file which tells EB which artifact to deploy
cat >> .elasticbeanstalk/config.yml <<_EOF
deploy:
  artifact: target/trading-1.0-SNAPSHOT-elastic-beanstalk.zip
_EOF

#deploy
eb deploy

exit 0