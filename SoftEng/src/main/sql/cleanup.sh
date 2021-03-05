#!/bin/bash
user=elli
password=Orpheus2019
database=medicine
sourcefile=$1

mysql --user="$user" --password="$password" --database="$database" --execute="USE $database; SOURCE $sourcefile;"
