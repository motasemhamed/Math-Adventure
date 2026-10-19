#!/bin/bash
while true; do
  python3 -m http.server 3000 --directory /app/public_dist
  sleep 1
done
