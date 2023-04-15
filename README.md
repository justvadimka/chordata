# chordata

Silly project for my own amusement :)

## Running

You'll need Java 17 or higher. You can check yours with

```
java -version
```

To convert a **b**inary file with the properties to JSON run

```
java -jar chordata.jar -b <your binary file> <JSON file to create>
```

To convert a **J**SON file to the binary format run

```
java -jar chordata.jar -j <your JSON file> <binary file to create>
```

As usual, zero exit code indicates success. Errors are printed to stderr.
