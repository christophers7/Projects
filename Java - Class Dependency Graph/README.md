This program that behaves like the Java command line compiler. Whenever we request
that the Java compiler recompiles a particular class, it not only recompiles that class
but every other class that depends upon it, directly or indirectly, and in a particular order.
Typing the file path to one of the graph files provided then clicking "Build Directed Graph" 
will cause the specified input file that contains the class dependency information to be read in
and the directed graph represented by those dependencies to be built.

![file select](https://user-images.githubusercontent.com/74217280/124340047-10d6c080-db78-11eb-8285-77ec8f646001.PNG)

The first file looks like:

![g](https://user-images.githubusercontent.com/74217280/124340155-c86bd280-db78-11eb-89eb-f772e22a93bc.PNG)

So, if we were to recompile ClassE then ClassH and ClassB must also be recompiled as they depend on ClassE. Because ClassB is
recompiled, ClassD and Class F must also as they depend on ClassB. The program achieves topological order by using a depth-first 
search of the graph generated from the file. So recompiling ClassE will look like:

![classE](https://user-images.githubusercontent.com/74217280/124340370-598f7900-db7a-11eb-88a5-765794829098.PNG)

The program is capable of catching and handling endless cycles

![cycle](https://user-images.githubusercontent.com/74217280/124340400-99566080-db7a-11eb-9576-10746ca41771.PNG)

as well as invalid class and file selections

![invalid class](https://user-images.githubusercontent.com/74217280/124340411-b0954e00-db7a-11eb-8f96-f74a4eb2deb9.PNG)
![invalid file](https://user-images.githubusercontent.com/74217280/124340413-b3903e80-db7a-11eb-88fe-54c918d97528.PNG)
