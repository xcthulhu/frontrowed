#!/bin/bash

echo "GET http://localhost:3000/average-of-averages =>" \
	$(curl -s "http://localhost:3000/average-of-averages")

echo "POST http://localhost:3000/add-key?key=foo&value=9 =>" \
	$(curl -s "http://localhost:3000/add-key" -d key=foo -d value=9)
