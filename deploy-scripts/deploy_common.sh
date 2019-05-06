#!/usr/bin/env bash
. $(dirname $0)/deploy_functions.sh
set -e

SLUG="47deg/helios"
JDK="oraclejdk8"
BRANCH="master"
VERSION_NAME=$(getProperty "VERSION_NAME")