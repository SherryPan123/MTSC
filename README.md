# MTSC

Implementations of multiple time series compression algorithm

## Requirement
- Java Runtime Environment 1.8+
- Apache Maven 3.5.0

## Build and Install
Clone the repository:

```
$ git clone https://github.com/SherryPan123/MTSC.git
```
Build the project:
```
$ cd MTSC
$ mvn package
```
The executable program will be generated in the ``target`` directory.

Move ``multiple-ts-compression-1.0-SNAPSHOT.jar`` out of ``target``.


## Configuration
1. Edit the file ``data/info.txt``, each line is a dataset in the form
'A:B:C:D:E:F:G'
	
    A             | B             | C             | D             | E            | F             | G           |
	:-----------: | :-----------: | :-----------: | :-----------: | -----------: | :-----------: | :-----------:
	Dataset name |Input data path|Compressed data path|Decompressed data path|Size|Length|eps


2. Run *Compress*

```
java -cp multiple-ts-compression-1.0-SNAPSHOT.jar cn.edu.fudan.mtsc.Compress
```

3. Run *Decompress*
```
java -cp multiple-ts-compression-1.0-SNAPSHOT.jar cn.edu.fudan.mtsc.Decompress [dataset] [signalId]
```
There are at most two parameters, **dataset** is required and **signalId** is optional.
**dataset** should be found in the file ``data/info.txt``.

