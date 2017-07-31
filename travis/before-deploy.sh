#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   openssl aes-256-cbc -K $encrypted_11b14bfd4b2e_key -iv $encrypted_11b14bfd4b2e_iv -in $TRAVIS_BUILD_DIR/travis/codesigning.asc.enc -out $TRAVIS_BUILD_DIR/travis/codesigning.asc -d
   gpg --fast-import travis/codesigning.asc
fi
