# Readme

## Prerequisites
- Docker
- JDK 11
- mongodb-org-tools (for mongo-import)

## Getting started

mongoimport --db=misc --collection=act-a --type=csv --headerline --file=../2a-Act_A.csv --uri "mongodb://root:example@localhost:27017"
mongodb://test:testpwd@localhost/test?authSource=admin

--uri "mongodb://root:example@localhost:27017"

1. Download HIBP DB from https://haveibeenpwned.com/Passwords
2. Start mongoDB
3. Import data:
   ```
   7za x -so ../pwned-passwords-sha1-ordered-by-hash-v7.7z | sed 's/:/,/g' | mongoimport --fields "_id.string(),c.int32()" --columnsHaveTypes --db hibp --collection pwndpsswds --type csv
   ```



If you want to optimize diskspace. You can import data using binary:   
   ```
   7za x -so ../pwned-passwords-sha1-ordered-by-hash-v7.7z | sed 's/:/,/g' | mongoimport --fields "_id.binary(base64),c.int32()" --columnsHaveTypes --db hibp --collection pwndpsswds --type csv
   ```
