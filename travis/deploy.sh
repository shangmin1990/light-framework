#!/bin/bash
TRAVIS_BRANCH="abc"
TRAVIS_TAG="2.1.0-RELEASE"
RELEASE="-RELEASE"
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    echo "master build"
    mvn clean deploy --settings travis/mvn_settings.xml
fi

if [[ "$TRAVIS_TAG" == *$RELEASE ]]; then
    echo "release build"
    mvn clean deploy --settings travis/mvn_settings.xml -P release
else 
    echo "不包含"
fi
