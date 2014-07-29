#!/bin/bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <.war file>"
  exit 1
fi

if [ "$TRAVIS_BRANCH" == "master" ]; then

  echo "Deploying $TRAVIS_BRANCH branch..."

  WAR=$1

  wget -qO- https://toolbelt.heroku.com/install-ubuntu.sh | sh
  heroku plugins:install https://github.com/heroku/heroku-deploy
  heroku deploy:war --war $WAR --app xwp-template

else

  echo "Not deploying $TRAVIS_BRANCH branch"

fi
