#!/bin/bash

OUT=$(curl -s "http://localhost:3000/average-of-averages")
echo $OUT
