# Readme

## Prerequisites
- Docker
- JDK 11
- mongodb-org-tools (for mongo-import)

## Getting started
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


## Testing
After data is imported in Mongo-DB, start the service and call the ```/api/{hash-prefix}``` endpoint.

e.g. sha1("password") => 5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8 => ```GET localhost/api/5baa6```
