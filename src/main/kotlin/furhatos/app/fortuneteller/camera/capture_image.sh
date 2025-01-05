#!/bin/bash
id=$1
storage_account_name=$2
storage_container_name=$3
storage_sas_token=$4

ffmpeg -y -f avfoundation -framerate 30 -pixel_format uyvy422 -i "2" -frames:v 1 -update 1 -probesize 10M "furhat_image_${id}.jpg"

azcopy copy "furhat_image_${id}.jpg" "https://${storage_account_name}.blob.core.windows.net/${storage_container_name}/furhat_image_${id}.jpg?${storage_sas_token}"

rm "furhat_image_${id}.jpg"