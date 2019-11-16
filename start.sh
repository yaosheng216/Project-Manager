#!/bin/sh
ArtifactId=pcss-back-workflow
runId=$(docker ps | grep ${ArtifactId} | awk '{print  $1}' | sed 's/%//g')
if [ -n "$runId" ]; then
  docker stop $runId
fi
containerId=$(docker ps -a | grep ${ArtifactId} | awk '{print  $1}' | sed 's/%//g')
if [ -n "$containerId" ]; then
  docker rm $containerId
fi
oldimage=$(docker images | grep ${ArtifactId} | awk '{print  $3}' | sed 's/%//g')
if [ -n "$oldimage" ]; then
  docker rmi -f $oldimage
fi
docker build -t ${ArtifactId} .
docker run -d -p 8184:8181 --env spring.cloud.config.uri=http://39.96.180.43:8186 --env spring.cloud.config.profile=dev --env spring.cloud.config.label=develop --name ${ArtifactId} ${ArtifactId}:latest
