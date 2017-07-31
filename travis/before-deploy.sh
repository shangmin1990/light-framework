#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   openssl aes-256-cbc -K $encrypted_11b14bfd4b2e_key -iv $encrypted_11b14bfd4b2e_iv -in codesigning.asc.enc -out codesigning.asc -d 
   gpg --fast-import codesigning.asc
fi
